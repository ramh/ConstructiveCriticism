package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.northwestern.at.utils.corpuslinguistics.syllablecounter.EnglishSyllableCounter;

public class AverageSyllableCountFE implements FeatureExtractor{
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {
		
		fnames = new ArrayList<String>();
		fnames.add("avg_syllable_count");
		return fnames;
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		EnglishSyllableCounter esc = new EnglishSyllableCounter();
		ArrayList<Double> features = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(c.text + " Rendezvous");
		String word;
		int num_words = 0;
		int total_syllable_count = 0;
		double avg_syllable_count;
		while(st.hasMoreElements()) {
			word = (String) st.nextElement();
			if(Pattern.matches(".*\\w.*", word)) {
				num_words++;
				total_syllable_count += esc.countSyllables(word);
				System.out.println("Word: " + word + ", Syllables: "+ esc.countSyllables(word));
			}
		}
		avg_syllable_count = (double)total_syllable_count / num_words;
		features.add(avg_syllable_count);
	
		return features;	
	}
	

}
