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

/**
 * Classe que encapsula a obtenção das informações do MongoDB.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@ApplicationScoped
public class MongoProvider {
    
	/** Objeto que representa o banco de dados. */
	private DB db;
	
	/** Objeto que representa a coleção de músicas. */
	private DBCollection musicsColl;
	
	/** Objeto que representa a coleção de usuários. */
	private DBCollection usersColl;
	
	/** Instânca do GridFS para manipulação de arquivos. */
	private GridFS musicsFS;
	
	/**
	 * Método executado após a construção do objeto.
	 * @throws UnknownHostException Caso não seja possível se conectar ao
	 * Mongo.
	 */
	@PostConstruct
    public void initDB() throws UnknownHostException {
		initDB("musicsDB", false);
    }

	/**
	 * Inicializa os objetos do MongoDB.
	 * @param dbName Nome do banco sendo acessado.
	 * @param drop true se o banco deve ser limpo na inicialização.
	 * @throws UnknownHostException Caso não seja possível se conectar ao
	 * Mongo.
	 */
	protected void initDB(String dbName, boolean drop)
			throws UnknownHostException {
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
	

	/**
	 * Produz objeto que representa o banco de dados do Mongo.
	 * @return objeto que representa o banco de dados do Mongo.
	 */
	@Produces
	public DB getDataBase() {
		return db;
	}	
	
	/**
	 * Produz instânca do GridFS para manipulação de arquivos. 
	 * @return Instânca do GridFS para manipulação de arquivos. 
	 */
	@Produces
	public GridFS getMusicGridFS() {
		return this.musicsFS;
	}
	
	/**
	 * Produz objeto que representa a coleção de músicas.
	 * @return objeto que representa a coleção de músicas.
	 */
	@Produces @MusicCollection
	public DBCollection getMusicCollection() {
		return this.musicsColl;
	}
	
	/**
	 * Produz objeto que representa a coleção de usuários.
	 * @return objeto que representa a coleção de usuários.
	 */
	@Produces @UserCollection
	public DBCollection getUserCollection() {
		return this.usersColl;
	}

}
