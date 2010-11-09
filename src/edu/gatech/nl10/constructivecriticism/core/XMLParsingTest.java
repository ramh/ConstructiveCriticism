package edu.gatech.nl10.constructivecriticism.core;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Post;
import edu.gatech.nl10.constructivecriticism.parsers.XMLParser;

public class XMLParsingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Post> posts = XMLParser.parse("data/sample.xml");

	}

}
