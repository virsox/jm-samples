package br.com.jm.musiclib.model.impl;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import br.com.jm.musiclib.model.cdi.MusicCollection;
import br.com.jm.musiclib.model.cdi.UserCollection;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;


@ApplicationScoped
public class MongoProvider {
    
	private DB db;
	private DBCollection musicsColl;
	private DBCollection usersColl;
	
	private GridFS musicsFS;
	
	@PostConstruct
    public void initDB() throws UnknownHostException {
		initDB("musicsDB", false);
    }

	protected void initDB(String dbName, boolean drop) throws UnknownHostException {
        Mongo m = new Mongo();

        if (drop) {
        	m.dropDatabase(dbName);
        }
        db = m.getDB(dbName);

        musicsColl = db.getCollection("musics");
        if (musicsColl == null) {
            musicsColl = db.createCollection("musics", null);
        }
        
        usersColl = db.getCollection("users");
        if (usersColl == null) {
        	usersColl = db.createCollection("users", null);
        }		
        
        musicsFS = new GridFS(this.db, "musics");
	}
	

	@Produces
	public DB getDataBase() {
		return db;
	}	
	
	@Produces
	public GridFS getMusicGridFS() {
		return this.musicsFS;
	}
	
	
	@Produces @MusicCollection
	public DBCollection getMusicCollection() {
		return this.musicsColl;
	}
	
	@Produces @UserCollection
	public DBCollection getUserCollection() {
		return this.usersColl;
	}


	
}
