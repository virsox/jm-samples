package br.com.jm.musiclib.model.converter;

import com.mongodb.DBObject;

/**
 * Interface gen�rica para convers�o de/para objetos da aplica��o e
 * documentos BSON manipulados pelo Mongo.
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * @param <T> Tipo dos objetos sendo convertidos.
 */
public interface Converter<T> {

  /**
   * A partir de um objeto da aplica��o, cria documento BSON correspondente.
   * @param obj Objeto da aplica��o sendo convertido.
   * @return Documento BSON equivalente ao objeto.
   */
  public DBObject toDBObject(T obj);

  /**
   * A partir de um documento BSON, cria objeto da aplica��o correspondente.
   * @param doc Documento BSON sendo convertido.
   * @return Objeto da aplica��o equivalente ao documento.
   */
  public T toObject(DBObject doc);

}
