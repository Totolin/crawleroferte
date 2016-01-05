package com.aceucv.vpe.crawler.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

	// Pane for the tabs
	private JTabbedPane tabs;
	
	// Objects in 1st tab (Offers tab)
	public JButton buttonOpenOffer;
	public JButton buttonDeleteOffer;
	public JButton buttonSelectAllOffers;
	public JButton buttonDeselectAllOffers;
	
	public JPanel  sidePanelOffers;
	public JLabel itemsLabel;
	public JLabel selectionOffersLabel;
	public JLabel logo;
	
	// Objects in 2nd tab (Settings tab)
	public JButton buttonCrawlItems;
	public JButton buttonCrawlCategories;
	public JButton buttonSelectAllCategories;
	public JButton buttonDeselectAllCategories;
	
	public JPanel  sidePanelCategories;
	
	private JPanel buttonPanelItems;
	private JPanel buttonPanelCategories;

	public JTable itemsTable;
	public JTable categoriesTable;

	
	public JLabel settingsLabel;

	private JScrollPane paneItems;
	private JScrollPane paneCategories;

	public JProgressBar progressCategories;

	private JFrame frame = new JFrame();

	private String[] columnNamesItems = { "Name", "Category", "Price (RON)", "Discount (%)", "Link" };
	private String[] columnNamesCategories = { "Name", "Select" };

	private Object[][] data = { { "igor", "B01_125-358", "1.124.01.125", "true", true },
			{ "lenka", "B21_002-242", "21.124.01.002", "true", false },
			{ "peter", "B99_001-358", "99.124.01.001", "false", true },
			{ "zuza", "B12_100-242", "12.124.01.100", "true", false },
			{ "jozo", "BUS_011-358", "99.124.01.011", "false", false },
			{ "nora", "B09_154-358", "9.124.01.154", "false", true },
			{ "xantipa", "B01_001-358", "1.124.01.001", "false", false }, };

	private Object[][] dataCategories = {};

	private DefaultTableModel modelItems = new DefaultTableModel(data, columnNamesItems) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 4:
				return Boolean.class;
			default:
				return String.class;
			}
		}
	};

	private DefaultTableModel modelSettings = new DefaultTableModel(dataCategories, columnNamesCategories) {
		private static final long serialVersionUID = 1L;

		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 0:
				return String.class;
			default:
				return Boolean.class;
			}
		}
	};

	public void addItemToList(Offer newOffer) {
		Object[] data = { newOffer.getName(), newOffer.getCategory(), newOffer.getPrice(), newOffer.getDiscount(),
				false };

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
		buttonOpenOffer = 			new JButton("    Open Offer    ");
		buttonDeleteOffer = 		new JButton("   Delete Offer   ");
		buttonSelectAllOffers = 	new JButton("    Select all    ");
		buttonDeselectAllOffers = 	new JButton("  Deselect all  ");
		itemsLabel = new JLabel(Resources.label_text_offers);
		selectionOffersLabel = new JLabel(Resources.label_text_selection_offers);
		logo = getLogo();
		
		// Set alignments
		itemsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionOffersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOpenOffer.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDeleteOffer.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonSelectAllOffers.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDeselectAllOffers.setAlignmentX(Component.CENTER_ALIGNMENT);
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add the buttons to a button panel
		buttonPanelItems = new JPanel();
		buttonPanelItems.setLayout(new BoxLayout(buttonPanelItems, BoxLayout.Y_AXIS));
		
		addPadding(buttonPanelItems, 1);
		buttonPanelItems.add(logo);
		
		addPadding(buttonPanelItems, 2);
		buttonPanelItems.add(itemsLabel);
		
		addPadding(buttonPanelItems, 1);
		buttonPanelItems.add(buttonOpenOffer);
		buttonPanelItems.add(buttonDeleteOffer);
		
		addPadding(buttonPanelItems, 3);
		buttonPanelItems.add(selectionOffersLabel);
		addPadding(buttonPanelItems, 1);
		buttonPanelItems.add(buttonSelectAllOffers);
		buttonPanelItems.add(buttonDeselectAllOffers);

		// Create a scrollable pane for the list
		paneItems = new JScrollPane(itemsTable);

		// Add items via layout
		container.setLayout(new BorderLayout());
		container.add(buttonPanelItems, BorderLayout.WEST);
		// container.add(testpanel, BorderLayout.WEST);
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

	public void addPadding (JPanel panel, int count) {
		while (count>0) {
			count--;
			panel.add(new JLabel(" "));
		}
	}
	
	private JLabel getLogo () {
		// Read the image through a buffer
		BufferedImage logoPicture = null;
		try {
			logoPicture = ImageIO.read(new File("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Resize the image to desired size
		Image dimg = logoPicture.getScaledInstance(150, 100,
	            Image.SCALE_SMOOTH);
		
		// Return the label with containing picture
		JLabel picLabel = new JLabel(new ImageIcon(dimg));
		return picLabel;
	}
	
	public MainWindow() {
		// Current tabbed view
		tabs = new JTabbedPane();

		// Create all the tabs
		tabs.addTab("Items", getItemsTab());
		tabs.addTab("Settings", getSettingsTab());

		// Set window configurations and visibility
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tabs);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}