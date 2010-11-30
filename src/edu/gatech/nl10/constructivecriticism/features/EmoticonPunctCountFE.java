package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class EmoticonPunctCountFE implements FeatureExtractor{
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("emoticon_count");
		fnames.add("punctuation_count");
		return fnames;
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(c.text);
		String word;
		int num_emot = 0, num_t_punct = 0;
		while(st.hasMoreElements()) {
			word = (String) st.nextElement();
			if(Pattern.matches("\\p{Graph}*\\p{Punct}\\p{Graph}*", word)) {
				int num_punct = 0;
				for(int i=0; i < word.length(); i++) {
					if(word.substring(i, i+1).matches("\\p{Punct}")) {
						num_punct++;
					}
				}
				if(((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7) {
					num_emot++;
				}
				else {
					num_t_punct += num_punct;
				}
			}
		}
		features.add((double)num_emot);
		features.add((double)num_t_punct);
	
		return features;
	}	
}
