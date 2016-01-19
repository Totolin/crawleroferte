package com.aceucv.vpe.crawler.source;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.CrawlPool;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.Resources;

/**
 * Controller : Class used to connect the visual interface objects (Swing) with the back-end,
 * using action listeners which this this class assigns to each GUI object. 
 * 
 * This class is also responsible to connect the back-end crawling engine (Crawler class) to 
 * an action within it's specific action listener. 
 * 
 * Among all mentioned, this class will take care of grabbing the data resulted from crawling,
 * place it in containers and send it to the GUI to be printed and/or to the DatabaseManage to
 * be stored in a MySQL database.
 *
 */
public class Controller {

	private MainWindow window;
	private Crawler crawler;
	private DatabaseManager dtb;

	/**
	 * Constructor for the Controller class
	 * @param window - MainWindow reference, used to display all data as a GUI.
	 * @param crawler - Crawler reference, used as the main crawling engine.
	 * @param dtb - DatabaseManager reference, used to store/read data to/from a database.
	 */
	public Controller(MainWindow window, Crawler crawler, DatabaseManager dtb) {
		this.window = window;
		this.crawler = crawler;
		this.dtb = dtb;
		setConnections();
		setUpDatabaseItems();
	}


	/**
	 * Method used to link all MainWindow Swing objects (GUI objects) to all their 
	 * desired actions, using action listeners.
	 */
	private void setConnections() {
		// Initialize the arrays we will be using during runtime
		Resources.categories = new HashMap<Integer, Category>();
	
		// Link interface objects with their action listeners
		window.buttonCrawlCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Run on a separate thread so not to interfere with the main one
				// which is used to display the GUI.
				window.clearCategoryList();
				window.clearLoadingBar();
				
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
				// Run on a separate thread so not to interfere with the main one
				// which is used to display the GUI.
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
				// Run on a separate thread so not to interfere with the main one
				// which is used to display the GUI.
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
		
		window.buttonSelectAllOffers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Run on a separate thread so not to interfere with the main one
				// which is used to display the GUI.
				Thread thread = new Thread() {
					public void run() {
						window.toggleSelectionOffers(true);
					}
				};
				thread.start();
			}
		});
		
		window.buttonDeselectAllOffers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Run on a separate thread so not to interfere with the main one
				// which is used to display the GUI.
				Thread thread = new Thread() {
					public void run() {
						window.toggleSelectionOffers(false);
					}
				};
				thread.start();
			}
		});
		
		window.buttonCrawlItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Crawl for each category's items on a separate thread
				window.clearLoadingBar();
				Thread[] threads = new Thread[Resources.categories.size()];
				final int progressInc = 30/threads.length;
				int size = 0;
				for (final Category category : Resources.categories.values()) {
					threads[size] = new Thread() {
						public void run() {
							category.processItems();
							window.progressCategories.setValue(
									window.progressCategories.getValue()
									+progressInc
									);
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
								items.put(category.getId(), crawler.crawlItems(category));
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
				
				System.out.println("Waiting for all " + size + " threads to finish..");
				
				// Save all the items crawled in the back-end
				System.out.println("Finished crawling " + items.size() + " categories.");
				
				System.out.println("Starting to crawl each item individually");
				CrawlPool pool = new CrawlPool();
				pool.populateItemPrices(crawler, items);
				
				System.out.println("Finished crawling idividual items.");
				System.out.println("Test item :");
				for(Entry<Integer, List<Item>> entry : items.entrySet()) {
		        	List<Item> currentList = entry.getValue();
		        	
		        	System.out.println(currentList.get(0).getTitle() + " " + currentList.get(0).getPrice());
		        	System.out.println(currentList.get(1).getTitle() + " " + currentList.get(1).getPrice());
		        	System.out.println(currentList.get(2).getTitle() + " " + currentList.get(2).getPrice());
				}
			}
		});
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
	
	private void setUpDatabaseItems() {
		List<Category> savedCategories = dtb.getCategories();
		window.populateCategoryList(savedCategories);
		Resources.updateFilteredCategories(savedCategories);
	}

}

