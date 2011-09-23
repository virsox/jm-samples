package br.com.jm.musiclib.model;

import java.util.List;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;

/**
 * Interface de servi�o contendo os m�todos para manipula��o de m�sicas.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public interface MusicService {

  /**
   * Obt�m uma m�sica a partir de seu identificador interno.
   * @param musicId Identificador interno da m�sica.
   * @return M�sica a partir do identificador informado.
   */
  public Music getMusic(String musicId);

  /**
   * Busca por m�sicas que possem em seu nome a String informada como filtro.
   * @param search String a ser utilizada como filtro.
   * @return Todas m�sicas que possuem em seu nome o filtro informado.
   */
  public List<Music> searchMusics(String search);

  /**
   * Adiciona uma nova tag a m�sica informada.
   * @param music M�sica sendo alterada.
   * @param tag Tag a ser adicionada.
   */
  public void addTag(Music music, String tag);

  /**
   * Adiciona um novo coment�rio a m�sica informada.
   * @param music M�sica sendo alterada.
   * @param comment Coment�rio a ser adicionado.
   */
  public void addComment(Music music, Comment comment);

  /**
   * Obt�m objeto que representa o arquivo com os dados bin�rios da m�sica.
   * @param musicFileId Identificador do arquivo a ser obtido.
   * @return objeto que representa o arquivo com os dados bin�rios da m�sica.
   */
  public MusicFile getMusicFile(String musicFileId);

  /**
   * Processa um evento gerado pelo componente indexer.
   * @param event Evento a ser processado.
   * @return M�sica criada resultante do processamento do evento.
   */
  public Music processIndexerEvent(MusicIndexerEvent event);

}