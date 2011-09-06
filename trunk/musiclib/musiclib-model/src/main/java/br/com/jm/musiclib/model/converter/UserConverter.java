package br.com.jm.musiclib.model.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.User;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
/**
 * Implementação do Converter para objetos do tipo User.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@ApplicationScoped
public class UserConverter implements Converter<User> {
	
	/** Converter para objetos Playlist. */
	private Converter<Playlist> playlistConv;
	
	/**
	 * Construtor.
	 * @param playlistConverter Conversor de objetos Playlist.
	 */
	@Inject
	public UserConverter(Converter<Playlist> playlistConverter) {
		this.playlistConv = playlistConverter;
	}
	
	/** Construtor sem parâmetros - necessário para o CDI. */
	public UserConverter() { }
	
	/** {@inheritDoc} */
	public DBObject toDBObject(User user) {
		DBObject doc = BasicDBObjectBuilder.start()
			.add("name", user.getName())
			.add("login", user.getLogin())
			.add("password", user.getPassword())
			.get();
		
		BasicDBList playlistList = new BasicDBList();
		for (Playlist playlist : user.getPlaylists()) {
			playlistList.add(playlistConv.toDBObject(playlist));
		}
		doc.put("playlists", playlistList);
		
		BasicDBList execucoesList = new BasicDBList();
		for (Map.Entry<String, Integer> entry : user.getExecutions().entrySet()) {
			DBObject execucao = BasicDBObjectBuilder.start()
				.add("music", new ObjectId(entry.getKey()))
				.add("quantity", entry.getValue())
				.get();
			execucoesList.add(execucao);
		}
		doc.put("executions", execucoesList);
				
		return doc;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public User toObject(DBObject doc) {
		List<DBObject> playlistsDocs = (List<DBObject>) doc.get("playlists");
		List<Playlist> playlists = new ArrayList<Playlist>();
		
		for (DBObject playlistDoc : playlistsDocs) {
			playlists.add(playlistConv.toObject(playlistDoc));
		}
				
		List<DBObject> executionsDocs = (List<DBObject>) doc.get("executions");
		Map<String, Integer> executions = new HashMap<String, Integer>();
		for (DBObject executionDoc : executionsDocs) {
			executions.put(
				((ObjectId) executionDoc.get("music")).toString(),
				(Integer) executionDoc.get("quantity"));			
		}
		
		User user = new User(
				((ObjectId) doc.get("_id")).toString(),
				(String) doc.get("name"),
				(String) doc.get("login"),
				(String) doc.get("password"),
				playlists,
				executions);		
		
		return user;
	}
	
}
