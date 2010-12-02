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

import edu.gatech.nl10.constructivecriticism.models.Comment;
import edu.gatech.nl10.constructivecriticism.models.Post;

public class HTMLGenerator {
//	private static String taggedhtmlfile = "data/taggedhtml.txt";
//
//	private static ArrayList<Comment> getNotWorthyComments() {
//		String datafile = "data/posts.xml";
//		ArrayList<Comment> notworthyComments = new ArrayList<Comment>();
//
//		ArrayList<Comment> all_comments = new ArrayList<Comment>();
//		ArrayList<Post> posts = XMLParser.parse(datafile);
//		for (Post post : posts)
//			all_comments.addAll(post.comments);
//
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new FileReader(taggedhtmlfile));
//			String nextline = in.readLine();
//			while(nextline!=null) {
//				if(nextline == "NotWorthy")
//					notworthyComments.add(all_comments.get(i))
//				nextline = in.readLine();
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		return null;
//	}
	
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
		String url = "http://news.cnet.com/8301-13577_3-20022753-36.html?part=rss&amp;amp;subj=news&amp;amp;tag=2547-1_3-0-20";
		Comment c = new Comment("Awesome article.");
		ArrayList<Comment> comments = new ArrayList<Comment>();
		comments.add(c);
		HTMLGenerator.write(url, comments);
	}
}
