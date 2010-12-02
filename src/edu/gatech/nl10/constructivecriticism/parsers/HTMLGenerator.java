package edu.gatech.nl10.constructivecriticism.parsers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import edu.gatech.nl10.constructivecriticism.models.Comment;

public class HTMLGenerator {
	public static void write(String url, ArrayList<Comment> comments) {
		File file;
		FileOutputStream fos;
	    DataOutputStream dos;
		try {
			String s;
			String[] fileNameStrings = url.split("http://news.cnet.com/([^\\.]+)\\.");
			String filename = "html/" + fileNameStrings[0] + ".html";
		    file= new File(filename);
		    fos = new FileOutputStream(file);
		    dos = new DataOutputStream(fos);

			BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			while ((s = r.readLine()) != null) {
				for (Comment c: comments) {
					if(s.contains(c.text))
						System.out.println("Found Comment");
					else
					    dos.writeChars(s);
				}		
			}
			fos.close();
			dos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
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
