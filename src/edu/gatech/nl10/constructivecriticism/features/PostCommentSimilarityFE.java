package edu.gatech.nl10.constructivecriticism.features;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.HashSet;

import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;

public class PostCommentSimilarityFE implements FeatureExtractor{
	private HashSet<String> stopwords;
	
	public PostCommentSimilarityFE() {
		stopwords = new HashSet<String>();
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("data/stopwords.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			String nextline = in.readLine();
			while(nextline != null) {
				stopwords.add(nextline.trim());
				nextline = in.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();
		
		HashSet<String> postwords = new HashSet<String>();
		StringTokenizer st;
		String word;
		
		st = new StringTokenizer(c.post.title);
		while(st.hasMoreElements()) {
			word = ((String) st.nextElement()).trim();
			if(Pattern.matches(".*\\w.*", word) && !stopwords.contains(word)) {
				postwords.add(word);
			}
		}
		
		st = new StringTokenizer(c.post.body);
		while(st.hasMoreElements()) {
			word = ((String) st.nextElement()).trim();
			if(Pattern.matches(".*\\w.*", word) && !stopwords.contains(word)) {
				postwords.add(word);
			}
		}	
		
		st = new StringTokenizer(c.text);
		int num_words = 0, num_sim_words = 0;
		while(st.hasMoreElements()) {
			word = ((String) st.nextElement()).trim();
			if(Pattern.matches(".*\\w.*", word)) {
				if(postwords.contains(word)) {
					num_sim_words++;
				}
				if(!stopwords.contains(word)) {
					num_words++;
				}
			}
		}
		features.add(((double)num_sim_words) / (num_words * postwords.size()));
	
		return features;
	}	
}
