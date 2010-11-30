package edu.gatech.nl10.constructivecriticism.parsers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;

public class XMLParser {
	private static Document dom;
	
	private static void parseXmlFile() {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse("data/posts.xml");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static ArrayList<Post> parseDocument() {
		// get the root elememt
		org.w3c.dom.Element docEle = dom.getDocumentElement();
		ArrayList<Post> posts = new ArrayList<Post>();
		
		// get a nodelist of <post> elements
		NodeList nl = docEle.getElementsByTagName("post");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				Post p = getPost(el);
				if(p!=null) posts.add(p);
			}
		}
		return posts;
	}
	
	private static Post getPost(Element el) {
		String title = getTextValue(el,"title");
		String id = getTextAttribute(el,"id");
		String body = getTextValue(el, "body");
		
		//Parse Comments
		ArrayList<Comment> comments = new ArrayList<Comment>();
		NodeList nl = el.getElementsByTagName("comment");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element comment_el = (Element) nl.item(i);
				Comment c = getComment(comment_el);
				if(c!=null) comments.add(c);
			}
		}
		
		//Create a new Post with the value read from the xml nodes
		Post p;
		if(id!=null && title!=null && body!=null && comments!=null) {
			p = new Post(id, title, body, comments);
			for(Comment c: comments)
				c.post = p;
		}
		else 
			p = null;
		
		return p;
	}
	
	private static Comment getComment(Element commentEl) {
		String text = commentEl.getTextContent();
		int responses = 0;
		if(commentEl.hasAttribute("responses")) {
			responses = Integer.parseInt(commentEl.getAttribute("responses"));
		}
		
		Comment c;
		if(text!=null)
			c = new Comment(text, responses);
		else c= null;
		
		return c;
	}

	private static String getTextAttribute(Element el, String attr_name) {
		return el.getAttribute(attr_name).toString();
	}

	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <post><title>Earthquake in Japan</title></post> xml snippet if
	 * the Post points to post node and tagName is title I will return "Earthquake in Japan"  
	 * @param ele
	 * @param tagName
	 * @return text string for the node requested
	 */
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	
	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return integer value
	 */
	private static int getIntValue(Element ele, String tagName) {
		//TODO: catch the exception here
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	/**
	 * Main Function used to call parsing on a posts XML document
	 * @param xmlFileName
	 * @return ArrayList of Post objects
	 */
	public static ArrayList<Post> parse(String xmlFileName) {
		parseXmlFile();
		return parseDocument();
	}
}
