package edu.gatech.nl10.constructivecriticism.models;

public class Comment {
	// Features
	public String worth; //Value of the comment (valuable = 1, useless = -1, unassigned = 0)

	// Attributes
	public String text;
	
	public int responses;
	
	public Post post;

	public Comment(String text) {
		super();
		this.text = text;
		this.worth = "";
		this.responses = 0;
		this.post = null;
	}
	
	public Comment(String text, Post post, int responses) {
		super();
		this.text = text;
		this.worth = "";
		this.responses = responses;
		this.post = post;
	}	
	
	public Comment(String text, int responses) {
		super();
		this.text = text;
		this.worth = "";
		this.responses = responses;
	}	
	
	@Override
	public String toString() {
		return "Comment <worth='" + worth + "', text='" + text + "'>";
	}
}
