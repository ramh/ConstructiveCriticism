package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class WordCountFE implements FeatureExtractor{

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(c.text);
		String word;
		int num_words = 0;
		while(st.hasMoreElements()) {
			word = (String) st.nextElement();
			if(Pattern.matches(".*\\w.*", word)) {
				num_words++;
			}
		}
		features.add((double)num_words);
	
		return features;
	}	
}
