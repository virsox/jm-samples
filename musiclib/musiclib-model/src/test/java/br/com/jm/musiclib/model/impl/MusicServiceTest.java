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

import br.com.jm.musiclib.indexer.MusicIndexerEvent;
import br.com.jm.musiclib.indexer.MusicInfo;
import br.com.jm.musiclib.model.Comment;
import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.converter.CommentConverter;
import br.com.jm.musiclib.model.converter.MusicConverter;
import br.com.jm.musiclib.model.converter.MusicFileConverter;


public class MusicServiceTest {

	private static MongoProvider mongo;
	private static MusicServiceBean service;
	
	private static Music music;
	
	@BeforeClass
	public static void setUp() throws UnknownHostException {
		mongo = new MongoProvider();
		mongo.initDB("musicsDBTest", true);
				
		service = new MusicServiceBean();
		service.musicsColl = mongo.getMusicCollection();
		service.musicsGridFS = mongo.getMusicGridFS();
		
		
		CommentConverter commentConv = new CommentConverter();
		MusicConverter musicConv = new MusicConverter(commentConv);

		service.commentConv = commentConv;
		service.musicConv = musicConv;
		service.musicFileConv = new MusicFileConverter();
	}
	
	@Test
	public void testProcessIndexerEvent() {
		MusicInfo info = new MusicInfo();
		
		info.setTrackNumber("1");
		info.setTitle("Enter Sanman");
		info.setArtist("Metalico");		
		info.setAlbum("Gray Album");
		info.setTags(Collections.singletonList("Heavy Metal"));
		info.setFileName("/Users/virso/Music/iTunes/iTunes Media/Music/" +
			"Beady Eye/Different Gear, Still Speeding/01 Four Letter Word.mp3");
		
		MusicIndexerEvent event = new MusicIndexerEvent(info);
		music = service.processIndexerEvent(event);
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
	

}
