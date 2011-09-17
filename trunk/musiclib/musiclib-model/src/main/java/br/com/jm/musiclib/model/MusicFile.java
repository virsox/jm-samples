package br.com.jm.musiclib.model;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Classe que representa o arquivo que contém os dados binários de uma música.
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
   * Obtém nome do arquivo.
   * @return Nome do arquivo.
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Obtém InputStream do qual os dados binários devem ser obtidos.
   * @return InputStream do qual os dados devem ser obtidos.
   */
  public InputStream getInputStream() {
    return this.stream;
  }

}
