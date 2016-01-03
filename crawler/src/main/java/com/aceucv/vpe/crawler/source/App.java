package com.aceucv.vpe.crawler.source;

import java.io.IOException;

import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.gui.MainWindow;

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

		MainWindow window = new MainWindow();
		Crawler crawler = new Crawler();
		Controller controller = new Controller(window, crawler);
	}
}
