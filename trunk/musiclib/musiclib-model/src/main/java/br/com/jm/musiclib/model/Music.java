package br.com.jm.musiclib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Classe que representa uma música da aplicação.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Music {
	
	/** Identificador interno da música. */
	private String id;
	
	/** Número da faixa (dentro do álbum). */
	private int trackNumber;
	
	/** Nome da música. */
	private String title;
	
	/** Nome do artista. */
	private String artistName;
	
	/** Nome do álbum. */
	private String albumName;
	
	/** Indentificador para o arquivo. */
	private String fileId;
	
	/** Lista de tags associadas à música. */
	private List<String> tags;
	
	/** Conjunto de comentários associados à música. */
	private SortedSet<Comment> comments;
	
	/**
	 * Construtor.
	 * @param trackNumber Número da faixa.
	 * @param title Nome da música.
	 * @param artistName Nome do artista.
	 * @param albumName Nome do álbum.
	 * @param fileId Identificador para o arquivo.
	 * @param tag Uma tag para a música.
	 */
	public Music(int trackNumber, String title, String artistName,
				 String albumName, String fileId,  String tag) {
		this(null, trackNumber, title, artistName, albumName, fileId,
				Collections.singletonList(tag),
				new TreeSet<Comment>());
	}
	
	/**
	 * Construtor com todos os atributos.
	 * 
	 * @param id Identificador interno da música.
	 * @param trackNumber Número da faixa.
	 * @param title Nome da música.
	 * @param artistName Nome do artista.
	 * @param albumName Nome do álbum.
	 * @param fileId Identificador para o arquivo.
	 * @param tags Lista de tags associadas à música.
	 * @param comments Conjunto de comentários associados à música.
	 */
	public Music(String id, int trackNumber, String title, String artistName,
			String albumName, String fileId, List<String> tags,
			SortedSet<Comment> comments) {
		this.id = id;
		this.trackNumber = trackNumber;
		this.title = title;
		this.artistName = artistName;
		this.albumName = albumName;
		this.fileId = fileId;
		this.tags = new ArrayList<String>(tags);
		this.comments = comments;
	}

	/**
	 * Obtém identificador interno da música.
	 * @return identificador interno da música.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Obtém número da faixa.
	 * @return número da faixa.
	 */
	public int getTrackNumber() {
		return trackNumber;
	}
	
	/**
	 * Obtém título da música.
	 * @return Título da música.
	 */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Obtém nome do artista.
     * @return Nome do artista.
     */
	public String getArtistName() {
		return this.artistName;
	}

	/**
	 * Obtém nome do álbum.
	 * @return Nome do álbum.
	 */
	public String getAlbumName() {
		return this.albumName;
	}

	/**
	 * Obtém lista de tags associadas à música.
	 * @return lista de tags associadas à música.
	 */
	public List<String> getTags() {
		return this.tags;
	}

	/**
	 * Obtém conjunto de comentários associados à música.
	 * @return conjunto de comentários associados à música.
	 */
	public SortedSet<Comment> getComments() {
		return this.comments;
	}

	/**
	 * Obtém identificador para o arquivo.
	 * @return Identificador para o arquivo.
	 */
	public String getFileId() {
		return this.fileId;
	}
	
	/**
	 * Configura o identificador interno da música.
	 * @param id identificador interno da música.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Adiciona uma nova tag ao conjunto de tags da música.
	 * @param tag Nova tag a ser adicionada.
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}

	/**
	 * Adiciona novo comentário ao conjunto de comentários associados.
	 * @param comment Comentário a ser adicionado.
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}





	
	
}
