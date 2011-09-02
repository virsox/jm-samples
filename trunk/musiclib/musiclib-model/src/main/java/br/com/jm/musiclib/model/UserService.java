package br.com.jm.musiclib.model;



public interface UserService {

	public abstract User login(String login, String password);

	public abstract User addPlaylist(User user, Playlist playlist);

	public abstract User play(User user, Music music);

	public String createUser(User user);

}