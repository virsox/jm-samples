package br.com.jm.musiclib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa um usu�rio da aplica��o.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class User implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8510416693981733737L;

  /** Identificador interno de um usu�rio. */
  private String id;

  /** Nome do usu�rio. */
  private String name;

  /** Login do usu�rio. */
  private String login;

  /** Senha. */
  private String password;

  /** Lista de playlists do usu�rio. */
  private List<Playlist> playlists;

  /**
   * Map contendo o n�mero de vezes que uma determinada m�sica foi executada.
   * A chave � o identificador da m�sica, e o valor o n�mero de vezes.
   */
  private Map<String, Integer> executions;

  /** Construtor sem argumentos. */
  public User() {
    this(null, null, null);
  }

  /**
   * Construtor.
   * 
   * @param name
   *            Nome do usu�rio.
   * @param login
   *            Login.
   * @param password
   *            Senha.
   */
  public User(String name, String login, String password) {
    this(null, name, login, password, new ArrayList<Playlist>(),
        new HashMap<String, Integer>());
  }

  /**
   * Construtor que recebe todos os atributos.
   * 
   * @param id
   *            Identificador interno do usu�rio.
   * @param name
   *            Nome.
   * @param login
   *            Login.
   * @param password
   *            Senha.
   * @param playlists
   *            Lista de playlists do usu�rio.
   * @param executions
   *            Map contendo o n�mero de execu��es de cada m�sica.
   */
  public User(String id, String name, String login, String password,
      List<Playlist> playlists, Map<String, Integer> executions)
  {
    this.id = id;
    this.name = name;
    this.login = login;
    this.password = password;
    this.playlists = playlists;
    this.executions = executions;
  }

  /**
   * Adiciona uma nova playlist ao usu�rio.
   * 
   * @param playlist
   *            Playlist a ser adicionada.
   */
  public void addPlaylist(Playlist playlist) {
    this.playlists.add(playlist);
    for (String musicId : playlist.getMusics()) {
      if (!executions.containsKey(musicId)) {
        executions.put(musicId, 0);
      }
    }
  }

  /**
   * Incremente o n�mero de execu��es de uma determinada m�sica.
   * 
   * @param musicaId
   *            M�sica a ter o n�mero de execu��es incrementada.
   */
  public void incExecution(String musicaId) {
    Integer qtd = executions.get(musicaId);
    if (qtd == null) {
      qtd = 0;
    }
    qtd = qtd + 1;
    executions.put(musicaId, qtd);
  }

  /**
   * Configura o identificador interno de um usu�rio.
   * 
   * @param id
   *            Indeitificador interno.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Configura o nome do usu�rio.
   * 
   * @param name
   *            Nome do usu�rio.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Configura o login do usu�rio.
   * 
   * @param login
   *            login do usu�rio.
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * Configura a senha do usu�rio.
   * 
   * @param password
   *            Senha.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Configura o conjunto de playlists do usu�rio.
   * 
   * @param playlists
   *            Conjunto de playlists do usu�rio.
   */
  public void setPlaylists(List<Playlist> playlists) {
    this.playlists = playlists;
  }

  /**
   * Obt�m identificador interno do usu�rio.
   * 
   * @return Identificador interno do usu�rio.
   */
  public String getId() {
    return id;
  }

  /**
   * Obt�m login do usu�rio.
   * 
   * @return Login do usu�rio.
   */
  public String getLogin() {
    return login;
  }

  /**
   * Obt�m nome do usu�rio.
   * 
   * @return nome do usu�rio.
   */
  public String getName() {
    return name;
  }

  /**
   * Obt�m senha do usu�rio.
   * 
   * @return senha do usu�rio.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Obt�m lista de playlists pertencentes ao usu�rio.
   * 
   * @return lista de playlists pertencentes ao usu�rio.
   */
  public List<Playlist> getPlaylists() {
    return playlists;
  }

  /**
   * Obt�m map contendo o n�mero de vezes que uma determinada m�sica foi
   * executada
   * 
   * @return map contendo o n�mero de vezes que uma determinada m�sica foi
   *         executada
   */
  public Map<String, Integer> getExecutions() {
    return this.executions;
  }

}
