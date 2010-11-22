package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class CommentLengthFE implements FeatureExtractor{

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();	
		features.add((double)c.text.length());
	
		return features;
	}
	

}
