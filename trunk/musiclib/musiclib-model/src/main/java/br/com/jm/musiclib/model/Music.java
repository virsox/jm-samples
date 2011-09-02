package br.com.jm.musiclib.model;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;

public class Music {
	private String id;
	
	private int trackNumber;
	private String title;
	private String artistName;
	private String albumName;
	
	private List<String> tags;
	
	
	private SortedSet<Comment> comments;
	
	
	public Music(int trackNumber, String title, String artistName, String albumName, String tag) {
		this(null, trackNumber, title, artistName, albumName,
				Collections.singletonList(tag),
				new TreeSet<Comment>());
	}
	
	
	public Music(String id, int trackNumber, String title, String artistName,
			String albumName, List<String> tags, SortedSet<Comment> comments) {
		this.id = id;
		this.trackNumber = trackNumber;
		this.title = title;
		this.artistName = artistName;
		this.albumName = albumName;
		this.tags = new ArrayList<String>(tags);
		this.comments = comments;
	}

	
	public void setId(String id) {
		this.id = id;
	}
	
	public DBObject toDBObject() {
		BasicDBObject doc = new BasicDBObject();
	
		doc.put("trackNumber", trackNumber);
		doc.put("title", title);
		doc.put("artistName", artistName);
		doc.put("albumName", albumName);
		
		BasicDBList tagsList = new BasicDBList();
		for (String tag : tags) {
			tagsList.add(tag);
		}
		doc.put("tags", tagsList);
		
		BasicDBList comentariosList = new BasicDBList();
		for (Comment comentario : comments) {
			comentariosList.add(comentario.toDBObject());
		}
		doc.put("comments", comentariosList);
		
		return doc;
	}

	@SuppressWarnings("unchecked")
	public static Music getMusic(DBObject doc) {
	
		List<DBObject> commentDocs = (List<DBObject>) doc.get("comments");
		SortedSet<Comment> comments = new TreeSet<Comment>();
		
		for (DBObject commentDoc : commentDocs) {
			comments.add(Comment.getComment(commentDoc));
		}
				
		Music music = new Music(
				((ObjectId) doc.get("_id")).toString(),
				(Integer) doc.get("trackNumber"),
				(String) doc.get("title"),
				(String) doc.get("artistName"),
				(String) doc.get("albumName"),
				(List<String>) doc.get("tags"),
				comments		
		);
		
		return music;
		
	}
	
	
	public String getId() {
		return this.id;
	}

	public int getTrackNumber() {
		return trackNumber;
	}
	
    public String getTitle() {
        return this.title;
    }
    
	public String getArtistName() {
		return this.artistName;
	}


	public void addTag(String tag) {
		this.tags.add(tag);
	}


	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
}
