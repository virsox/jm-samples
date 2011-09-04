package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Classe que representa uma música da aplicação.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Music {
	
	/** Identificador interno da música. */
	private String id;
	
	/** Número da faixa (dentro do álbum). */
	private int trackNumber;
	
	/** Nome da música. */
	private String title;
	
	/** Nome do artista. */
	private String artistName;
	
	/** Nome do álbum. */
	private String albumName;
	
	/** Indentificador para o arquivo. */
	private String fileId;
	
	private List<String> tags;
	
	
	private SortedSet<Comment> comments;
	
	
	public Music(int trackNumber, String title, String artistName,
				 String albumName, String fileId,  String tag) {
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
