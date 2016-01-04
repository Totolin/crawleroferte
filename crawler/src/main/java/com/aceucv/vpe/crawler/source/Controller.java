package com.aceucv.vpe.crawler.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.Resources;

/**
 * Controller used to connect the GUI with the backend, so that any action which
 * takes places via the user interactions will reflect in the backend processes.
 *
 */
public class Controller {

	private MainWindow window;
	private Crawler crawler;

	public Controller(MainWindow window, Crawler crawler) {
		this.window = window;
		this.crawler = crawler;

		setConnections();
	}

	public void setConnections() {
		window.buttonCrawlCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						System.out.println("Starting categories crawl");

						// Start searching for all categories
						Map<Integer, Category> categories = crawler.crawlCategories("http://www.emag.ro/homepage",
								"http://emag.ro", window);

						// Save them for later use (for e.g. storing in dtb)
						Resources.categories = categories;
					}
				};
				thread.start();
			}
		});
	}

}
