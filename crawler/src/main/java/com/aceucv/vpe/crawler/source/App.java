package com.aceucv.vpe.crawler.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.gui.MainWindow;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		MainWindow window = new MainWindow();
		Crawler crawler = new Crawler();
		DatabaseManager mgr = new DatabaseManager();
		Controller controller = new Controller(window, crawler, mgr);
	}
}
