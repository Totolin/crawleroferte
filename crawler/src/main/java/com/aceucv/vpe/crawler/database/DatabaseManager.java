package com.aceucv.vpe.crawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;
import com.aceucv.vpe.crawler.entities.Offer;
import com.aceucv.vpe.crawler.model.Resources;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * Class which takes care of querrying the database.
 * @author cristiantotolin
 *
 */
public class DatabaseManager {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/crawler";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";


	/**
	 * Inserts a list of categories in the database
	 * @param categories
	 */
	public void insertCategories(List<Category> categories) {

		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;

			// Empty Table
			String delete_sql = "DELETE FROM categories";
			stmt.executeUpdate(delete_sql);

			// Utility values
			for (Category category : categories) {
				sql = "INSERT INTO categories VALUES(";
				sql += category.getId();
				sql += ',';
				sql += '\'' + category.getDescription() + '\'' + ',';
				sql += '\'' + category.getPrivURL() + '\'' + ',';
				sql += '\'' + category.getRootURL() + '\'' + ',';
				sql += '\'' + category.getSubcatString() + '\'' + ')';
				stmt.executeUpdate(sql);
			}

			// STEP 6: Clean-up environment
			stmt.close();
			conn.close();

			System.out.println("Succesfully finished querries");

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets all categories saved in the database
	 * @deprecated
	 * @return
	 */
	@Deprecated
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			
			String sql = ("SELECT * FROM categories;");
			ResultSet rs = stmt.executeQuery(sql);
			
			// STEP 5: Grabbing items
			int id;
			String descr;
			String url;
			String rooturl;
			String subcats;
			
			
			while(rs.next()) {
				id = rs.getInt("id");
				descr = rs.getString("description");
				url = rs.getString("url");
				rooturl = rs.getString("rooturl");
				subcats = rs.getString("subcategories");
				
				if (subcats != "" && subcats != null) {
					categories.add(new Category (id,descr,url,rooturl, subcats));
				} else {
					categories.add(new Category (id,descr,url,rooturl));
				}
			}

			System.out.println("Succesfully finished querries");

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}	
		return categories;
	}

	/**
	 * Inserts a map of items ( by index and value ) to the database.
	 * @param crawledItems
	 * @return
	 */
	public Map<Integer, Offer> insertItems(Map<Integer, List<Item>> crawledItems) {
		Connection conn = null;
		Statement stmt = null;
		Map<Integer, Offer> offers = new HashMap<Integer,Offer>();
		List<Integer> itemsFound = new ArrayList<Integer>();
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;

			// Extract all items from the database
			sql = ("SELECT * FROM items;");
			ResultSet rs = stmt.executeQuery(sql);
			
			// STEP 5: Grabbing items
			int id;
			String title;
			double price;
			String url;
			int categoryId;
			double discount;
			
			while(rs.next()) {
				// Grab item's data
				id = rs.getInt("id");
				categoryId = rs.getInt("category");
				title = rs.getString("title");
				price = rs.getDouble("price");
				url = rs.getString("url");
				discount = rs.getDouble("discount");
				
				// Grab the item to compare to
				Item toCompare = null;
				List<Item> toCompareList = crawledItems.get(categoryId);
				if (toCompareList != null) {
					for (Item currentItem : toCompareList) {
						if (currentItem.getId() == id) {
							toCompare = currentItem;
						}
					}
				}
					
				// We found a match
				if (toCompare != null) {
					// Check if price changed
					if (Math.abs((price - discount) - toCompare.getPrice()) > 0.01) {
						// There's a difference in price
						// Calculate the new discount
						discount += ((price - discount)-toCompare.getPrice());
						
						Offer offer = new Offer(title, toCompare.getCategoryId(), toCompare.getId(), price, discount, toCompare.getBaseURL(), url);
						
						// Add our new offer
						offers.put(offer.getId(), offer);
					}

					// Remember we found this item
					itemsFound.add(toCompare.getId());
				}
			}
			
			// Add new items to the database
			for (Entry<Integer, List<Item>> entry : crawledItems.entrySet()) {
				List<Item> items = entry.getValue();
				for (Item item : items) {
					
					if (!itemsFound.contains(item.getId())) {
						sql = "INSERT INTO items VALUES(";
						sql += item.getIdString();
						sql += ',';
						sql += item.getCategoryString();
						sql += ',';
						sql += '\'' + item.getTitle() + '\'' + ',';
						sql += '\'' + item.getPriceString() + '\'';
						sql += ',';
						sql += '\'' + item.getURL() + '\'' + ",0)";
						System.out.println(sql);
						
						try {
							stmt.executeUpdate(sql);
						} catch (MySQLIntegrityConstraintViolationException e) {
							// Found duplicate in website, do nothing
						}
					}
				}	
			}
			
			// Update previous items in the database
			for (Entry<Integer, Offer> entry : offers.entrySet()) {
				Offer offer = entry.getValue();
				sql = "UPDATE items SET price=";
				sql += offer.getPriceString();
				sql += ",discount=";
				sql += offer.getDiscountString();
				sql += " WHERE id=";
				sql += offer.getIdString();
				
				System.out.println(sql);
				stmt.executeUpdate(sql);
				
				// Also update our own entries
				Resources.offers.put(offer.getId(), offer);
			}

			// STEP 6: Clean-up environment
			stmt.close();
			conn.close();

			System.out.println("Succesfully finished querries");

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return offers;
	}
}
