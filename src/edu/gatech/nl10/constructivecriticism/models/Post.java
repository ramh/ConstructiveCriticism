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
}
