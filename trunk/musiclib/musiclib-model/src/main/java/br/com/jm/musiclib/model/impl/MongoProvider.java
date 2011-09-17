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
 * Classe que encapsula a obten��o das informa��es do MongoDB.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@ApplicationScoped
public class MongoProvider {

  /** Objeto que representa o banco de dados. */
  private DB db;

  /** Objeto que representa a cole��o de m�sicas. */
  private DBCollection musicsColl;

  /** Objeto que representa a cole��o de usu�rios. */
  private DBCollection usersColl;

  /** Inst�nca do GridFS para manipula��o de arquivos. */
  private GridFS musicsFS;

  /**
   * M�todo executado ap�s a constru��o do objeto.
   * @throws UnknownHostException Caso n�o seja poss�vel se conectar ao
   * Mongo.
   */
  @PostConstruct
  public void initDB() throws UnknownHostException {
    initDB("musicsDB", false);
  }

  /**
   * Inicializa os objetos do MongoDB.
   * @param dbName Nome do banco sendo acessado.
   * @param drop true se o banco deve ser limpo na inicializa��o.
   * @throws UnknownHostException Caso n�o seja poss�vel se conectar ao
   * Mongo.
   */
  protected void initDB(String dbName, boolean drop)
      throws UnknownHostException
  {
    Mongo m = new Mongo();

    if (drop) {
      m.dropDatabase(dbName);
    }
    db = m.getDB(dbName);

    // tenta obter a cole��o - se n�o conseguir cria 
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
   * Produz inst�nca do GridFS para manipula��o de arquivos. 
   * @return Inst�nca do GridFS para manipula��o de arquivos. 
   */
  @Produces
  public GridFS getMusicGridFS() {
    return this.musicsFS;
  }

  /**
   * Produz objeto que representa a cole��o de m�sicas.
   * @return objeto que representa a cole��o de m�sicas.
   */
  @Produces @MusicCollection
  public DBCollection getMusicCollection() {
    return this.musicsColl;
  }

  /**
   * Produz objeto que representa a cole��o de usu�rios.
   * @return objeto que representa a cole��o de usu�rios.
   */
  @Produces
  @UserCollection
  public DBCollection getUserCollection() {
    return this.usersColl;
  }

}
