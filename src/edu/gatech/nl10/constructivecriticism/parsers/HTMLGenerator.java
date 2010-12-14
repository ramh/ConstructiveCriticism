package edu.gatech.nl10.constructivecriticism.parsers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import weka.core.Instances;

import edu.gatech.nl10.constructivecriticism.core.ExtractAllFeatures;
import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;

public class HTMLGenerator {
	private static void makeARFF() {
		String datafile = "scripts/article.xml";

		ArrayList<Comment> all_comments = new ArrayList<Comment>();
		ArrayList<Post> posts = XMLParser.parse(datafile);
		for (Post post : posts)
			all_comments.addAll(post.comments);
		
		String[] worths = new String[all_comments.size()];
		int i = 0;
		for (Comment c: all_comments)
			worths[i++] = "NotWorthy";
		
		ExtractAllFeatures eaf = new ExtractAllFeatures();	
		eaf.processComments(all_comments, worths, "scripts/article.arff");

	}
	
	public static void write(String url, ArrayList<Comment> comments) {
		File file;
		FileOutputStream fos;
	    DataOutputStream dos;
		try {
			String s;
			String[] fileNameStrings = url.split("\\.html")[0].split("\\/");
			String filename = "html/" + fileNameStrings[fileNameStrings.length-1] + ".html";
			System.out.println("Filename: " + filename);
		    file= new File(filename);
		    fos = new FileOutputStream(file);
		    dos = new DataOutputStream(fos);

			BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			while ((s = r.readLine()) != null) {
				for (Comment c: comments) {
					if(s.contains(c.text)) {
						String[] splits = s.split(c.text);
						s = splits[0] + splits[1];
						s.replaceAll(c.text, "");
					}
					dos.writeUTF(s);
				}		
			}
			fos.close();
			dos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		HTMLGenerator.makeARFF();

//		HTMLGenerator.write(url, comments);
	}
}
