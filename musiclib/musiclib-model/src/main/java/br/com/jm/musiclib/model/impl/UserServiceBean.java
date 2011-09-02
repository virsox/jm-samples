package br.com.jm.musiclib.model.impl;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import br.com.jm.musiclib.model.Music;
import br.com.jm.musiclib.model.Playlist;
import br.com.jm.musiclib.model.User;
import br.com.jm.musiclib.model.UserService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless()
@Local(UserService.class)
public class UserServiceBean implements UserService {
	
	@Inject
	private MongoProviderBean mongo;
	
	public void setMongoProvider(MongoProviderBean mongo) {
		this.mongo = mongo;
	}
	
	
	public String createUser(User user) {
				
        DBObject doc = user.toDBObject();        
        DBCollection usersColl = mongo.getUsersCollection();
        
        WriteResult result = usersColl.insert(doc);
        
        // TODO corrigir este tratamento
        if (result.getError() != null) {
        	throw new RuntimeException("Deu pane!");
        }
        
        ObjectId id = (ObjectId) doc.get("_id");        
        user.setId(id.toString());
        return user.getId();    
	}
	
	/* (non-Javadoc)
	 * @see br.com.jm.musiclib.model.impl.UserService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public User login(String login, String password) {
		
		DBCollection usersColl = mongo.getUsersCollection();
		
		DBObject loginQuery = new BasicDBObject();
		loginQuery.put("login", login);
		loginQuery.put("password", password);
		
		DBObject result = usersColl.findOne(loginQuery);
		if (result != null) {
			return User.getUser(result);
		}
		return null;
	}
	
	@Override
	public User play(User user, Music music) {		
		//user.incExecution(music.getId());
		
		BasicDBObject key = new BasicDBObject("_id", new ObjectId(user.getId()));
		key.put("executions.music", new ObjectId(music.getId()));
		
		DBObject update = new BasicDBObject("$inc",
				new BasicDBObject("executions.$.quantity", 1));
		
		DBCollection usersColl = mongo.getUsersCollection();
		usersColl.update(key, update);
		
		return user;
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.jm.musiclib.model.impl.UserService#addPlaylist(br.com.jm.musiclib.model.User, br.com.jm.musiclib.model.Playlist)
	 */
	@Override
	public User addPlaylist(User user, Playlist playlist) {
		//user.addPlaylist(playlist);
		
		BasicDBObject key = new BasicDBObject("_id", new ObjectId(user.getId()));		
    	DBCollection usersColl = mongo.getUsersCollection();
    	usersColl.update(key, user.toDBObject());
		
//    	BasicDBObject update = new BasicDBObject("$push",
//    			new BasicDBObject("playlists", playlist.toDBObject()));
//    	
//    	    	
//    	for (String music : playlist.getMusics()) {
//    		DBObject musicExecution = new BasicDBObject();
//    		musicExecution.put("music", music);
//    		musicExecution.put("")
//    		
//    		update = new BasicDBObject("$addToSet",
//        		new BasicDBObject("executions", playlist.toDBObject()));
//    	}
    	
    	

		
    	return user;
	}
	
	

	
}
