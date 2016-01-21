package com.aceucv.vpe.crawler.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
/**
 * Frame displayed intially when opening the App.
 * Resources are loaded on separate threads.
 * Must be closed after everything is loaded
 * 
 * @author cristiantotolin
 *
 */
public class LoadingScreen extends JFrame {
 
	private static final long serialVersionUID = 1L;
	private JLabel imglabel;
    private JProgressBar pbar;
    private JLabel message;
 
    private JLabel getLogo() {
		// Read the image through a buffer
		BufferedImage logoPicture = null;
		try {
			logoPicture = ImageIO.read(new File("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Resize the image to desired size
		Image dimg = logoPicture.getScaledInstance(150, 100, Image.SCALE_SMOOTH);

		// Return the label with containing picture
		JLabel picLabel = new JLabel(new ImageIcon(dimg));
		return picLabel;
	}
    
    /**
     * Sets a loading text
     * @param text
     */
    public void setLoadingText (String text) {
    	this.message.setText(text);
    }
    
    /**
     * Increments value of progress bar
     * @param value
     */
    public void incremenetLoading (int value) {
    	this.pbar.setValue(this.pbar.getValue() + value);
    }
    
    public LoadingScreen() {
        super("Crawler");
        
        // Set loading screen size and properties
        setSize(404, 310);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);
        
        // Add an image logo
        imglabel = getLogo();
        imglabel.setBounds(0, 0, 404, 310);
        add(imglabel);
        
        // Add a message box
        message = new JLabel();
        message.setBounds(100, 260, 300, 50);
        add(message);

        // Add a simple progress bar 
        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
        pbar.setStringPainted(false);
        pbar.setForeground(Color.LIGHT_GRAY);
        pbar.setPreferredSize(new Dimension(310, 30));
        pbar.setBounds(0, 290, 404, 20);
        add(pbar);  
    }
}