package com.aceucv.vpe.crawler.engine;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aceucv.vpe.crawler.entities.Item;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.Resources;
/**
 * Class used to manage threads for a multithreaded approach to crawling a website
 * @author cristiantotolin
 *
 */
public class CrawlPool {

	/**
	 * Starts sending out threads for each individual item to be crawled
	 * @param crawler
	 * @param categoryMap
	 * @param window
	 */
    public void populateItemPrices (final Crawler crawler, Map<Integer,List<Item>> categoryMap, final MainWindow window) {
    	
    	// Create an executor to pool all our threads
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Get entire number of items for the progress bar
        int totalItems = 0;
        
        for(Entry<Integer, List<Item>> entry : categoryMap.entrySet()) {
        	totalItems += entry.getValue().size();
		}
        
        // Assign progress bar tick
        if (totalItems == 0) {
        	// Website denied further access
        	window.settingsLabel.setText(Resources.label_text_crawl_error);
        	return;
        }
        
        float chunkSize  = (100 - window.progressCategories.getValue()) / totalItems ;
        final int progressTick = 2*(int) Math.ceil(chunkSize);
        System.out.println("Tick is : " + progressTick);
        // Assign threads for each job accordingly
        for(Entry<Integer, List<Item>> entry : categoryMap.entrySet()) {
        	List<Item> currentList = entry.getValue();
        	
        	for (final Item item : currentList) {
        		Thread thread = new Thread() {
    				public void run() {
    					crawler.setPrices(item);
    					window.incrementProgress(progressTick);
    				}
    		    };
    		    
    		    // Tell the pool to give our process a thread
    		    executor.execute(thread);
        	}
		}
        
        // Shut it down once we are finished
        executor.shutdown();
        while (!executor.isTerminated()) {
        	// Wait for it
        }
        
        System.out.println("Threads have finished crawling item prices.");
    }
}