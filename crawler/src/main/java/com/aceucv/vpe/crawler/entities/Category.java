package com.aceucv.vpe.crawler.entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctotolin on 22-Nov-15.
 */
public class Category {

    private int id;
    private String description;
    private String privURL;
    private String rootURL;
    private List<String> subcategories;

    public Category(int id, String description, String URL, String rootURL) {
        this.id = id;
        this.description = description;
        this.privURL = URL;
        this.rootURL = rootURL;
        this.subcategories = new ArrayList<String>();
    }

    public String getRootURL() {
        return rootURL;
    }

    public void setRootURL(String rootURL) {
        this.rootURL = rootURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivURL() {
        return privURL;
    }

    public void setPrivURL(String privURL) {
        this.privURL = privURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSubs (){
        return subcategories;
    }

    public static int parseID (Element elem) {
        String id = elem.id();
        if (!id.contains("c")) return 0;
        String parts[] = id.split("c");
        if (parts.length<=0) return 0;
        return Integer.parseInt(parts[1]);
    }

    public static String parseDescription (Element elem) {
        return elem.ownText();
    }

    public static String parseURL (Element elem) {
        return elem.getElementsByAttribute("href").attr("href");
    }

    public void processItems() {
        try {
            Document doc = Jsoup.connect(rootURL + privURL).get();
            for (Element elem : doc.select("a[href]")) {
                String href = elem.getElementsByAttribute("href").attr("href");
                if (href.length() > 1 &&
                        href.charAt(href.length()-1) == 'c' &&
                        !href.contains("http")) {
                    subcategories.add(href.substring(0, href.length()-1));
                }
            }
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
