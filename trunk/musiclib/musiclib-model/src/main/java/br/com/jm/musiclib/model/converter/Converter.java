package br.com.jm.musiclib.model.converter;

import com.mongodb.DBObject;

/**
 * Interface genérica para conversão de/para objetos da aplicação e
 * documentos BSON manipulados pelo Mongo.
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * @param <T> Tipo dos objetos sendo convertidos.
 */
public interface Converter<T> {

  /**
   * A partir de um objeto da aplicação, cria documento BSON correspondente.
   * @param obj Objeto da aplicação sendo convertido.
   * @return Documento BSON equivalente ao objeto.
   */
  public DBObject toDBObject(T obj);

  /**
   * A partir de um documento BSON, cria objeto da aplicação correspondente.
   * @param doc Documento BSON sendo convertido.
   * @return Objeto da aplicação equivalente ao documento.
   */
  public T toObject(DBObject doc);

}
