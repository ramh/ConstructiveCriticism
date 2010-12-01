package edu.gatech.nl10.constructivecriticism.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import edu.gatech.nl10.constructivecriticism.features.FrequentWordIndexFE;
import edu.gatech.nl10.constructivecriticism.features.ReadabilityFE;
import edu.gatech.nl10.constructivecriticism.features.AverageSyllableCountFE;
import edu.gatech.nl10.constructivecriticism.features.EmoticonPunctCountFE;
import edu.gatech.nl10.constructivecriticism.features.PostCommentSimilarityFE;
import edu.gatech.nl10.constructivecriticism.features.ResponseCountFE;
import edu.gatech.nl10.constructivecriticism.features.WordCountFE;
import edu.gatech.nl10.constructivecriticism.features.FeatureExtractor;
import edu.gatech.nl10.constructivecriticism.features.CharLengthFE;
import edu.gatech.nl10.constructivecriticism.features.WorthFE;
import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;
import edu.gatech.nl10.constructivecriticism.parsers.XMLParser;

public class ExtractAllFeatures {
	private ArrayList<FeatureExtractor> fes;
	
	public ExtractAllFeatures() {
		// Make a list of all features needed
		fes = new ArrayList<FeatureExtractor>();
//		fes.add(new WorthFE()); // must be first
		
		fes.add(new CharLengthFE());
		fes.add(new WordCountFE());
		//fes.add(new AverageSyllableCountFE());
		fes.add(new EmoticonPunctCountFE());
		fes.add(new ResponseCountFE());
		fes.add(new PostCommentSimilarityFE());
		fes.add(new ReadabilityFE());
		//fes.add(new FrequentWordIndexFE());
		
	}
	
	public Instance extract(Comment c) {
		
		// Parse all Features
		ArrayList<Double> features = new ArrayList<Double>();
		ArrayList<String> fnames = new ArrayList<String>();
		for(FeatureExtractor fe : fes) {
			features.addAll(fe.extractFeatures(c));
			fnames.addAll(fe.featureNames());
		}
		if(features.size() != fnames.size())
			System.out.println("Features not all named.");
		
		double[] fvals = new double[features.size()];
		for(int i=0; i < features.size(); i++) {
			fvals[i] = features.get(i);
		}
		Instance inst = new Instance(1.0, fvals);
		return inst;
	}
	
	public ArrayList<String> getFeatureNames() {

		ArrayList<String> fnames = new ArrayList<String>();
		for(FeatureExtractor fe : fes) {
			fnames.addAll(fe.featureNames());
		}
		return fnames;
	}
	
	public Instances processComments(ArrayList<Comment> comments) {
		FastVector attr_names = new FastVector();
		
		//Adding worth nominal feature
		FastVector worthvals = new FastVector();
		worthvals.addElement("Worthy"); worthvals.addElement("NotWorthy");
		Attribute sentclass = new Attribute("Worth", worthvals);
		attr_names.addElement(sentclass);
		
		for(String name : getFeatureNames()) {
			attr_names.addElement(new Attribute(name));
		}
		
		Instances insts = new Instances("processed_comments", attr_names, comments.size());
		insts.setClassIndex(0);
		
		for(Comment c : comments) {
			Instance inst = extract(c);
			inst.setValue(0, c.worth);
			insts.add(inst);
		}		
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(insts);
		try {
			saver.setFile(new File("output/extracted_features.arff"));
			saver.writeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return insts;
	}
	
	public static void main(String[] args) {
		//ArrayList<Post> posts = XMLParser.parse("");
		//Comment c = posts.indexAt(1).comments.indexAt(0);
//		ArrayList<Comment> comments = new ArrayList<Comment>();
//		Post p = new Post("p1", "AMD's Athlon is the best processor line", "While Intel's core 2 duo line has had lots of success, the athlon processors were the best technology improvement of their time! :D", comments);
//		Comment c = new Comment("The AMD's Athlon processor sucks!!! :P :3 ;-D", p, 4);
//		comments.add(c);
		
		ArrayList<Comment> all_comments = new ArrayList<Comment>();
		ArrayList<Post> posts = XMLParser.parse("data/posts.xml");
		for (Post post : posts)
			all_comments.addAll(post.comments);
		
		ExtractAllFeatures eaf = new ExtractAllFeatures();
		System.out.println(eaf.processComments(all_comments));
		System.out.println(eaf.getFeatureNames().toString());
		

	}
}
