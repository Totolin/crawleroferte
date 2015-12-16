package com.aceucv.vpe.crawler.source;

import com.aceucv.vpe.crawler.gui.MainWindow;

/**
 * Hello world!
 *
 */
public class App {
	// public static void main(String[] args) throws IOException {
	// Map<Integer,Category> categories =
	// Crawler.crawlCategories("http://www.emag.ro/homepage", "http://emag.ro");
	//
	// categories.get(12).processItems();
	// List<Item> items = Crawler.crawlItems(categories.get(12));
	//
	// System.out.println(items.get(0).getURL());
	//
	// for (Item item : items) {
	// Crawler.setPrices(item);
	// }
	// }

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainWindow frame = new MainWindow();
				Controller controller = new Controller(frame);
			}
		});
	}
}
