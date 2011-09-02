/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ConversationScoped
@Named
public class CreatePlaylist implements Serializable {
    
    @EJB
    UserService userService;
    
    @EJB
    MusicService musicService;
    
    @Inject
    UserBean user;
   
    @Inject
    Conversation conv;
    
    
    private String name;
    private String searchValue;
    
    private List<Music> selectedMusics;
    private List<Music> searchResults;
    
    public String start() {
        conv.begin();
        
        selectedMusics = new ArrayList<Music>();
        searchResults = new ArrayList<Music>();
        
        return "playlist.xhtml";
    }
    
    public String create() {
        
        Playlist p = new Playlist(name);
        for (Music music : selectedMusics) {
            p.addMusic(music);
        }
        
        user.getCurrentUser().addPlaylist(p);
        
        userService.addPlaylist(user.getCurrentUser(), p);        
        conv.end();
        
        return "main.xhtml";
    }
    
    public String cancel() {
        conv.end();
        return "main.xhtml";
    }
    
    public void addMusic(Music music) {
        this.selectedMusics.add(music);
    }
    
    public void search() {
        this.searchResults = musicService.searchMusics(this.searchValue);
    }
    
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSearchValue() {
        return this.searchValue;
    }
    
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
    
    public List<Music> getSearchResults() {
        return this.searchResults;
    
    }
    
    public List<Music> getSelectedMusics() {
        return this.selectedMusics;
    }
    
}
