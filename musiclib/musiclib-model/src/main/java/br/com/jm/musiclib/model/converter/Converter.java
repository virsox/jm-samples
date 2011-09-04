package br.com.jm.musiclib.model.converter;

import com.mongodb.DBObject;

public interface Converter<T> {
	public DBObject toDBObject(T obj);
	public T toObject(DBObject doc);
	
}
