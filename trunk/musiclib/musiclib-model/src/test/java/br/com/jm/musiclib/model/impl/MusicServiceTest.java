package br.com.jm.musiclib.model.impl;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;

import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;


public class MusicServiceTest {

	private static MongoProviderBean mongo;
	private static MusicServiceBean service;
	
	private static Music music;
	
	@BeforeClass
	public static void setUp() throws UnknownHostException {
		mongo = new MongoProviderBean();
		mongo.initDB("musicsDBTest", true);
				
		service = new MusicServiceBean();
		service.setMongoProvider(mongo);
	}
	
	@Test
	public void testProcessIndexerEvent() {
		MusicInfo info = new MusicInfo();
		
		info.setTrackNumber("1");
		info.setTitle("Enter Sanman");
		info.setArtist("Metalico");		
		info.setAlbum("Gray Album");
		info.setTags(Collections.singletonList("Heavy Metal"));
		info.setFileName("metalico - enter sanman.mp3");
		
		MusicIndexerEvent event = new MusicIndexerEvent(info);
		//music = service.processIndexerEvent(event);
	}
	
	@Test
	public void testAddTag() {
		
		service.addTag(music, "Rock");
		service.addTag(music, "Anos 90");
	}

	@Test
	public void testAddComment() throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				
		Comment comentario1 = new Comment(df.parse("09/09/2009 10:00"), 10,
				"Metallica r0x", "user1");		
		Comment comentario2 = new Comment(df.parse("10/10/2010 13:23"), 9.5,
				"Metallica r0x 2", "user2");

		service.addComment(music, comentario1);
		service.addComment(music, comentario2);		
	}
	
	
	@Test
	public void testGetMusic() {
		Music result = service.getMusic(music.getId());
		System.out.println(result);
	}
	
	@Test
	public void testSearchMusic() {		
		
		List<Music> musics = service.searchMusics("sanMan");
		assertEquals(1, musics.size());
		assertEquals(music.getId(), musics.get(0).getId());
		
	}
	
	
//	@Test
//	public void testCriaArquivo() {
//		File file = new File("/Users/virso/Desktop/eurail-map-2011.pdf");
//		service.criaArquivo(file);
//	}
	
}
