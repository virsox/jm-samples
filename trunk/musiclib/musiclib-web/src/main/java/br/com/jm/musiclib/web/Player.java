/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jm.musiclib.web;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.MusicFile;
import br.com.jm.musiclib.model.MusicService;
import br.com.jm.musiclib.model.Playlist;

/**
 * Bean respons�vel por gerenciar o Player.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Named
@SessionScoped
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2550035241033339662L;

	/**
	 * EJB MusicService injetado pelo container.
	 */
	@EJB
	private MusicService musicService;

	/**
	 * UserBean injetado pelo container.
	 */
	@Inject
	private UserBean user;

	/**
	 * Posi��o atual da m�sica que est� sendo tocada.
	 */
	private int currentIndex = 0;

	/**
	 * Nome da playlist selecionada
	 */
	private String selectedPlaylist;
	/**
	 * Playlist selecionada.
	 */
	private Playlist currentPlaylist;
	/**
	 * Lista de m�sicas da playlist selecionada
	 */
	private List<Music> musics;
	/**
	 * M�sica que est� tocando.
	 */
	private Music currentMusic;
	/**
	 * Novo coment�rio que pode ser adicionado pelo usu�rio
	 */
	private Comment comment = new Comment();

	/**
	 * @return o nome da playlist selecionada
	 */
	public String getSelectedPlaylist() {
		return this.selectedPlaylist;
	}

	/**
	 * Altera o nome da playlist selecionada.
	 * 
	 * @param playlist
	 */
	public void setSelectedPlaylist(String playlist) {
		this.selectedPlaylist = playlist;
	}

	/**
	 * @return a playlist selecionada
	 */
	public Playlist getCurrentPlaylist() {
		if (selectedPlaylist != null) {
			for (Playlist playlist : user.getCurrentUser().getPlaylists()) {
				if (playlist.getName().equals(this.selectedPlaylist)) {
					this.currentPlaylist = playlist;
					break;
				}
			}
		}
		return this.currentPlaylist;
	}

	/**
	 * Altera a playlist selecionada
	 * 
	 * @param playlist
	 */
	public void setCurrentPlaylist(Playlist playlist) {
		this.currentPlaylist = playlist;
		this.musics = null;
	}

	/**
	 * 
	 * @return a m�sica atual.
	 */
	public Music getCurrentMusic() {
		return currentMusic;
	}

	/**
	 * 
	 * @return a lista de m�sicas da playlist selecionada ou lista vazia caso
	 *         n�o exista nenhuma playlist selecionada.
	 * 
	 */
	public List<Music> getMusics() {
		if (currentPlaylist == null) {
			return Collections.emptyList();
		}
		if (musics == null) {
			musics = new ArrayList<Music>();
			for (String musicId : currentPlaylist.getMusics()) {
				musics.add(musicService.getMusic(musicId));
			}
		}
		return musics;
	}

	/**
	 * Seleciona a playlist e zera a posi��o do �ndice.
	 */
	public void selectPlaylist() {
		currentIndex = 0;
	}

	/**
	 * Move o �ndice para a posi��o anterior.
	 */
	public void previous() {
		currentIndex--;
		if (currentIndex < 0) {
			currentIndex = getMusics().size() - 1;
		}
	}

	/**
	 * Move o �ndice para a pr�xima posi��o.
	 */
	public void next() {
		currentIndex++;
		if (currentIndex >= getMusics().size()) {
			currentIndex = 0;
		}
	}

	/**
	 * 
	 * @return a m�sica selecionada como Stream de �udio mp3 para ser executado.
	 * 
	 * @see MusicService#getMusicFile(String)
	 */
	public StreamedContent getMedia() {
		// Verificar se a lista de m�sicas n�o est� vazia
		if (!getMusics().isEmpty()) {
			StreamedContent media;
			InputStream inputStream;
			MusicFile file;

			// Obt�m a m�sica correspondente ao objeto atual
			currentMusic = musics.get(currentIndex);
			// Obt�m o arquivo
			file = musicService.getMusicFile(currentMusic.getFileId());
			// Cria o input stream
			inputStream = new BufferedInputStream(file.getInputStream());
			// Cria um objeto StreamedContent do tipo audio/mp3
			media = new DefaultStreamedContent(inputStream, "audio/mp3");

			// Verifica se estourou o �ndice da lista
			if (currentIndex >= getMusics().size()) {
				currentIndex = 0;
			}

			return media;
		}
		return null;
	}

	/**
	 * Salva um coment�rio feito pelo usu�rio
	 * 
	 * @see MusicService#addComment(Music, Comment)
	 */
	public void saveComment() {
		comment.setUserName(user.getCurrentUser().getName());
		comment.setPostDate(new Date());

		musicService.addComment(currentMusic, comment);
		comment = new Comment();
	}

	/**
	 * 
	 * @return o objeto comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * Altera o objeto comment
	 * 
	 * @param comment
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
