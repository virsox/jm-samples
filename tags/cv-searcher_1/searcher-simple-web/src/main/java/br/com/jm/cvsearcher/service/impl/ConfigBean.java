package br.com.jm.cvsearcher.service.impl;

import javax.enterprise.context.ApplicationScoped;

/**
 * Bean utilitário que contém as configurações para a pasta de indexação e
 * armazenamento dos currículos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@ApplicationScoped
public class ConfigBean {
  /**
   * Pasta do armazenamento de currículos
   * 
   * @see #rootFolder
   */
  private static final String DIRECTORY_FILES = "/files/";
  /**
   * Pasta que contém o índice
   * 
   * @see #rootFolder
   */
  private static final String DIRECTORY_INDEX = "/index/";
  /**
   * Pasta raiz dos arquivos da aplicação.
   */
  private String rootFolder;

  /**
   * Construtor padrão.
   * 
   * Inicializa a pasta de indexação e armazenamento de currículos como
   * c:/Temp.
   */
  public ConfigBean() {
    this.setRootFolder("/Users/virso/tmp/cv-searcher");
  }

  /**
   * 
   * @return retorna a pasta de armazenamento de currículos
   * @see #DIRECTORY_FILES
   * @see #getRootFolder()
   */
  public String getFilesDirectory() {
    return getRootFolder() + DIRECTORY_FILES;
  }

  /**
   * 
   * @return retorna a pasta que contém o índice
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
