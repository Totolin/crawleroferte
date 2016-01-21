package com.aceucv.vpe.crawler.source;

import java.io.IOException;

import com.aceucv.vpe.crawler.database.DatabaseManager;
import com.aceucv.vpe.crawler.engine.Crawler;
import com.aceucv.vpe.crawler.gui.LoadingScreen;
import com.aceucv.vpe.crawler.gui.MainWindow;
import com.aceucv.vpe.crawler.model.XMLParser;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		MainWindow window = new MainWindow();
		Crawler crawler = new Crawler();
		DatabaseManager mgr = new DatabaseManager();
		LoadingScreen loadScreen = new LoadingScreen();
		XMLParser parser = new XMLParser();
		
		Controller controller = new Controller(window, crawler, mgr, loadScreen, parser);
		controller.start();
	}
}
