package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Playlist {
	
	private String name;
	private List<String> musics;
	
	public Playlist(String name) {
		this.name = name;
		this.musics = new ArrayList<String>();
	}

	public Playlist(String name, List<String> musics) {
		this.name = name;
		this.musics = musics;
	}
	
	public void addMusic(Music music) {
		this.musics.add(music.getId());
	}
	


	public List<String> getMusics() {
		return this.musics;
	}
	
	public String getName() {
        return this.name;
    }
	
    
}
