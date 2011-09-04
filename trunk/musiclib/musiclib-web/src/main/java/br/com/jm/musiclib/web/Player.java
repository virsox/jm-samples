/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;

@Named
@Singleton
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

		if (this.currentPlaylist != null) {
			String musicId = this.currentPlaylist.getMusics().get(currentIndex);
			Music music = musicService.getMusic(musicId);

			return music;
		}
		return null;
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

	public Music next() {
		if (!getMusics().isEmpty()) {
			return getMusics().get(currentIndex++);
		}
		return null;
	}

}
