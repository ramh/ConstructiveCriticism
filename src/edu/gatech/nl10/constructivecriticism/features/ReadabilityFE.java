package edu.gatech.nl10.constructivecriticism.features;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.northwestern.at.utils.corpuslinguistics.syllablecounter.EnglishSyllableCounter;

public class ReadabilityFE implements FeatureExtractor{
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("sentence_count");
		fnames.add("avg_sentence_len");
		fnames.add("avg_syllables_per_word");
		fnames.add("flesch_kinkaid_ease");
		fnames.add("gunning_fog_index");
		return fnames;
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();
		String[] sentences = c.text.split("[.?!]");
		int num_sent = 0, num_t_words = 0, num_sylls = 0, num_c_words = 0;
		EnglishSyllableCounter esc = new EnglishSyllableCounter();
		for(String sent : sentences) {
			sent = sent.trim();
			if(sent.length() > 0) {
				StringTokenizer st;
				String word;
				int num_words = 0;
				
				st = new StringTokenizer(sent);
				while(st.hasMoreElements()) {
					word = ((String) st.nextElement()).trim().toLowerCase();
					if(Pattern.matches(".*\\w.*", word)) {
						int num_punct = 0;
						for(int i=0; i < word.length(); i++) {
							if(word.substring(i, i+1).matches("\\p{Punct}")) {
								num_punct++;
							}
						}
						// not emoticon
						if(! (((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7)) {
							num_words++;
							int sylls = esc.countSyllables(word);
							num_sylls += sylls;
							if(sylls >= 3)
								num_c_words++;
						}
					}
				}
				if(num_words > 0) {
					num_sent++;
					num_t_words += num_words;
				}
			}
		}
		double avg_sent_len = ((double) num_t_words) / num_sent;
		double avg_syll = ((double) num_sylls) / num_t_words;
		double percent_complex = ((double)num_c_words) / num_t_words;
		double flesch = 206.835 - 1.015 * avg_sent_len - 84.6 * avg_syll;
		double gunning = 0.4 * (avg_sent_len + 100 * percent_complex);
		
		features.add((double) num_sent);
		features.add(avg_sent_len);
		features.add(avg_syll);
		features.add(flesch);
		features.add(gunning);
		
	
		return features;
	}
	

}
