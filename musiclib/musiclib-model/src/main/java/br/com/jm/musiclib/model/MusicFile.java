package br.com.jm.musiclib.model;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Classe que representa o arquivo que cont�m os dados bin�rios de uma m�sica.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class MusicFile implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7065206754846941630L;

  /** Nome do arquivo. */
  private String fileName;

  /** InputStream do qual os dados podem ser obtidos. */
  private InputStream stream;

  /**
   * Construtor.
   * @param fileName Nome do arquivo.
   * @param inputStream Stream do qual os dados podem ser obtidos.
   */
  public MusicFile(String fileName, InputStream inputStream) {
    this.fileName = fileName;
    this.stream = inputStream;
  }

  /**
   * Obt�m nome do arquivo.
   * @return Nome do arquivo.
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Obt�m InputStream do qual os dados bin�rios devem ser obtidos.
   * @return InputStream do qual os dados devem ser obtidos.
   */
  public InputStream getInputStream() {
    return this.stream;
  }

}
