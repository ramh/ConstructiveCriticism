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
import edu.gatech.nl10.constructivecriticism.models.Post;
import edu.gatech.nl10.constructivecriticism.parsers.XMLParser;
import edu.stanford.nlp.io.EncodingPrintWriter.out;

public class FrequentWordIndexFE implements FeatureExtractor{
	final  static int MAX_FREQ_WORDS = 500;
	int num_freq_words;
	private Hashtable<String, Integer> freqwords;
	private ArrayList<String> fnames;

	public ArrayList<String> featureNames() {

		fnames = new ArrayList<String>();
		fnames.add("freq_word_index");
		return fnames;
	}

	public FrequentWordIndexFE() {
		freqwords = new Hashtable<String, Integer>();
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("data/freqwords.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			num_freq_words = 0;
			String nextline = in.readLine();
			while(nextline != null) {
				String word = nextline.split(" ")[0].trim();
				int count = Integer.parseInt(nextline.split(" ")[1]);
				freqwords.put(word,count);
				num_freq_words += count;
				nextline = in.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void findFrequentWords(ArrayList<Comment> comments) {
		HashSet<String> stopwords = new HashSet<String>();
		System.out.println("num" + comments.size());
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
		int n = 0;
		for(Comment c : comments) {
			System.out.println("c " + n);
			n ++;
			StringTokenizer st = new StringTokenizer(c.text);
			String word;
			while(st.hasMoreElements()) {
				word = ((String) st.nextElement()).trim().toLowerCase();
				word = word.replaceAll("[^A-Za-z]", "");;
				if(Pattern.matches(".*\\w.*", word) && !stopwords.contains(word)) {
					int num_punct = 0;
					for(int i=0; i < word.length(); i++) {
						if(word.substring(i, i+1).matches("\\p{Punct}")) {
							num_punct++;
						}
					}
					// not emoticon
					if(! (((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7)) {
						if(!wcounts.containsKey(word)) {
							wcounts.put(word, 1);
						}
						else {
							int new_c = wcounts.get(word) + 1;
							wcounts.remove(word);
							wcounts.put(word, new_c);
						}
					}
				}
			}
			//System.out.println("wcsize" + wcounts.size());
		}
		ArrayList<Entry<String, Integer>> fwlist = getFreqWords(wcounts);
		System.out.println("fwsize" + fwlist.size());
		try {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("data/freqwords.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			for(Entry<String, Integer> fw : fwlist) {
				System.out.println(fw.getKey() + " " + fw.getValue());
				bw.write(fw.getKey() + " " + fw.getValue());
				bw.newLine();
			}
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static ArrayList<Entry<String, Integer> > getFreqWords(Map<String, Integer> map) {
	     LinkedList<Entry<String, Integer> > list = new LinkedList<Entry<String, Integer> >(map.entrySet());
	     Collections.sort(list, new Comparator<Entry<String, Integer> >() {
	          public int compare(Entry<String, Integer> o1, 
	        		  Entry<String, Integer> o2) {
	               return (o2.getValue() - o1.getValue());
	          }
	     });
	     ArrayList<Entry<String, Integer> > freqwords = new ArrayList<Entry<String, Integer> >();
	     int num_add = 0;
		for (Iterator<Entry<String, Integer> > it = list.iterator(); it.hasNext();) {
			Entry<String, Integer> entry = (Entry<String, Integer>)it.next();
			freqwords.add(entry);
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
				word = word.trim().toLowerCase();
				int num_punct = 0;
				for(int i=0; i < word.length(); i++) {
					if(word.substring(i, i+1).matches("\\p{Punct}")) {
						num_punct++;
					}
				}
				// not emoticon
				if(! (((double) num_punct)/word.length() > 0.499 && word.length() > 1 && word.length() < 7)) {
					num_words++;
					Integer count = freqwords.get(word);
					if(count != null) {
						num_freq_match+=count;
					}
				}
			}
		}
		double cossim = ((double) num_freq_match) / Math.sqrt(num_words * num_freq_words);
		features.add(cossim);
	
		return features;
	}
	
	public static void main(String[] args) {
		ArrayList<Comment> all_comments = new ArrayList<Comment>();
		ArrayList<Post> posts = XMLParser.parse("");
		int i = 0;
		for (Post post : posts) {
			all_comments.addAll(post.comments);
		}
		FrequentWordIndexFE.findFrequentWords(all_comments);
	}
}
