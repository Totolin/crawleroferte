package com.aceucv.vpe.crawler.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;
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
	private DatabaseManager dtb;

	public Controller(MainWindow window, Crawler crawler, DatabaseManager dtb) {
		this.window = window;
		this.crawler = crawler;
		this.dtb = dtb;
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
		
		window.buttonSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						System.out.println("Saving changes from settings...");

						// Get all selected categories
						List<Integer> indexSelected = window.getSelectedCategories();
						
						// Grab the objects using the indexes
						List<Category> filtered = Utils.getCategoriesFromIndex(indexSelected,
								Resources.categories);

						// Store them in the database 
						dtb.insetCategories(filtered);
						
						// Update the GUI list
						window.populateCategoryList(filtered);
						
						// Update the resources
						Resources.updateFilteredCategories(filtered);
					}
				};
				thread.start();
			}
		});
		
		window.buttonSelectAllCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						if (!window.allSelected) {
							window.toggleSelectionCategories(true);
							window.allSelected = true;
						} else {
							window.toggleSelectionCategories(false);
							window.allSelected = false;
						}
					}
				};
				thread.start();
			}
		});
		
		window.buttonCrawlItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Crawl for each category's items on a separate thread
				Thread[] threads = new Thread[Resources.categories.size()];
				int size = 0;
				for (final Category category : Resources.categories.values()) {
					threads[size] = new Thread() {
						public void run() {
							category.processItems();
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
							items.put(category.getId(), crawler.crawlItems(category));
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
				
				// Save all the items crawled in the backend
				System.out.println(items.size());
			}
		});
	}
}
