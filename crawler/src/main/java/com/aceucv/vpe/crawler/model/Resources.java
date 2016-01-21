package com.aceucv.vpe.crawler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Offer;

/**
 * Resources: Class used as a Model for the project, which is used to store data used at runtime.
 * Contains labels and texts used for the GUI, lists of objects used to fill lists in the GUI,
 * and useful methods which do not rely on any of the other components of the class.
 * 
 */
public class Resources {

	public static String label_text_offers 		 		= "    You can manage your offers here   ";
	public static String label_text_settings 	 		= "    You can view your settings here   ";
	public static String label_text_crawl_start         = "   Beginning to crawl for categories  ";
	public static String label_text_crawling 	 		= "      Searching for all categories    ";
	public static String label_text_finished_cat 		= "   Finished searching for categories  ";
	public static String label_text_selection_offers 	= "    Select/Deselect multiple offers   ";
	public static String label_text_crawl_error         = "    Website denied further requests   ";
	
	public static String label_text_reading_categories  = "    Reading categories from XML...    ";
	public static String label_text_checking_offers     = "     Checking previous offers...      ";
	public static String label_text_updating_backend    = "          Updating backend...         ";
	public static String label_text_setting_connect     = "      Establishing connections...     ";
	public static String label_text_reading_offers      = "      Reading offers from XML...      ";
	public static Map<Integer, Category> categories;
	public static Map<Integer, Offer> offers;
	
	
	/**
	 * Updates contained categories with a new filtered list
	 * @param filtered
	 */
	public static void updateFilteredCategories(List<Category> filtered) {
		System.out.println("Updating categories with " + filtered.size() + " categories...");
		categories = new HashMap<Integer,Category>();
		for (Category category : filtered) {
			categories.put(category.getId(), category);
		}		
	}
	
	/**
	 * Updates contained offers from a new list
	 * @param newOffers
	 */
	public static void updateOffers (List<Offer> newOffers) {
		offers = new HashMap<Integer, Offer>();
		for (Offer offer : newOffers) {
			offers.put(offer.getId(), offer);
		}
	}
	
	/**
	 * Returns all offers contained, but as a list
	 * @return
	 */
	public static List<Offer> getOffersAsList() {
		List<Offer> offersList = new ArrayList<Offer>();
		for (Offer offer : offers.values()) {
			offersList.add(offer);
		}
		return offersList;
	}
	
	/**
	 * Removes offers based on a list of indexes
	 * @param indexes
	 */
	public static void removeOffers (List<Integer> indexes) {
		for (Integer index : indexes) {
			System.out.println("index : " + index);
			offers.remove(index);
		}
	}
}
