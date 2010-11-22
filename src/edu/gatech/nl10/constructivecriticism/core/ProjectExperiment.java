package edu.gatech.nl10.constructivecriticism.core;

import java.util.ArrayList;
import java.util.Iterator;

import edu.gatech.nl10.constructivecriticism.models.Post;
import edu.gatech.nl10.constructivecriticism.parsers.XMLParser;

public class ProjectExperiment {
	public static void main(String[] args) {
		// Parsing
		ArrayList<Post> posts = XMLParser.parse("data/posts.xml");
		System.out.println("Total Posts Parsed: " + posts.size());
		
		//Printing
		Iterator<Post> post_it = posts.iterator();
		while(post_it.hasNext())
			System.out.println(post_it.next());
		
		System.out.println(posts.get(1).comments.get(0));
		
		System.out.println(ExtractAllFeatures.extract(posts.get(1).comments.get(0)));
	}
}
