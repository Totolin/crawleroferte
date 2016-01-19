package com.aceucv.vpe.crawler.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aceucv.vpe.crawler.entities.Category;

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
	
	public static Map<Integer, Category> categories;

	public static void updateFilteredCategories(List<Category> filtered) {
		System.out.println("Updating categories with " + filtered.size() + " categories...");
		categories = new HashMap<Integer,Category>();
		for (Category category : filtered) {
			categories.put(category.getId(), category);
		}		
	}
}
