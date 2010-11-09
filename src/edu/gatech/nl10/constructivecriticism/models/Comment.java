package edu.gatech.nl10.constructivecriticism.models;

public class Comment {
	// Features
	public String value; //Value of the comment (valuable, useless, etc..)

	// Attributes
	public String text;

	public Comment(String value, String text) {
		super();
		this.value = value;
		this.text = text;
	}
}
