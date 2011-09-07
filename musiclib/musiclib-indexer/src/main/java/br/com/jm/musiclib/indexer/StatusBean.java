package br.com.jm.musiclib.indexer;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

/**
 * Bean que guarda a última música encontrada pelo indexador de músicas.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Singleton
public class StatusBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6445790885225082845L;
	/**
	 * Referência para a última música encontrada
	 */
	private MusicInfo currentMusicInfo;

	/**
	 * Flag que indica se o processo de indexação está completo.
	 */
	private boolean completed;

	/**
	 * Recebe um evento do tipo MusicIndexerEvent disparado pelo MusicIndexer.
	 * Salva a referência da música encontrada e marca se o precesso foi
	 * finalizado.
	 * 
	 * @param event
	 * 
	 * @see MusicIndexer
	 * @see MusicIndexerImpl
	 */
	public void process(@Observes MusicIndexerEvent event) {
		setCurrentMusicInfo(event.getMusicInfo());
		setCompleted(event.getCompleted());
	}

	/**
	 * @return a música atual. Null se não existe.
	 */
	public MusicInfo getCurrentMusicInfo() {
		return currentMusicInfo;
	}

	/**
	 * Altera a música atual
	 * 
	 * @param currentMusicInfo
	 */
	public void setCurrentMusicInfo(MusicInfo currentMusicInfo) {
		this.currentMusicInfo = currentMusicInfo;
	}

	/**
	 * @return o título da música atual ou vazio se não existe
	 */
	public String getTitle() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getTitle();
	}

	/**
	 * @return o artista da música atual ou vazio se não existe
	 */
	public String getArtist() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getArtist();
	}

	/**
	 * @return o album da música atual ou vazio se não existe
	 */
	public String getAlbum() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getAlbum();
	}

	/**
	 * @return o nome do arquivo da música atual ou vazio se não existe
	 */
	public String getFileName() {
		return currentMusicInfo == null ? "" : currentMusicInfo.getFileName();
	}

	/**
	 * @return a faixa da música atual ou vazio se não existe
	 */
	public String getTrackNumber() {
		return currentMusicInfo == null ? "" : currentMusicInfo
				.getTrackNumber();
	}

	/**
	 * @return flag indicando que o processo de indexação foi finalizado.
	 */
	public boolean getCompleted() {
		return completed;
	}

	/**
	 * Altera a flag q indica que o processo foi finalizado
	 * 
	 * @param completed
	 */
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

}
