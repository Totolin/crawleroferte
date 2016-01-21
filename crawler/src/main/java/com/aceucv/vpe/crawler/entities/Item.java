package com.aceucv.vpe.crawler.entities;

/**
 * Class which holds details for an Item
 * 
 * Created by ctotolin on 22-Nov-15.
 */
public class Item {

	private double price;
	private String URL;
	private String baseURL;
	private int id;
	private String title;
	private int categoryId;

	public Item(String baseURL, String URL, int id) {
		this.setBaseURL(baseURL);
		this.URL = URL;
		this.categoryId = id;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.categoryId = id;
	}
	
	public int getCategoryId () {
		return categoryId;
	}

	public String getTitle() {
		return title;
	}
	
	public String getPriceString() {
		return Double.toString(price);
	}
	
	public String getIdString() {
		return Integer.toString(id);
	}
	
	public String getCategoryString() {
		return Integer.toString(categoryId);
	}

	public void setTitle(String title) {
		// Remove junk after title
		title = title.replaceAll(" - eMAG.ro","");
		title = title.replaceAll("'", "");
		
		this.title = title;
	}
	
	/**
	 * Randomly generates a new ID
	 */
	public void obfuscateId() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((URL == null) ? 0 : URL.hashCode());
		result = prime * result + ((baseURL == null) ? 0 : baseURL.hashCode());
		result = prime * result + categoryId;
		result = prime * result + id;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		this.id = result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (URL == null) {
			if (other.URL != null)
				return false;
		} else if (!URL.equals(other.URL))
			return false;
		if (baseURL == null) {
			if (other.baseURL != null)
				return false;
		} else if (!baseURL.equals(other.baseURL))
			return false;
		if (categoryId != other.categoryId)
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
}
