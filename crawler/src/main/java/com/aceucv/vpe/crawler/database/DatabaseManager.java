package com.aceucv.vpe.crawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

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

	public void insetCategories(Map<Integer, Category> categories) {

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
			for (Map.Entry<Integer, Category> category : categories.entrySet()) {
				sql = "INSERT INTO categories VALUES(";
				sql += category.getKey();
				sql += ',';
				sql += '\'' + category.getValue().getDescription() + '\'' + ',';
				sql += '\'' + category.getValue().getPrivURL() + '\'' + ',';
				sql += '\'' + category.getValue().getRootURL() + '\'' + ',';
				sql += '\'' + category.getValue().getSubcatString() + '\'' + ')';
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
}
