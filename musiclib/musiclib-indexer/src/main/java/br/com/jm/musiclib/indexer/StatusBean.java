package br.com.jm.musiclib.indexer;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;



@Singleton
public class StatusBean implements Serializable {
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

	public String getTrackNumber() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getTrackNumber();
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

}
