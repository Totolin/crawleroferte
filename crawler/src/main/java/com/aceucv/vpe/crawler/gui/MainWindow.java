package com.aceucv.vpe.crawler.gui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {

    private JPanel panel1;
    private JTabbedPane tabs;
    private JButton runButton;
    private JFrame frame = new JFrame();
    private String[] columnNames = {"Nama", "Nim", "IP", "Hapus Baris ke"};
    private Object[][] data = {
        {"igor", "B01_125-358", "1.124.01.125", "true"},
        {"lenka", "B21_002-242", "21.124.01.002", "true"},
        {"peter", "B99_001-358", "99.124.01.001", "false"},
        {"zuza", "B12_100-242", "12.124.01.100", "true"},
        {"jozo", "BUS_011-358", "99.124.01.011", "false"},
        {"nora", "B09_154-358", "9.124.01.154", "false"},
        {"xantipa", "B01_001-358", "1.124.01.001", "false"},};
    private DefaultTableModel model = new DefaultTableModel(data, columnNames) {
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

    public MainWindow() {
        tabs = new JTabbedPane();
        panel1 = new JPanel();
        runButton = new JButton("go!");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                List<String[]> data = new LinkedList<String[]>();
                for (int i = 1; i < 10; i++) {
                    data.add(new String[]{"entry1", "value1", "value2", "value3"});
                }
                for (String[] datarow : data) {
                    model.addRow(Arrays.copyOf(datarow, datarow.length, Object[].class));
                }
            }
        });
        panel1.add(runButton);
        tabs.addTab("first tab", panel1);
        JTable table = new JTable(model);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("A button"));
        buttonPanel.add(new JLabel("Some description for the awesome table below "));
        buttonPanel.add(new JButton("another button"));
        JScrollPane tablePanel = new JScrollPane(table);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(tablePanel, BorderLayout.CENTER);
        tabs.addTab("Preview", container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tabs);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow frame = new MainWindow();

            }
        });
    }
}