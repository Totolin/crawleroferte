package com.aceucv.vpe.crawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Item;

public class DatabaseManager {

	public void insertIntoDatabase(Item item) {

	}

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/crawler";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";

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

	public void insertItems(Map<Integer, List<Item>> crawledItems) {
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
			for (Entry<Integer, List<Item>> entry : crawledItems.entrySet()) {
				List<Item> items = entry.getValue();
				for (Item item : items) {
					sql = "INSERT INTO items VALUES(";
					sql += item.getId();
					sql += ',';
					sql += '\'' + item.getTitle() + '\'' + ',';
					sql += '\'' + item.getPrice() + '\'';
					sql += ',';
					sql += '\'' + item.getURL() + '\'' + ')';
					System.out.println(sql);
					stmt.executeUpdate(sql);
				}	
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
}
