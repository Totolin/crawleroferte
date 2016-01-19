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
		// Remove junk after title
		title = title.replaceAll(" - eMAG.ro","");
		title = title.replaceAll("'", "");
		
		this.title = title;
	}
	
	public void obfuscateId() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
		result = prime * result + ((this.URL == null) ? 0 : this.URL.hashCode());
		
		this.id = result;
	}
}
