package com.aceucv.vpe.crawler.engine;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.Resources;

/**
 * Created by ctotolin on 22-Nov-15.
 */
public class Crawler {

	public Crawler() {
	}

	public Map<Integer, Category> crawlCategories(String baseURL, String pseudoURL, MainWindow window) {
		Document doc = null;
		Random gen = new Random();
		try {
			doc = Jsoup.connect(baseURL).get();
		} catch (IOException e) {
			System.out.println("Cannot connect to " + baseURL);
			return null;
		}

		Map<Integer, Category> categories = new HashMap<Integer, Category>();
		Elements selection = doc.select("a[href]");
		window.settingsLabel.setText(Resources.label_text_crawling);
		window.categoriesTable.setFocusable(false);
		int progressTick = selection.size() / 100;
		window.progressCategories.setValue(0);
		for (Element elem : selection) {
			if (elem.hasAttr("id")) {
				// Parse category data
				int id = Category.parseID(elem);
				if (id == 0)
					continue;

				String description = Category.parseDescription(elem);
				if (description == "")
					continue;

				String url = Category.parseURL(elem);
				if (description == "")
					continue;

				// Add the category to our final list
				Category newCategory = new Category(id, description, url, pseudoURL);
				categories.put(id, newCategory);

				// Increment the progress bar
				window.progressCategories.setValue(window.progressCategories.getValue() + progressTick);

				// Add the new category to our view
				window.addCategoryToList(newCategory);

				try {
					Thread.sleep(gen.nextInt(100) + 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("Finished crawling categories");
		window.settingsLabel.setText(Resources.label_text_finished_cat);
		window.categoriesTable.setFocusable(true);
		return categories;
	}

	public List<Item> crawlItems(Category category){
		List<String> subcategories = category.getSubs();
		String baseURL = category.getRootURL();
		Document doc = null;
		int id = category.getId();
		List<Item> allItems = new ArrayList<Item>();

		for (int i = 0; i < subcategories.size(); i++) {
			// Check subcategory integrity
			if (subcategories.get(i) == null || subcategories.get(i).equals("") || subcategories.get(i).equals(" ")) {
				continue;
			}
			// Inside a subcategory we will find items. We need to find them
			// Must also travel through pages
			int currentPage = 1;
			String previousURL = "";

			// Get first page
			System.out.println(baseURL + subcategories.get(i) + "p" + currentPage + "/c");
			try {
				doc = Jsoup.connect(baseURL + subcategories.get(i) + "p" + currentPage + "/c").get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Grab rest of pages until none are left
			// TODO : current page condition back to 10
			while (doc.baseUri().compareTo(previousURL) != 0 && currentPage < 2) {
				// Process items
				allItems.addAll(getItems(doc, id));

				// Check next page
				currentPage++;
				previousURL = doc.baseUri();
				try {
					doc = Jsoup.connect(baseURL + subcategories.get(i) + "p" + currentPage + "/c").get();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return allItems;
	}

	private static List<Item> getItems(Document doc, int id) {
		List<Item> items = new ArrayList<Item>();
		for (Element elem : doc.select("a[href]")) {
			if (elem.hasAttr("class") && elem.getElementsByAttribute("class").attr("class")
					.compareTo("product-container js-product-container") == 0) {

				String privURL = elem.getElementsByAttribute("href").attr("href");
				items.add(new Item(privURL, id));
			}
		}
		return items;
	}

	public static void setPrices(Item item) {
		try {
			Document doc = Jsoup.connect("http://emag.ro" + item.getURL()).get();

			for (Element elem : doc.select("title")) {
				item.setTitle(elem.ownText());
			}

			for (Element elem : doc.select("p")) {
				if (elem.className().compareTo("price") == 0) {
					Elements subElems = elem.getElementsByTag("span");
					for (Element subElem : subElems) {
						if (subElem.className().compareTo("money-int") == 0) {
							System.out.println(subElem.ownText());
							item.setPrice(Float.parseFloat(subElem.ownText()));
						}
					}
				}
			}
		} catch (UnknownHostException e) {
			// Not printing errors
		} catch (IOException e) {
			// Not printing errors
		}
	}
}
