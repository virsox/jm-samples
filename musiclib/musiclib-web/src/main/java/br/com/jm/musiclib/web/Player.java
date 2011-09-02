/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class Player implements Serializable {
   
    @EJB
    MusicService musicService;
          
    @Inject
    UserBean user;
    
    int currentIndex = 0;
    
    String selectedPlaylist;
    Playlist currentPlaylist;
    
    
    public String getSelectedPlaylist() {
        return this.selectedPlaylist;
    }
    
    public void setSelectedPlaylist(String playlist) {
        this.selectedPlaylist = playlist;
    }
    
    public Playlist getCurrentPlaylist() {
        if (selectedPlaylist != null) {
            for (Playlist playlist : user.getCurrentUser().getPlaylists()) {
                if (playlist.getName().equals(this.selectedPlaylist)) {
                    this.currentPlaylist = playlist;
                    break;
                }
            }
        }
        return this.currentPlaylist;
    }
    
    public void setCurrentPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
    }    
    
    public Music getCurrentMusic() {
        String musicId = this.currentPlaylist.getMusics().get(currentIndex);        
        Music music = musicService.getMusic(musicId);
        return music;
    }
    
    public void selectPlaylist() {
        currentIndex = 0;        
    }
    
    public void next() {
        
    }
    
    public void previous() {
        
    }
    
    
}
