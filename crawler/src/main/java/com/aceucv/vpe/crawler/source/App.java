package com.aceucv.vpe.crawler.source;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws IOException {
        Map<Integer,Category> categories = Crawler.crawlCategories("http://www.emag.ro/homepage", "http://emag.ro");

        categories.get(12).processItems();
        List<Item> items = Crawler.crawlItems(categories.get(12));

        System.out.println(items.get(0).getURL());

        for (Item item : items) {
            Crawler.setPrices(item);
        }
    }
}
