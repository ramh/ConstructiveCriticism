package edu.gatech.nl10.constructivecriticism.models;

public class Comment {
	// Features
	public String worth; //Value of the comment (valuable, useless, etc..)

	// Attributes
	public String text;

	public Comment(String text) {
		super();
		this.text = text;
		this.worth = null;
	}
	
	@Override
	public String toString() {
		return "Comment <worth='" + worth + "', text='" + text + "'>";
	}
}
