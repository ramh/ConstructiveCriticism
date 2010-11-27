package edu.gatech.nl10.constructivecriticism.models;

public class Comment {
	// Features
	public String worth; //Value of the comment (valuable, useless, etc..)

	// Attributes
	public String text;
	
	public int responses;
	
	public Post post;

	public Comment(String text) {
		super();
		this.text = text;
		this.worth = null;
		this.responses = 0;
		this.post = null;
	}
	
	public Comment(String text, Post post, int responses) {
		super();
		this.text = text;
		this.worth = null;
		this.responses = responses;
		this.post = post;
	}	
	
	@Override
	public String toString() {
		return "Comment <worth='" + worth + "', text='" + text + "'>";
	}
}
