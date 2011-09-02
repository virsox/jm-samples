package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

public class User {
	private String id;
	private String name;
	private String login;
	private String password;
	
	private List<Playlist> playlists;	
	private Map<String, Integer> executions;
	
	public User() {
		this(null, null, null);
	}
    
	public User(String name, String login, String password) {
		this(null, name, login, password, new ArrayList<Playlist>(),
				new HashMap<String, Integer>());
	}
	
	public User(String id, String name, String login, String password,
			List<Playlist> playlists, Map<String, Integer> executions) {		
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;		
		this.playlists = playlists;
		this.executions = executions;
	}	
	
	public void addPlaylist(Playlist playlist) {
		this.playlists.add(playlist);
		for (String musicId : playlist.getMusics()) {
			if (!executions.containsKey(musicId)) {
				executions.put(musicId, 0);
			}
		}
		
	}
	
	public void incExecution(String musicaId) {
		Integer qtd = executions.get(musicaId);
		if (qtd == null) {
			qtd = 0;
		}
		qtd = qtd + 1;
		executions.put(musicaId, qtd);
	}
	
	
	public DBObject toDBObject() {
		DBObject doc = BasicDBObjectBuilder.start()
			.add("name", name)
			.add("login", login)
			.add("password", password)
			.get();
		
		BasicDBList playlistList = new BasicDBList();
		for (Playlist playlist : playlists) {
			playlistList.add(playlist.toDBObject());
		}
		doc.put("playlists", playlistList);
		
		BasicDBList execucoesList = new BasicDBList();
		for (Map.Entry<String, Integer> entry : executions.entrySet()) {
			DBObject execucao = BasicDBObjectBuilder.start()
				.add("music", new ObjectId(entry.getKey()))
				.add("quantity", entry.getValue())
				.get();
			execucoesList.add(execucao);
		}
		doc.put("executions", execucoesList);
				
		return doc;
	}

	@SuppressWarnings("unchecked")
	public static User getUser(DBObject doc) {
		List<DBObject> playlistsDocs = (List<DBObject>) doc.get("playlists");
		List<Playlist> playlists = new ArrayList<Playlist>();
		
		for (DBObject playlistDoc : playlistsDocs) {
			playlists.add(Playlist.getPlaylist(playlistDoc));
		}
				
		List<DBObject> executionsDocs = (List<DBObject>) doc.get("executions");
		Map<String, Integer> executions = new HashMap<String, Integer>();
		for (DBObject executionDoc : executionsDocs) {
			executions.put(
				((ObjectId) executionDoc.get("music")).toString(),
				(Integer) executionDoc.get("quantity"));			
		}
		
		User user = new User(
				((ObjectId) doc.get("_id")).toString(),
				(String) doc.get("name"),
				(String) doc.get("login"),
				(String) doc.get("password"),
				playlists,
				executions);		
		
		return user;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
	
    
    
}
