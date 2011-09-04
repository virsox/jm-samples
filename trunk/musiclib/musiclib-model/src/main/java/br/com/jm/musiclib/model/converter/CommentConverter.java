package br.com.jm.musiclib.model.converter;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import br.com.jm.musiclib.model.Comment;


@ApplicationScoped
public class CommentConverter implements Converter<Comment> {

	@Override
	public DBObject toDBObject(Comment comment) {
		BasicDBObject doc = new BasicDBObject();
		
		doc.put("postDate", comment.getPostDate());
		doc.put("grade", comment.getGrade());
		doc.put("message", comment.getMessage());
		doc.put("userName", comment.getUserName());
		
		return doc;
	}

	@Override
	public Comment toObject(DBObject doc) {
		Comment comment = new Comment(
				(Date) doc.get("postDate"),
				(Double) doc.get("grade"),
				(String) doc.get("message"),
				(String) doc.get("userName"));		
		
		return comment;		
	}

}
