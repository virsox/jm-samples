package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Map<String,Integer> getExecutions() {
		return this.executions;
	}
	
    
    
}
