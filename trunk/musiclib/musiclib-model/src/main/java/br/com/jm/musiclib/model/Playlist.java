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
	
	public DBObject toDBObject() {
		BasicDBObject doc = new BasicDBObject();		
		doc.put("name", name);
		
		List<ObjectId> musicIds = new ArrayList<ObjectId>();
		for (String music : musics) {
			musicIds.add(new ObjectId(music));
		}		
		doc.put("musics", musicIds);
		
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	public static Playlist getPlaylist(DBObject doc) {
		
		List<ObjectId> musicIds = (List<ObjectId>) doc.get("musics");
		List<String> musics = new ArrayList<String>();
		for (ObjectId id : musicIds) {
			musics.add(id.toString());
		}
		
		Playlist playlist = new Playlist((String) doc.get("name"), musics);
		return playlist;				
	}

	public List<String> getMusics() {
		return this.musics;
	}
	
	public String getName() {
        return this.name;
    }
	
    
}
