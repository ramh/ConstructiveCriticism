package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class WorthFE implements FeatureExtractor{
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("worth");
		return fnames;
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();	
//		features.add(c.worth);
	
		return features;
	}
	

}
