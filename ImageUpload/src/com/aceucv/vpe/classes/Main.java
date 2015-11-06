package com.aceucv.vpe.classes;

import javax.swing.*;

/**
 * Created by cristiantotolin on 06/11/15.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new MainFrame("Hello");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
