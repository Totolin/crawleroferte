package com.aceucv.vpe.classes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cristiantotolin on 06/11/15.
 */
public class MainFrame extends JFrame {

    private JLabel imageLabel;
    private JButton uploadButton;
    private JLabel imageDescription;
    public MainFrame (String title) {
        super(title);

        // Set layout type
        setLayout(new BorderLayout());

        // Create swing components
        imageLabel = new JLabel("No image uploaded");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        uploadButton = new JButton("Upload a picture");
        imageDescription = new JLabel("No image uploaded",SwingConstants.CENTER);
        imageDescription.setOpaque(true);
        imageDescription.setBackground(Color.white);
        imageDescription.setVisible(false);

        // Add these components to panel
        Container container = getContentPane();

        container.add(imageLabel,BorderLayout.CENTER);
        container.add(uploadButton,BorderLayout.SOUTH);
        container.add(imageDescription,BorderLayout.NORTH);


        // Add a listener to our button
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressedUploadButton(e);
            }
        });
    }

    private void pressedUploadButton(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String path = f.getAbsolutePath();
        try {
            // Get the image from file chooser and scale it to match JLabel size
            ImageIcon ii=new ImageIcon(scaleImage(400, 400, ImageIO.read(new File(f.getAbsolutePath()))));

            // Remove image label text
            imageLabel.setText(null);
            imageLabel.setIcon(ii);

            // Set top label image description text
            String text = "<html>"
                    + getHtmlLabel("verdana","red",getFilename(path))
                    + " - "
                    + getHtmlLabel("georgia", "black", getCurrentDate());

            imageDescription.setText(text);
            imageDescription.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static BufferedImage scaleImage(int w, int h, BufferedImage img) throws Exception {
        BufferedImage bi;
        bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(img, 0, 0, w, h, null);
        g2d.dispose();
        return bi;
    }

    private static String getFilename (String path) {
        String parts[] = path.split("/");
        return parts[parts.length-1];
    }

    private static String getHtmlLabel (String font, String color, String text) {
        //<font color=red font face="verdana">RED</font>
        String html = "<";
        html+="font color=" + color;
        html+=" font face=\""+font+"\">";
        html+=text;
        html+="</font>";
        return html;
    }

    private static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
}
