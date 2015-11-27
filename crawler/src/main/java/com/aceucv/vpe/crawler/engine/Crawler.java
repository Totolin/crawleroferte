package com.aceucv.vpe.crawler.engine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by ctotolin on 22-Nov-15.
 */
public class Crawler {

    public static Map<Integer,Category> crawlCategories (String baseURL, String pseudoURL) {
        Document doc = null;
        try {
            doc = Jsoup.connect(baseURL).get();
        } catch (IOException e) {
            System.out.println("Cannot connect to " + baseURL);
            return null;
        }

        Elements filtered = new Elements();
        Map<Integer,Category> categories = new HashMap<Integer,Category>();

        for (Element elem : doc.select("a[href]")) {
            if (elem.hasAttr("id")) {
                // Parse category data
                int id = Category.parseID(elem);
                if (id == 0) continue;

                String description = Category.parseDescription(elem);
                if (description == "") continue;

                String url = Category.parseURL (elem);
                if (description == "") continue;

                categories.put(id, new Category(id,description,url, pseudoURL));
            }
        }

        return categories;
    }

    public static List<Item> crawlItems (Category category) throws IOException {
        List<String> subcategories = category.getSubs();
        String baseURL = category.getRootURL();
        Document doc = null;
        int id = category.getId();
        List<Item> allItems = new ArrayList<Item>();

        // TODO : Reset to normal state
        for (int i=0; i<4; i++) {
            // Inside a subcategory we will find items. We need to find them
            // Must also travel through pages
            int currentPage = 1;
            String previousURL = "";

            // Get first page
            System.out.println(baseURL + subcategories.get(i) + "p"+currentPage+"/c");
            doc = Jsoup.connect(baseURL + subcategories.get(i) + "p"+currentPage+"/c").get();

            // Grab rest of pages until none are left
            // TODO : current page condition back to 10
            while (doc.baseUri().compareTo(previousURL)!=0 && currentPage<2) {
                // Process items
                allItems.addAll(getItems(doc,id));

                // Check next page
                currentPage++;
                previousURL = doc.baseUri();
                doc = Jsoup.connect(baseURL + subcategories.get(i) + "p"+currentPage+"/c").get();
            }
        }

        return allItems;
    }

    private static List<Item> getItems (Document doc, int id) {
        List<Item> items = new ArrayList<Item>();
        for (Element elem : doc.select("a[href]")) {
            if (elem.hasAttr("class") &&
                    elem.getElementsByAttribute("class").attr("class")
                            .compareTo("product-container js-product-container")==0) {

                String privURL = elem.getElementsByAttribute("href").attr("href");
                items.add(new Item(privURL, id));
            }
        }

        return items;
    }

    public static void setPrices (Item item) {
        try {
            Document doc = Jsoup.connect("http://emag.ro" + item.getURL()).get();

            for (Element elem : doc.select("p")) {
                if (elem.className().compareTo("price")==0) {
                    Elements subElems = elem.getElementsByTag("span");
                    for (Element subElem : subElems) {
                        if (subElem.className().compareTo("money-int")==0) {
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
