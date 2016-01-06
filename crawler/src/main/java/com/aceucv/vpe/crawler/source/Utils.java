package com.aceucv.vpe.crawler.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aceucv.vpe.crawler.entities.Category;

public class Utils {

	public static List<Category> getCategoriesFromIndex(List<Integer> indexes, Map<Integer,Category> categories) {
		List<Category> filtered = new ArrayList<Category>();
		for (Integer idx : indexes) {
			filtered.add(categories.get(idx));
		}
		return filtered;
	}
}
