package com.aceucv.vpe.crawler.entities;

public class Offer {

	private String name;
	private String category;
	private double price;
	private double discount;
	private String URL;
	
	public Offer(String name, String category, double price, double discount, String uRL) {
		super();
		this.name = name;
		this.category = category;
		this.price = price;
		this.discount = discount;
		URL = uRL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPrice() {
		return Double.toString(price);
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDiscount() {
		return Double.toString(discount);
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
}
