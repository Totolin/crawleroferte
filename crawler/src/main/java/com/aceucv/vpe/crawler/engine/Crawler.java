package com.aceucv.vpe.crawler.engine;

import java.awt.Color;
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
 * Class used as a main crawling engine for the application
 * Is capable of searching for Categories, Items, Offers (later on)
 * 
 * Created by ctotolin on 22-Nov-15.
 */
public class Crawler {

	private int oneOfCategories = -1;
	public Crawler() {
	}

	/**
	 * Crawls for all possible categories found inside the base website.
	 * @param baseURL
	 * @param pseudoURL
	 * @param window
	 * @return
	 */
	public Map<Integer, Category> crawlCategories(String baseURL, String pseudoURL, MainWindow window) {
		
		Document doc = null;
		Random gen = new Random();
		window.settingsLabel.setText(Resources.label_text_crawl_start);
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
				window.incrementProgress(progressTick);
				
				// Add the new category to our view
				window.addCategoryToList(newCategory);

				try {
					Thread.sleep(gen.nextInt(100) + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Finished crawling categories");
		window.settingsLabel.setText(Resources.label_text_finished_cat);
		window.categoriesTable.setFocusable(true);
		return categories;
	}
	
	
	/**
	 * Searches for all items inside a desired category
	 * @param category
	 * @return
	 * @throws IOException
	 */
	public List<Item> crawlItems(Category category) throws IOException{
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
			doc = Jsoup.connect(baseURL + subcategories.get(i) + "p" + currentPage + "/c").get();

			// Grab rest of pages until none are left
			// TODO : current page condition back to 10
			while (doc.baseUri().compareTo(previousURL) != 0 && currentPage < 2) {
				// Process items
				allItems.addAll(getItems(category.getRootURL(), doc, id));

				// Check next page
				currentPage++;
				previousURL = doc.baseUri();
				doc = Jsoup.connect(baseURL + subcategories.get(i) + "p" + currentPage + "/c").get();
			}
		}

		return allItems;
	}
	
	/**
	 * Crawls all items from all categories
	 * @param window
	 * @return
	 */
	public Map<Integer, List<Item>> crawlAllItems (final MainWindow window) {
		
		Thread[] threads = new Thread[Resources.categories.size()];
		final int progressInc = 30/threads.length;
		int size = 0;
		for (final Category category : Resources.categories.values()) {
			threads[size] = new Thread() {
				public void run() {
					category.processItems();
					window.incrementProgress(progressInc);
				}
			};
			threads[size].start();
			size++;
		}
		
		// Join the threads
		for(int i = 0; i < size; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
			
		System.out.println("Finished grabbing all subcategories");
		final Map<Integer,List<Item>> items = new HashMap<Integer,List<Item>>();
		
		// Crawl for each category's items on a separate thread
		System.out.println("Starting to crawl items");
		threads = new Thread[Resources.categories.size()];
		
		size = 0;
		for (final Category category : Resources.categories.values()) {
			threads[size] = new Thread() {
				public void run() {
					try {
						items.put(category.getId(), crawlItems(category));
						oneOfCategories = category.getId();
					} catch (IOException e) {
						websiteFailure(window);
					}
				}
			};
			threads[size].start();
			size++;
		}
		
		// Join the threads
		for(int i = 0; i < size; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		// Also put test website there
		if (oneOfCategories != -1) {
			Item fakeItem = new Item("http://127.0.0.1","", oneOfCategories);
			items.get(oneOfCategories).add(fakeItem);
		}
		
		System.out.println("Waiting for all " + size + " threads to finish..");
		
		// Save all the items crawled in the back-end
		System.out.println("Finished crawling " + items.size() + " categories.");
		
		System.out.println("Starting to crawl each item individually");
		CrawlPool pool = new CrawlPool();
		pool.populateItemPrices(this, items, window);
		
		System.out.println("Finished crawling idividual items.");
		
		return items;
	}

	/**
	 * Get details about an item found inside a category/subcategory
	 * @param baseURL
	 * @param doc
	 * @param id
	 * @return
	 */
	private static List<Item> getItems(String baseURL, Document doc, int id) {
		List<Item> items = new ArrayList<Item>();
		for (Element elem : doc.select("a[href]")) {
			if (elem.hasAttr("class") && elem.getElementsByAttribute("class").attr("class")
					.compareTo("product-container js-product-container") == 0) {

				String privURL = elem.getElementsByAttribute("href").attr("href");
				items.add(new Item(baseURL, privURL, id));
			}
		}
		return items;
	}

	/** 
	 * Crawls for and sets a price for an item
	 * @param item
	 */
	public void setPrices(Item item) {
		try {
			Document doc = Jsoup.connect(item.getBaseURL() + item.getURL()).get();

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
							item.obfuscateId();
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
	
	private void websiteFailure(final MainWindow window) {
		Thread thread = new Thread() {
			public void run() {
				window.progressCategories.setForeground(Color.red);
				window.settingsLabel.setText(Resources.label_text_crawl_error);
			}
		};
		thread.start();
	}
}
