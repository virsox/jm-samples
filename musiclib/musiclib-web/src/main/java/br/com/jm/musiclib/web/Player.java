/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicFile;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;

@Named
@SessionScoped
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2550035241033339662L;

	@EJB
	private MusicService musicService;

	@Inject
	private UserBean user;

	private int currentIndex = 0;

	private String selectedPlaylist;
	private Playlist currentPlaylist;
	private List<Music> musics;
	private Music currentMusic;
	private Comment comment = new Comment();
	
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
		this.musics = null;
	}

	public Music getCurrentMusic() {

		
		return currentMusic;
	}

	public List<Music> getMusics() {
		if (currentPlaylist == null) {
			return Collections.emptyList();
		}
		if (musics == null) {
			musics = new ArrayList<Music>();
			for (String musicId : currentPlaylist.getMusics()) {
				musics.add(musicService.getMusic(musicId));
			}
		}
		return musics;
	}

	public void selectPlaylist() {
		currentIndex = 0;
	}

	public void previous() {
		currentIndex--;
		if (currentIndex < 0) {
			currentIndex =  getMusics().size()-1;
		}
	}

	public void next() {
		currentIndex++;
		if (currentIndex >= getMusics().size()) {
			currentIndex = 0;
		}
	}
	
	public StreamedContent getMedia() {
		
		if (!getMusics().isEmpty()) {
			StreamedContent media;			
			InputStream inputStream;
			currentMusic = musics.get(currentIndex);
			MusicFile file = musicService.getMusicFile(currentMusic.getFileId());
			inputStream = new BufferedInputStream(file.getInputStream());
			
			media = new DefaultStreamedContent(inputStream, "audio/mp3");
			
			if (currentIndex >= getMusics().size()) {
				currentIndex = 0;
			}
			
			return media;
		}
		return null;
	}
	
	public void saveComment() {
		comment.setUserName(user.getCurrentUser().getName());
		comment.setPostDate(new Date());
		currentMusic.addComment(comment);
		musicService.addComment(currentMusic, comment);
		comment = new Comment();
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	

}
