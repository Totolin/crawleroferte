package com.aceucv.vpe.crawler.entities;

/**
 * Class which holds information about a current Offer
 * @author cristiantotolin
 *
 */
public class Offer {

	private String name;
	private int category;
	private int id;
	private double price;
	private double discount;
	private String URL;
	private String rootURL;
	
	public Offer(String name, int categoryId, int id, double price, double discount, String rootURL, String URL) {
		this.name = name;
		this.category = categoryId;
		this.price = price;
		this.discount = discount;
		this.URL = URL;
		this.id = id;
		this.setRootURL(rootURL);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getPriceString() {
		return Double.toString(price);
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public double getDiscount() {
		return this.discount;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDiscountString() {
		return Double.toString(discount);
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getURL() {
		return URL;
	}
	
	public String getIdString() {
		return Integer.toString(id);
	}
	
	public String getCategoryString() {
		return Integer.toString(category);
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}
}
