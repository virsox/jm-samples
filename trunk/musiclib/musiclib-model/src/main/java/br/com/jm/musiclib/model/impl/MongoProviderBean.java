package br.com.jm.musiclib.model.impl;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


@Singleton
@Startup
public class MongoProviderBean {
    
	private DB db;
	private DBCollection musicsColl;
	private DBCollection usersColl;
	
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
	}
	

	public DB getDataBase() {
		return db;
	}	
	
	public DBCollection getMusicCollection() {
		return this.musicsColl;
	}
	
	public DBCollection getUsersCollection() {
		return this.usersColl;
	}


	
}
