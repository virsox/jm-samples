package br.com.jm.musiclib.model.impl;

import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.User;

public class UsuarioServiceTest {
	
	private static MongoProvider mongo;	
	
	private static MusicServiceBean musicService;
	private static UserServiceBean userService;
	
	@BeforeClass
	public static void setUp() throws UnknownHostException {
		mongo = new MongoProvider();
		mongo.initDB("musicsDBTest", true);
	
		musicService = new MusicServiceBean();
		musicService.setDataBase(mongo.getDataBase());
		musicService.setMusicCollection(mongo.getMusicCollection());
		
		userService = new UserServiceBean();
		userService.setUserCollection(mongo.getUserCollection());
	}
	
	@Test
	public void testCriaUsuario() {
		//DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Music music1 = new Music(1, "Enter Sandman", "Metallica", "Black Album", null, "Rock");
		Music music2 = new Music(2, "Sad But True", "Metallica", "Black Album", null, "Rock");		
		Music music3 = new Music(3, "Unforgiven", "Metallica", "Black Album", null, "Rock");
		
		musicService.createMusic(music1);
		musicService.createMusic(music2);
		musicService.createMusic(music3);			
		
		User user = new User("Wilson", "wah", "123");		
		userService.createUser(user);
				
		Playlist playlist = new Playlist("Lista de Rock");
		playlist.addMusic(music1);
		playlist.addMusic(music2);
		playlist.addMusic(music3);
        user.addPlaylist(playlist);
		
		userService.addPlaylist(user, playlist);
		userService.play(user, music1);
		userService.play(user, music1);
		userService.play(user, music2);
		
	}
	
}
