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

	/**
	 * Construtor padrão.
	 */
	public MusicInfo() {
		this.tags = new ArrayList<String>();
	}

	/**
	 * Adiciona uma tag
	 * 
	 * @param tag
	 *            Tag a ser adicionada
	 * @return true se a tag foi inserida
	 */
	public boolean addTag(String tag) {
		return this.tags.add(tag);
	}

	/**
	 * Remove uma tag
	 * 
	 * @param tag
	 *            Tag a ser removida
	 * @return true se a tag foi removida
	 */
	public boolean removeTag(String tag) {
		return this.tags.remove(tag);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist
	 *            the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return the album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @param album
	 *            the album to set
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * @return the trackNumber
	 */
	public String getTrackNumber() {
		return trackNumber;
	}

	/**
	 * @param trackNumber
	 *            the trackNumber to set
	 */
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
