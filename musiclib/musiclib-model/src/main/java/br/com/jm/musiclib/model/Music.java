package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Music {
	private String id;
	
	private int trackNumber;
	private String title;
	private String artistName;
	private String albumName;
	private String fileId;
	
	private List<String> tags;
	
	
	private SortedSet<Comment> comments;
	
	
	public Music(int trackNumber, String title, String artistName,
				 String albumName, String fileId, String tag) {
		this(null, trackNumber, title, artistName, albumName, fileId,
				Collections.singletonList(tag),
				new TreeSet<Comment>());
	}
	
	
	public Music(String id, int trackNumber, String title, String artistName,
			String albumName, String fileId, List<String> tags, SortedSet<Comment> comments) {
		this.id = id;
		this.trackNumber = trackNumber;
		this.title = title;
		this.artistName = artistName;
		this.albumName = albumName;
		this.fileId = fileId;
		this.tags = new ArrayList<String>(tags);
		this.comments = comments;
	}

	
	public void setId(String id) {
		this.id = id;
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


	public String getAlbumName() {
		return this.albumName;
	}


	public List<String> getTags() {
		return this.tags;
	}


	public SortedSet<Comment> getComments() {
		return this.comments;
	}


	public String getFileId() {
		return this.fileId;
	}
	
}
