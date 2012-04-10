package br.com.jm.cvsearcher.service.impl;

import javax.enterprise.context.ApplicationScoped;

/**
 * Bean utilit�rio que cont�m as configura��es para a pasta de indexa��o e
 * armazenamento dos curr�culos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@ApplicationScoped
public class ConfigBean {
  /**
   * Pasta do armazenamento de curr�culos
   * 
   * @see #rootFolder
   */
  private static final String DIRECTORY_FILES = "/files/";
  /**
   * Pasta que cont�m o �ndice
   * 
   * @see #rootFolder
   */
  private static final String DIRECTORY_INDEX = "/index/";
  /**
   * Pasta raiz dos arquivos da aplica��o.
   */
  private String rootFolder;

  /**
   * Construtor padr�o.
   * 
   * Inicializa a pasta de indexa��o e armazenamento de curr�culos como
   * c:/Temp.
   */
  public ConfigBean() {
    this.setRootFolder("/Users/virso/tmp/cv-searcher");
  }

  /**
   * 
   * @return retorna a pasta de armazenamento de curr�culos
   * @see #DIRECTORY_FILES
   * @see #getRootFolder()
   */
  public String getFilesDirectory() {
    return getRootFolder() + DIRECTORY_FILES;
  }

  /**
   * 
   * @return retorna a pasta que cont�m o �ndice
   * @see #DIRECTORY_INDEX
   * @see #getRootFolder()
   */
  public String getIndexDirectory() {
    return getRootFolder() + DIRECTORY_INDEX;
  }

  /**
   * @return the rootFolder
   */
  public String getRootFolder() {
    return rootFolder;
  }

  /**
   * @param rootFolder
   *            the rootFolder to set
   */
  public void setRootFolder(String rootFolder) {
    this.rootFolder = rootFolder;
  }

}
