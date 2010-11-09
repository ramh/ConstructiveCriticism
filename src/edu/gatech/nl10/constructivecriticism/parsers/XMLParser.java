package edu.gatech.nl10.constructivecriticism.parsers;

import java.io.File;
import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.gatech.nl10.constructivecriticism.models.Post;

public class XMLParser {
	public static ArrayList<Post> parse(String xmlFileName) {
		File xmlFile = new File(xmlFileName);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(xmlFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if (doc != null) {
			NodeList postElements = doc.getElementsByTagName("post");
			if (postElements != null && postElements.getLength() > 0) {
				ArrayList<Post> posts = new ArrayList<Post>();
				Node postElement;
				System.out.println(postElements.getLength());
				for (int i = 0; i < postElements.getLength(); i++) {
					// Post Elements
					postElement = postElements.item(i);
					NodeList x = postElement.getChildNodes();
					String postId = x.item(1).getTextContent();
					String postTitle = x.item(2).getTextContent();
					String postBody = x.item(3).getTextContent();
					
					//TODO: Add Comments
					posts.add(new Post(postId, postTitle, postBody, null));
				}
				return posts;
			}
		}
		return null;

	}
}
