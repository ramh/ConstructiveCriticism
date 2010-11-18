package edu.gatech.nl10.constructivecriticism.models;

import java.util.ArrayList;

public class Post {
	public String id;
	public String title;
	public String body;
	public ArrayList<Comment> comments;
	
	public Post(String id, String title, String body, ArrayList<Comment> comments) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		int num_comments = 0;
		if(comments != null)
			num_comments = comments.size();
		return "Post <id='" + id + "', title='" + title + "', num_comments='" + num_comments + "'>";
	}
}
