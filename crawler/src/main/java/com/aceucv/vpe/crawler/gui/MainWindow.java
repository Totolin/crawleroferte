package com.aceucv.vpe.crawler.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Offer;
import com.aceucv.vpe.crawler.model.Resources;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabs;
	public JButton buttonOpenOffer;
	public JButton buttonDeleteOffer;
	public JButton buttonCrawlCategories;
	public JButton buttonSelectAllCategories;
	public JButton buttonCrawlItems;

	private JPanel buttonPanelItems;
	private JPanel buttonPanelCategories;

	private JTable itemsTable;
	private JTable categoriesTable;

	public JLabel itemsLabel;
	public JLabel settingsLabel;

	private JScrollPane paneItems;
	private JScrollPane paneCategories;

	public JProgressBar progressCategories;

	private JFrame frame = new JFrame();

	private String[] columnNamesItems = { "Name", "Category", "Price (RON)", "Discount (%)", "Link" };
	private String[] columnNamesCategories = { "Name", "Select" };

	private Object[][] data = { { "igor", "B01_125-358", "1.124.01.125", "true", "asd" },
			{ "lenka", "B21_002-242", "21.124.01.002", "true", "asd" },
			{ "peter", "B99_001-358", "99.124.01.001", "false", "asd" },
			{ "zuza", "B12_100-242", "12.124.01.100", "true", "asd" },
			{ "jozo", "BUS_011-358", "99.124.01.011", "false", "asd" },
			{ "nora", "B09_154-358", "9.124.01.154", "false", "asd" },
			{ "xantipa", "B01_001-358", "1.124.01.001", "false", "asd" }, };

	private Object[][] dataCategories = {};

	private DefaultTableModel modelItems = new DefaultTableModel(data, columnNamesItems) {
		private static final long serialVersionUID = 1L;
	};

	private DefaultTableModel modelSettings = new DefaultTableModel(dataCategories, columnNamesCategories) {
		private static final long serialVersionUID = 1L;

		@Override
		public Class getColumnClass(int column) {
			switch (column) {
			case 0:
				return String.class;
			default:
				return Boolean.class;
			}
		}
	};

	public void addItemToList(Offer newOffer) {
		String[] data = { newOffer.getName(), newOffer.getCategory(), newOffer.getPrice(), newOffer.getDiscount(),
				newOffer.getURL() };

		modelItems.addRow(data);
	}

	public void addCategoryToList(Category newCategory) {
		Object[] data = { newCategory.getDescription(), false };
		System.out.println("Adding new category");
		modelSettings.addRow(data);
	}

	private JPanel getItemsTab() {
		// Create the container (main)
		JPanel container = new JPanel();

		// Create the table
		itemsTable = new JTable(modelItems);

		// Create all the buttons and labels needed
		buttonOpenOffer = new JButton("Open Offer");
		buttonDeleteOffer = new JButton("Delete Offer");
		itemsLabel = new JLabel(Resources.label_text_offers);

		// Add the buttons to a button panel
		buttonPanelItems = new JPanel();
		buttonPanelItems.add(buttonOpenOffer);
		buttonPanelItems.add(itemsLabel);
		buttonPanelItems.add(buttonDeleteOffer);

		// Create a scrollable pane for the list
		paneItems = new JScrollPane(itemsTable);

		// Add items via layout
		container.setLayout(new BorderLayout());
		container.add(buttonPanelItems, BorderLayout.NORTH);
		container.add(paneItems, BorderLayout.CENTER);

		return container;
	}

	private JPanel getSettingsTab() {
		// Create the container (main)
		JPanel container = new JPanel();

		// Create the table
		categoriesTable = new JTable(modelSettings);

		// Create all the buttons needed
		buttonCrawlCategories = new JButton("Search caterogires");

		buttonSelectAllCategories = new JButton("Select all");

		buttonCrawlItems = new JButton("Crawl");

		settingsLabel = new JLabel(Resources.label_text_settings);

		// Create a progress bar for crawling progress
		progressCategories = new JProgressBar();

		// Add the buttons to a button panel
		buttonPanelCategories = new JPanel();
		buttonPanelCategories.add(buttonCrawlCategories);
		buttonPanelCategories.add(buttonCrawlItems);
		buttonPanelCategories.add(settingsLabel);
		buttonPanelCategories.add(progressCategories);
		buttonPanelCategories.add(buttonSelectAllCategories);

		// Create a scrollable pane for the list
		paneCategories = new JScrollPane(categoriesTable);

		// Add items via layout
		container.setLayout(new BorderLayout());
		container.add(buttonPanelCategories, BorderLayout.NORTH);
		container.add(paneCategories, BorderLayout.CENTER);

		return container;
	}

	public MainWindow() {
		// Current tabbed view
		tabs = new JTabbedPane();

		// Create all the tabs
		tabs.addTab("Items", getItemsTab());
		tabs.addTab("Settings", getSettingsTab());

		// Set window configurations and visibility
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(tabs);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}