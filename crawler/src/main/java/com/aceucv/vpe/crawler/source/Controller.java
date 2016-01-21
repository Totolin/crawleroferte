package com.aceucv.vpe.crawler.source;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;
import com.aceucv.vpe.crawler.entities.Offer;
import com.aceucv.vpe.crawler.gui.LoadingScreen;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.Resources;
import com.aceucv.vpe.crawler.model.XMLParser;

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
	private LoadingScreen loadScreen;
	private XMLParser parser;

	/**
	 * Constructor for the Controller class
	 * @param window - MainWindow reference, used to display all data as a GUI.
	 * @param crawler - Crawler reference, used as the main crawling engine.
	 * @param dtb - DatabaseManager reference, used to store/read data to/from a database.
	 */
	public Controller(MainWindow window, Crawler crawler, DatabaseManager dtb, LoadingScreen loadScrn, XMLParser parser) {
		this.window = window;
		this.crawler = crawler;
		this.dtb = dtb;
		this.loadScreen = loadScrn;
		this.parser = parser;
	}
	
	// Start working as a controller
	public void start () {
		
		// Display a loading screen initially
		Thread thread = new Thread() {
			public void run() {
				// Show the loading screen
				loadScreen.setVisible(true);
				
			}
		};
		thread.start();
		
		// Meanwhile, start loading resources
		setConnections();
		setUpCategories();
		setUpOffers();
		
		// Finished loading everything, show the main UI
		loadScreen.dispose();
		window.showWindow();
	}
	
	
	/**
	 * Method used to link all MainWindow Swing objects (GUI objects) to all their 
	 * desired actions, using action listeners.
	 */
	private void setConnections() {
		// Display loading message
		loadScreen.setLoadingText(Resources.label_text_setting_connect);
		
		// Initialize the arrays we will be using during runtime
		Resources.offers = new HashMap<Integer, Offer>();
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
						parser.writeToFileCategories(filtered);
						
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
				// Check if we have anything to crawl
				if (Resources.categories.size() > 0) {
					// Crawl for each category's items on a separate thread
					window.clearLoadingBar();
					
					Thread thread = new Thread() {
						public void run() {
							// Get all items from the categories / subcategories
							Map<Integer, List<Item>> crawledItems = crawler.crawlAllItems(window);
							
							// Compare with database and get back offers
							Resources.offers = dtb.insertItems(crawledItems);
							
							System.out.println("Done searching for all ");
							
							// Save offers in XML
							List<Offer> offersList = Resources.getOffersAsList();
							parser.writeToFileOffers(offersList);
							
							// Update the UI with the new offers
							window.populateOfferList(offersList);
						}
					};
					thread.start();
				}
			}
		});
		
		window.buttonOpenOffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the offer currently selected
				Offer offer = window.getSelectedItem();
				
				System.out.println("Going to : " + offer.getRootURL() + offer.getURL());
				if(Desktop.isDesktopSupported()) {
					  try {
						Desktop.getDesktop().browse(new URI(offer.getRootURL() + offer.getURL()));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		window.buttonDeleteOffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get all offers to delete
				List<Integer> offers = window.getSelectedItemsIndex();
				
				// Remove them from model
				Resources.removeOffers(offers);
				
				// Update xml
				List<Offer> offersList = Resources.getOffersAsList();
				parser.writeToFileOffers(offersList);
				
			}
		});
		
		takeUpTime(900);
		loadScreen.incremenetLoading(40);
	}
	
	private void setUpCategories() {
		loadScreen.setLoadingText(Resources.label_text_reading_categories);
		List<Category> savedCategories = parser.readFromFileCategories();
		window.populateCategoryList(savedCategories);
		Resources.updateFilteredCategories(savedCategories);
		takeUpTime(1200);
		loadScreen.incremenetLoading(30);
	}
	
	private void setUpOffers() {
		loadScreen.setLoadingText(Resources.label_text_reading_offers);
		List<Offer> savedOffers = parser.readFromFileOffers();
		window.populateOfferList(savedOffers);
		Resources.updateOffers(savedOffers);
		takeUpTime(1100);
		loadScreen.incremenetLoading(30);
	}

	private void takeUpTime (int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

