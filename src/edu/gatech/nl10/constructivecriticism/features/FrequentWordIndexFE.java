package edu.gatech.nl10.constructivecriticism.features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class FrequentWordIndexFE implements FeatureExtractor{
	int MAX_FREQ_WORDS = 100;
	int num_freq_words;
	private HashSet<String> freqwords;
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("freq_word_index");
		return fnames;
	}

	public FrequentWordIndexFE() {
		freqwords = new HashSet<String>();
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("data/freqwords.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			String nextline = in.readLine();
			while(nextline != null) {
				freqwords.add(nextline.trim());
				nextline = in.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void findFrequentWords(ArrayList<Comment> comments) {
		HashSet<String> stopwords = new HashSet<String>();
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
		Hashtable<String, Integer> wcounts = new Hashtable<String, Integer>();
		for(Comment c : comments) {
			StringTokenizer st = new StringTokenizer(c.text);
			String word;
			while(st.hasMoreElements()) {
				word = ((String) st.nextElement()).trim().toLowerCase();
				if(Pattern.matches(".*\\w.*", word) && !stopwords.contains(word)) {
					int num_punct = 0;
					for(int i=0; i < word.length(); i++) {
						if(word.substring(i, i+1).matches("\\p{Punct}")) {
							num_punct++;
						}
					}
					// not emoticon
					if(! (((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7)) {
						if(!wcounts.contains(word)) {
							wcounts.put(word, 1);
						}
						else {
							int new_c = wcounts.get(word) + 1;
							wcounts.put(word, new_c);
						}
					}
				}
			}
		}
		ArrayList<String> fwlist = getFreqWords(wcounts);
		num_freq_words = fwlist.size();
		try {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter("data/freqwords.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			for(String fw : fwlist) {
				out.write(fw);
				out.newLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<String> getFreqWords(Map<String, Integer> map) {
	     LinkedList<Entry<String, Integer> > list = new LinkedList<Entry<String, Integer> >(map.entrySet());
	     Collections.sort(list, new Comparator<Entry<String, Integer> >() {
	          public int compare(Entry<String, Integer> o1, 
	        		  Entry<String, Integer> o2) {
	               return ((Comparable<Integer>) ((Entry<String, Integer>) (o2)).getValue()).compareTo(((Entry<String, Integer>) (o1)).getValue());
	          }
	     });
	     ArrayList<String> freqwords = new ArrayList<String>();
	     int num_add = 0;
		for (Iterator<Entry<String, Integer> > it = list.iterator(); it.hasNext();) {
			Entry<String, Integer> entry = (Entry<String, Integer>)it.next();
			freqwords.add(entry.getKey());
			num_add++;
			if(num_add == MAX_FREQ_WORDS)
				break;
		}
		return freqwords;
	}

	
	public ArrayList<Double> extractFeatures(Comment c) {
		ArrayList<Double> features = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(c.text);
		String word;
		int num_words = 0, num_freq_match = 0;
		while(st.hasMoreElements()) {
			word = (String) st.nextElement();
			if(Pattern.matches(".*\\w.*", word)) {
				word = ((String) st.nextElement()).trim().toLowerCase();
				int num_punct = 0;
				for(int i=0; i < word.length(); i++) {
					if(word.substring(i, i+1).matches("\\p{Punct}")) {
						num_punct++;
					}
				}
				// not emoticon
				if(! (((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7)) {
					num_words++;
					if(freqwords.contains(word)) {
						num_freq_match++;
					}
				}
			}
		}
		double cossim = ((double) num_freq_match) / Math.sqrt(num_words * num_freq_words);
		features.add(cossim);
	
		return features;
	}
	

}
