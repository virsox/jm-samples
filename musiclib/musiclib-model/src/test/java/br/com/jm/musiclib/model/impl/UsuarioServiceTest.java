package br.com.jm.musiclib.model.impl;

import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.converter.CommentConverter;
import br.com.jm.musiclib.model.converter.MusicConverter;
import br.com.jm.musiclib.model.converter.PlaylistConverter;
import br.com.jm.musiclib.model.converter.UserConverter;

public class UsuarioServiceTest {
	
	private static MongoProvider mongo;	
	
	private static MusicServiceBean musicService;
	private static UserServiceBean userService;
	
	@BeforeClass
	public static void setUp() throws UnknownHostException {
		mongo = new MongoProvider();
		mongo.initDB("musicsDBTest", true);
	
		musicService = new MusicServiceBean();
		musicService.musicsColl = mongo.getMusicCollection();

		CommentConverter commentConv = new CommentConverter();
		MusicConverter musicConv = new MusicConverter(commentConv);
		musicService.commentConv = commentConv;
		musicService.musicConv = musicConv;
		
		userService = new UserServiceBean();
		userService.userColl = mongo.getUserCollection();
		
		PlaylistConverter playlistConv = new PlaylistConverter();
		UserConverter userConv = new UserConverter(playlistConv);
		userService.userConv = userConv;
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
		
		userService.update(user); //, playlist);
		userService.play(user, music1);
		userService.play(user, music1);
		userService.play(user, music2);
		
	}
	
}
