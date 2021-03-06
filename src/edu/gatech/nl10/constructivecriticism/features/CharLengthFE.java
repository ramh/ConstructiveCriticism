package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class CharLengthFE implements FeatureExtractor{
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("character_count");
		return fnames;
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();	
		features.add((double)c.text.length());
	
		return features;
	}
	

}
