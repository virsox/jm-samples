package br.com.jm.musiclib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Classe que representa uma m�sica da aplica��o.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Music implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3471296001691844288L;

  /** Identificador interno da m�sica. */
  private String id;

  /** N�mero da faixa (dentro do �lbum). */
  private int trackNumber;

  /** Nome da m�sica. */
  private String title;

  /** Nome do artista. */
  private String artistName;

  /** Nome do �lbum. */
  private String albumName;

  /** Indentificador para o arquivo. */
  private String fileId;

  /** Lista de tags associadas � m�sica. */
  private List<String> tags;

  /** Conjunto de coment�rios associados � m�sica. */
  private SortedSet<Comment> comments;

  /**
   * Construtor.
   * @param trackNumber N�mero da faixa.
   * @param title Nome da m�sica.
   * @param artistName Nome do artista.
   * @param albumName Nome do �lbum.
   * @param fileId Identificador para o arquivo.
   * @param tag Uma tag para a m�sica.
   */
  public Music(int trackNumber, String title, String artistName,
      String albumName, String fileId, String tag)
  {
    this(null, trackNumber, title, artistName, albumName, fileId, Collections
        .singletonList(tag), new TreeSet<Comment>());
  }

  /**
   * Construtor com todos os atributos.
   * 
   * @param id Identificador interno da m�sica.
   * @param trackNumber N�mero da faixa.
   * @param title Nome da m�sica.
   * @param artistName Nome do artista.
   * @param albumName Nome do �lbum.
   * @param fileId Identificador para o arquivo.
   * @param tags Lista de tags associadas � m�sica.
   * @param comments Conjunto de coment�rios associados � m�sica.
   */
  public Music(String id, int trackNumber, String title, String artistName,
      String albumName, String fileId, List<String> tags,
      SortedSet<Comment> comments)
  {
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
   * Obt�m identificador interno da m�sica.
   * @return identificador interno da m�sica.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Obt�m n�mero da faixa.
   * @return n�mero da faixa.
   */
  public int getTrackNumber() {
    return trackNumber;
  }

  /**
   * Obt�m t�tulo da m�sica.
   * @return T�tulo da m�sica.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Obt�m nome do artista.
   * @return Nome do artista.
   */
  public String getArtistName() {
    return this.artistName;
  }

  /**
   * Obt�m nome do �lbum.
   * @return Nome do �lbum.
   */
  public String getAlbumName() {
    return this.albumName;
  }

  /**
   * Obt�m lista de tags associadas � m�sica.
   * @return lista de tags associadas � m�sica.
   */
  public List<String> getTags() {
    return this.tags;
  }

  /**
   * Obt�m conjunto de coment�rios associados � m�sica.
   * @return conjunto de coment�rios associados � m�sica.
   */
  public SortedSet<Comment> getComments() {
    return this.comments;
  }

  /**
   * @return o conjunto de coment�rios como uma lista.
   * 
   * @see #getComments()
   */
  public List<Comment> getCommentsAsList() {
    return new ArrayList<Comment>(getComments());
  }

  /**
   * Obt�m identificador para o arquivo.
   * @return Identificador para o arquivo.
   */
  public String getFileId() {
    return this.fileId;
  }

  /**
   * Configura o identificador interno da m�sica.
   * @param id identificador interno da m�sica.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Adiciona uma nova tag ao conjunto de tags da m�sica.
   * @param tag Nova tag a ser adicionada.
   */
  public void addTag(String tag) {
    this.tags.add(tag);
  }

  /**
   * Adiciona novo coment�rio ao conjunto de coment�rios associados.
   * @param comment Coment�rio a ser adicionado.
   */
  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

}
