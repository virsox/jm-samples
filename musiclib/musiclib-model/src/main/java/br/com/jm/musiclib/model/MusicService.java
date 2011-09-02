package br.com.jm.musiclib.model;

import java.util.List;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;

public interface MusicService {

	//public abstract void createIndex(File dir);

	public abstract Music getMusic(String musicId);
	
	public abstract List<Music> searchMusics(String search);
	
	
	public abstract void addTag(Music music, String tag);

	public abstract void addComment(Music music,
			Comment comentario);
	

    public void processIndexerEvent(MusicIndexerEvent event);

}