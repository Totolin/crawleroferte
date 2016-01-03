package com.aceucv.vpe.crawler.entities;

/**
 * Created by ctotolin on 22-Nov-15.
 */
public class Item {

	private float price;
	private String URL;
	private int id;
	private String title;

	public Item(String URL, int id) {
		this.URL = URL;
		this.id = id;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
