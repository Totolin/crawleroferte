package com.aceucv.vpe.crawler.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import com.aceucv.vpe.crawler.entities.Category;
import com.aceucv.vpe.crawler.entities.Offer;

/**
 * Class used to parse data from/to XML files.
 * Used to store/read saved categories and offers
 * 
 * @author cristiantotolin
 *
 */
public class XMLParser {

	public XMLParser () {
		// Nothing configurable
	}
	
	/**
	 * Reads all categories from their xml file
	 * @return
	 */
	public List<Category> readFromFileCategories() {
		List<Category> categories = new ArrayList<Category>();

		try {

			// Read xml file
			File fXmlFile = new File("categories.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// The document might not exist
			Document doc;
			try {
				doc = dBuilder.parse(fXmlFile);
			} catch (SAXParseException e) {
				return categories;
			} catch (FileNotFoundException e) {
				return categories;
			}
			
			doc.getDocumentElement().normalize();

			// Root node is category
			NodeList nList = doc.getElementsByTagName("category");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				String description;
				int id;
				String url;
				String privurl;
				String subcategories;
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					description = (eElement.getElementsByTagName("name").item(0).getTextContent());
					id = (Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()));
					url = (eElement.getElementsByTagName("url").item(0).getTextContent());
					privurl = (eElement.getElementsByTagName("privurl").item(0).getTextContent());
					subcategories = (eElement.getElementsByTagName("subcategories").item(0).getTextContent());
					
					categories.add(new Category(id, description, privurl, url, subcategories));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return categories;
	}
	
	/**
	 * Reads all offers from their xml file
	 * @return
	 */
	public List<Offer> readFromFileOffers() {
		List<Offer> offers = new ArrayList<Offer>();

		try {

			// Read xml file
			File fXmlFile = new File("offers.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// The document might not exist
			Document doc;
			try {
				doc = dBuilder.parse(fXmlFile);
			} catch (SAXParseException e) {
				return offers;
			} catch (FileNotFoundException e) {
				return offers;
			}
			
			doc.getDocumentElement().normalize();

			// Root node is category
			NodeList nList = doc.getElementsByTagName("offer");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				String name;
				int id;
				String url;
				String privurl;
				double price;
				double discount;
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					name = (eElement.getElementsByTagName("name").item(0).getTextContent());
					id = (Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()));
					url = (eElement.getElementsByTagName("url").item(0).getTextContent());
					privurl = (eElement.getElementsByTagName("privurl").item(0).getTextContent());
					price = Double.parseDouble((eElement.getElementsByTagName("price").item(0).getTextContent()));
					discount = Double.parseDouble((eElement.getElementsByTagName("discount").item(0).getTextContent()));
					
					offers.add(new Offer(name,0,id,price,discount,privurl,url));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return offers;
	}

	/**
	 * Stores a list of categories in an xml file
	 * @param categories
	 */
	public void writeToFileCategories(List<Category> categories) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("categories");
			doc.appendChild(rootElement);

			for (Category category : categories) {
				Element node_category = doc.createElement("category");
				rootElement.appendChild(node_category);

				Element node_id = doc.createElement("id");
				node_id.appendChild(doc.createTextNode(category.getIdString()));
				node_category.appendChild(node_id);

				Element node_name = doc.createElement("name");
				node_name.appendChild(doc.createTextNode(category.getDescription()));
				node_category.appendChild(node_name);
				
				Element node_privurl = doc.createElement("privurl");
				node_privurl.appendChild(doc.createTextNode(category.getPrivURL()));
				node_category.appendChild(node_privurl);
				
				Element node_url = doc.createElement("url");
				node_url.appendChild(doc.createTextNode(category.getRootURL()));
				node_category.appendChild(node_url);
				
				Element node_subcategories = doc.createElement("subcategories");
				node_subcategories.appendChild(doc.createTextNode(category.getSubcatString()));
				node_category.appendChild(node_subcategories);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// Used to format nodes inside xml
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("categories.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("Saved XML categories file.");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	/**
	 * Stores all offers received as a list into an xml file
	 * @param offersList
	 */
	public void writeToFileOffers(List<Offer> offersList) {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("offers");
			doc.appendChild(rootElement);

			for (Offer offer : offersList) {
				Element node_category = doc.createElement("offer");
				rootElement.appendChild(node_category);

				Element node_id = doc.createElement("id");
				node_id.appendChild(doc.createTextNode(offer.getIdString()));
				node_category.appendChild(node_id);

				Element node_name = doc.createElement("name");
				node_name.appendChild(doc.createTextNode(offer.getName()));
				node_category.appendChild(node_name);
				
				Element node_privurl = doc.createElement("privurl");
				node_privurl.appendChild(doc.createTextNode(offer.getRootURL()));
				node_category.appendChild(node_privurl);
				
				Element node_url = doc.createElement("url");
				node_url.appendChild(doc.createTextNode(offer.getURL()));
				node_category.appendChild(node_url);
				
				Element node_price = doc.createElement("price");
				node_price.appendChild(doc.createTextNode(offer.getPriceString()));
				node_category.appendChild(node_price);
				
				Element node_discount = doc.createElement("discount");
				node_discount.appendChild(doc.createTextNode(offer.getDiscountString()));
				node_category.appendChild(node_discount);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// Used to format nodes inside xml
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("offers.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("Saved XML categories file.");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
