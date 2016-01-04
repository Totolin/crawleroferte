package com.aceucv.vpe.crawler.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.entities.Category;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		// Map<Integer, Category> categories =
		// Crawler.crawlCategories("http://www.emag.ro/homepage",
		// "http://emag.ro");
		//
		// categories.get(12).processItems();
		// List<Item> items = Crawler.crawlItems(categories.get(12));
		//
		// for (Item item : items) {
		// Crawler.setPrices(item);
		// }

		// MainWindow window = new MainWindow();
		// Crawler crawler = new Crawler();
		// Controller controller = new Controller(window, crawler);

		Map<Integer, Category> testmap = new HashMap<Integer, Category>();
		testmap.put(1, new Category(1, "asd", "casvsa", "ierige"));
		DatabaseManager mgr = new DatabaseManager();
		mgr.insetCategories(testmap);
	}
}
