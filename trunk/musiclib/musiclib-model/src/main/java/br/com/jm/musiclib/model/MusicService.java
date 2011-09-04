package br.com.jm.musiclib.model;

import java.util.List;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;

public interface MusicService {


	public abstract Music getMusic(String musicId);
	
	public abstract List<Music> searchMusics(String search);	
	
	public abstract void addTag(Music music, String tag);

	public abstract void addComment(Music music,
			Comment comentario);
	
	public abstract MusicFile getMusicFile(String musicFileId);

    public void processIndexerEvent(MusicIndexerEvent event);

}