package br.com.jm.musiclib.model;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Comment implements Comparable<Comment> {

	private Date postDate;
	private double grade;
	private String message;
	private String userName;
	
	public Comment(Date date, double grade, String message, String userName) {
		this.postDate = date;
		this.grade = grade;
		this.message = message;
		this.userName = userName;
	}
	
	public BasicDBObject toDBObject() {
		BasicDBObject doc = new BasicDBObject();
		
		doc.put("postDate", postDate);
		doc.put("grade", grade);
		doc.put("message", message);
		doc.put("userName", userName);
		
		return doc;
	}
	
	public static Comment getComment(DBObject doc) {

		Comment comment = new Comment(
				(Date) doc.get("postDate"),
				(Double) doc.get("grade"),
				(String) doc.get("message"),
				(String) doc.get("userName"));		
		
		return comment;		
	}
	
	
	public Date getPostDate() {
		return postDate;
	}

	public double getGrade() {
		return grade;
	}

	public String getMessage() {
		return message;
	}

	public String getUserName() {
		return userName;
	}
	
	@Override
	public int compareTo(Comment obj) {
		return this.postDate.compareTo(obj.postDate);
	}


	
}
