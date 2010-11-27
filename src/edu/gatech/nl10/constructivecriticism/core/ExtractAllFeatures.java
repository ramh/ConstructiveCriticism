package edu.gatech.nl10.constructivecriticism.core;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.features.AverageSyllableCountFE;
import edu.gatech.nl10.constructivecriticism.features.EmoticonPunctCountFE;
import edu.gatech.nl10.constructivecriticism.features.PostCommentSimilarityFE;
import edu.gatech.nl10.constructivecriticism.features.ResponseCountFE;
import edu.gatech.nl10.constructivecriticism.features.WordCountFE;
import edu.gatech.nl10.constructivecriticism.features.FeatureExtractor;
import edu.gatech.nl10.constructivecriticism.features.CharLengthFE;
import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;
import edu.gatech.nl10.constructivecriticism.parsers.XMLParser;

public class ExtractAllFeatures {
	private ArrayList<FeatureExtractor> fes;
	
	public ExtractAllFeatures() {
		// Make a list of all features needed
		fes = new ArrayList<FeatureExtractor>();
		fes.add(new CharLengthFE());
		fes.add(new WordCountFE());
		fes.add(new AverageSyllableCountFE());
		fes.add(new EmoticonPunctCountFE());
		fes.add(new ResponseCountFE());
		fes.add(new PostCommentSimilarityFE());
		
	}
	
	public ArrayList<Double> extract(Comment c) {
		
		// Parse all Features
		ArrayList<Double> features = new ArrayList<Double>();
		for(FeatureExtractor fe : fes) {
			features.addAll(fe.extractFeatures(c));
		}
		return features;
	}
	
	public static void main(String[] args) {
		//ArrayList<Post> posts = XMLParser.parse("");
		//Comment c = posts.indexAt(1).comments.indexAt(0);
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Post p = new Post("p1", "AMD's Athlon is the best processor line", "While Intel's core 2 duo line has had lots of success, the athlon processors were the best technology improvement of their time! :D", comments);
		Comment c = new Comment("The AMD's Athlon processor sucks!!! :P :3 ;-D", p, 4);
		comments.add(c);
		ExtractAllFeatures eaf = new ExtractAllFeatures();
		System.out.println(eaf.extract(c));
	}
}
