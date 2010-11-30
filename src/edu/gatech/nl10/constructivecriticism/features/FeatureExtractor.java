package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public interface FeatureExtractor {
	public ArrayList<Double> extractFeatures(Comment c);
	public ArrayList<String> featureNames();
}
