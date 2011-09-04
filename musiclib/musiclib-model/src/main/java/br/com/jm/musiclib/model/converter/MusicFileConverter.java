package br.com.jm.musiclib.model.converter;

import javax.enterprise.context.ApplicationScoped;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import br.com.jm.musiclib.model.MusicFile;

@ApplicationScoped
public class MusicFileConverter implements Converter<MusicFile> {

	@Override
	public DBObject toDBObject(MusicFile musicFile) {
		throw new UnsupportedOperationException("Operação não suportada!");
	}

	@Override
	public MusicFile toObject(DBObject doc) {
		
		GridFSDBFile dbFile = (GridFSDBFile) doc;
		MusicFile musicFile = new MusicFile(
				(String) dbFile.get("filename"),
				dbFile.getInputStream());
				
		return musicFile;
	}

}
