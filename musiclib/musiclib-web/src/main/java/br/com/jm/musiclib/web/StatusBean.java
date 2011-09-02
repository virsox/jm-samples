package br.com.jm.musiclib.web;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;

@Singleton
public class StatusBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6445790885225082845L;
	/**
	 * 
	 */
	private MusicInfo currentMusicInfo;

	/**
	 * Flag que indica se o processo de indexação está completo.
	 */
	private boolean completed;

	public void process(@Observes MusicIndexerEvent event) {
		setCurrentMusicInfo(event.getMusicInfo());
		setCompleted(event.getCompleted());
	}

	public MusicInfo getCurrentMusicInfo() {
		return currentMusicInfo;
	}

	public void setCurrentMusicInfo(MusicInfo currentMusicInfo) {
		this.currentMusicInfo = currentMusicInfo;
	}

	public String getTitle() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getTitle();
	}

	public String getArtist() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getArtist();
	}

	public String getAlbum() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getAlbum();
	}

	public String getFileName() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getFileName();
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

}
