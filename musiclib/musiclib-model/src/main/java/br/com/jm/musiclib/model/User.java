package br.com.jm.musiclib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa um usuário da aplicação.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class User implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8510416693981733737L;

  /** Identificador interno de um usuário. */
  private String id;

  /** Nome do usuário. */
  private String name;

  /** Login do usuário. */
  private String login;

  /** Senha. */
  private String password;

  /** Lista de playlists do usuário. */
  private List<Playlist> playlists;

  /**
   * Map contendo o número de vezes que uma determinada música foi executada.
   * A chave é o identificador da música, e o valor o número de vezes.
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
   *            Nome do usuário.
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
   *            Identificador interno do usuário.
   * @param name
   *            Nome.
   * @param login
   *            Login.
   * @param password
   *            Senha.
   * @param playlists
   *            Lista de playlists do usuário.
   * @param executions
   *            Map contendo o número de execuções de cada música.
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
   * Adiciona uma nova playlist ao usuário.
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
   * Incremente o número de execuções de uma determinada música.
   * 
   * @param musicaId
   *            Música a ter o número de execuções incrementada.
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
   * Configura o identificador interno de um usuário.
   * 
   * @param id
   *            Indeitificador interno.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Configura o nome do usuário.
   * 
   * @param name
   *            Nome do usuário.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Configura o login do usuário.
   * 
   * @param login
   *            login do usuário.
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * Configura a senha do usuário.
   * 
   * @param password
   *            Senha.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Configura o conjunto de playlists do usuário.
   * 
   * @param playlists
   *            Conjunto de playlists do usuário.
   */
  public void setPlaylists(List<Playlist> playlists) {
    this.playlists = playlists;
  }

  /**
   * Obtém identificador interno do usuário.
   * 
   * @return Identificador interno do usuário.
   */
  public String getId() {
    return id;
  }

  /**
   * Obtém login do usuário.
   * 
   * @return Login do usuário.
   */
  public String getLogin() {
    return login;
  }

  /**
   * Obtém nome do usuário.
   * 
   * @return nome do usuário.
   */
  public String getName() {
    return name;
  }

  /**
   * Obtém senha do usuário.
   * 
   * @return senha do usuário.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Obtém lista de playlists pertencentes ao usuário.
   * 
   * @return lista de playlists pertencentes ao usuário.
   */
  public List<Playlist> getPlaylists() {
    return playlists;
  }

  /**
   * Obtém map contendo o número de vezes que uma determinada música foi
   * executada
   * 
   * @return map contendo o número de vezes que uma determinada música foi
   *         executada
   */
  public Map<String, Integer> getExecutions() {
    return this.executions;
  }

}
