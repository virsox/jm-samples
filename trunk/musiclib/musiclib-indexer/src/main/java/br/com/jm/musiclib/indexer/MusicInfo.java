package br.com.jm.musiclib.indexer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto que contém as informações extraidas dos arquivos mp3
 * 
 * @author Sigrist
 * 
 */
public class MusicInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** ID único da música na coleção */
	private Long id;
	/** Nome da música */
	private String title;
	/** Nome do artista */
	private String artist;
	/** Nome do album */
	private String album;
	/** Número da faixa */
	private String trackNumber;
	/** lista de tags associadas à música */
	private List<String> tags;
	/** Nome do arquivo com as informações extraídas */
	private String fileName;
	/** Array de bytes com o binário representando a capa do album */
	private byte[] cover;

	public MusicInfo() {
		this.tags = new ArrayList<String>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	 
	
	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] cover) {
		this.cover = cover;
	}

	/**
	 * Adiciona uma tag 
	 * @param tag Tag a ser adicionada
	 * @return true se a tag foi inserida
	 */
	public boolean addTag(String tag) {
		return this.tags.add(tag);
	}

	/**
	 * Remove uma tag 
	 * @param tag Tag a ser removida
	 * @return true se a tag foi removida
	 */
	public boolean removeTag(String tag) {
		return this.tags.remove(tag);
	}

}
