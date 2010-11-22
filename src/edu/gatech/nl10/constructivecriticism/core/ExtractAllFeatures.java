package edu.gatech.nl10.constructivecriticism.core;

import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.features.AverageSyllableCountFE;
import edu.gatech.nl10.constructivecriticism.features.WordCountFE;
import edu.gatech.nl10.constructivecriticism.features.FeatureExtractor;
import edu.gatech.nl10.constructivecriticism.features.CharLengthFE;
import edu.gatech.nl10.constructivecriticism.models.Comment;

public class ExtractAllFeatures {
	
	public static ArrayList<Double> extract(Comment c) {
		// Make a list of all features needed
		ArrayList<FeatureExtractor> fes = new ArrayList<FeatureExtractor>();
		fes.add(new CharLengthFE());
		fes.add(new WordCountFE());
		fes.add(new AverageSyllableCountFE());
		
		// Parse all Features
		ArrayList<Double> features = new ArrayList<Double>();
		for(FeatureExtractor fe : fes) {
			features.addAll(fe.extractFeatures(c));
		}
		return features;
	}
}
