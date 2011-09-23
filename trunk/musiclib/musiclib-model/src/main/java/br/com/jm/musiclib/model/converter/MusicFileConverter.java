package br.com.jm.musiclib.model.converter;

import javax.enterprise.context.ApplicationScoped;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import br.com.jm.musiclib.model.MusicFile;

/**
 * Implementa��o do Converter para objetos do tipo MusicaFile.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@ApplicationScoped
public class MusicFileConverter implements Converter<MusicFile> {

  /** {@inheritDoc} */
  @Override
  public DBObject toDBObject(MusicFile musicFile) {
    throw new UnsupportedOperationException("Opera��o n�o suportada!");
  }

  /** {@inheritDoc} */
  @Override
  public MusicFile toObject(DBObject doc) {

    GridFSDBFile dbFile = (GridFSDBFile) doc;
    MusicFile musicFile = new MusicFile((String) dbFile.get("filename"),
        dbFile.getInputStream());

    return musicFile;
  }

}
