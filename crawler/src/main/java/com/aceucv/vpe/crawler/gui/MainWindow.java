package com.aceucv.vpe.crawler.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.aceucv.vpe.crawler.entities.Offer;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabs;

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

	private DefaultTableModel model = new DefaultTableModel(data, columnNamesItems) {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int row, int column) {
			switch (column) {
			case 3:
				return true;
			default:
				return false;
			}
		}

		@Override
		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}
	};

	private void addItemToList(Offer newOffer) {
		String[] data = { newOffer.getName(), newOffer.getCategory(), newOffer.getPrice(), newOffer.getDiscount(),
				newOffer.getURL() };

		model.addRow(data);
	}

	public MainWindow() {
		// Current tabbed view
		tabs = new JTabbedPane();

		JTable table = new JTable(model);
		JTable table2 = new JTable(model);

		JPanel buttonPanel = new JPanel();
		JPanel buttonPanel2 = new JPanel();

		buttonPanel.add(new JButton("A button"));
		buttonPanel.add(new JLabel("Some description for the awesome table below "));
		buttonPanel.add(new JButton("another button"));

		buttonPanel2.add(new JButton("A button"));
		buttonPanel2.add(new JLabel("Some description for the awesome table below "));
		buttonPanel2.add(new JButton("another button"));

		JScrollPane tablePanel = new JScrollPane(table);
		JScrollPane tablePanel2 = new JScrollPane(table2);

		JPanel container = new JPanel();
		JPanel container2 = new JPanel();

		container.setLayout(new BorderLayout());
		container.add(buttonPanel, BorderLayout.NORTH);
		container.add(tablePanel, BorderLayout.CENTER);

		container2.setLayout(new BorderLayout());
		container2.add(buttonPanel2, BorderLayout.NORTH);
		container2.add(tablePanel2, BorderLayout.CENTER);

		tabs.addTab("Items", container);
		tabs.addTab("Settings", container2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(tabs);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}