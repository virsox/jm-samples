package br.com.jm.musiclib.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.UserService;

/**
 * Bean respons�vel por criar a playlist.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@ConversationScoped
@Named(value = "createPlaylist")
public class CreatePlaylist implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -521826768044601787L;

  /**
   * EJB UserService injetado pelo container.
   */
  @Inject
  private UserService userService;

  /**
   * EJB MusicService injetado pelo container.
   */
  @Inject
  private MusicService musicService;

  /**
   * UserBean injetado pelo container.
   */
  @Inject
  private UserBean user;

  /**
   * Escopo conversation injetado pelo container.
   */
  @Inject
  private Conversation conv;

  /** Nome da playlist */
  private String name;
  /** Campo de busca de m�sica */
  private String searchValue;
  /** Lista de m�sicas selecionadas */
  private List<Music> selectedMusics;
  /** Lista do resultado de busca */
  private List<Music> searchResults;

  /**
   * Inicia a conversa��o.
   * 
   * @return redirecionamento para a p�gina de cria��o de playlist.
   */
  public String start() {
    conv.begin();

    selectedMusics = new ArrayList<Music>();
    searchResults = new ArrayList<Music>();

    return "playlist.xhtml";
  }

  /**
   * Cria a playlist criada pelo usu�rio e finaliza a conversa��o.
   * 
   * @return redirecionamento para a p�gina main
   * 
   * @see UserService#addPlaylist(br.com.jm.musiclib.model.User, Playlist)
   */
  public String create() {

    Playlist p = new Playlist(name);
    for (Music music : selectedMusics) {
      p.addMusic(music);
    }

    user.getCurrentUser().addPlaylist(p);

    userService.update(user.getCurrentUser()); //, p);
    conv.end();

    return "main.xhtml";
  }

  /**
   * Finaliza a conversa��o e retorna para a p�gina main.
   * 
   * @return redirecionamento para a p�gina main
   */
  public String cancel() {
    conv.end();
    return "main.xhtml";
  }

  /**
   * Adiciona a m�sica selecionada na lista de m�sicas selecionadas e remove
   * do resultado de busca.
   * 
   * @param music
   * 
   */
  public void addMusic(Music music) {
    // Adicionar na lista de selecionados
    this.selectedMusics.add(music);
    // Remover da lista da busca
    this.searchResults.remove(music);
  }

  /**
   * Remove a m�sica selecionada da lista de m�sicas selecionadas e adiciona
   * na lista do resultado da busca.
   * 
   * @param music
   */
  public void removeMusic(Music music) {
    // Remover da lista de selecionados
    this.selectedMusics.remove(music);
    // Adicionar na lista da busca
    this.searchResults.add(music);
  }

  /**
   * Procura por m�sicas indexadas com o nome especificado pelo campo
   * <tt>searchValue</tt>
   * 
   * @see MusicService#searchMusics(String)
   */
  public void search() {
    this.searchResults = musicService.searchMusics(this.searchValue);
  }

  /**
   * @return o nome da playlist
   */
  public String getName() {
    return this.name;
  }

  /**
   * Altera o nome da playlist
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return o campo de busca
   */
  public String getSearchValue() {
    return this.searchValue;
  }

  /**
   * Altera o campo de busca
   * 
   * @param searchValue
   */
  public void setSearchValue(String searchValue) {
    this.searchValue = searchValue;
  }

  /**
   * 
   * @return a lista com o resultado da busca por m�sicas
   */
  public List<Music> getSearchResults() {
    return this.searchResults;

  }

  /**
   * 
   * @return a lista de m�sicas selecionadas.
   */
  public List<Music> getSelectedMusics() {
    return this.selectedMusics;
  }

}
