package br.com.jm.cvsearcher.service;

/**
 * Exception disparada pelo SearchService quando algum método falha.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * @see SearchService
 */
public class CurriculumException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 3250163722501611445L;

  /**
   * Construtor simples.
   * 
   * @param message
   *            Mensagem de erro.
   */
  public CurriculumException(String message) {
    super(message);
  }

  /**
   * Construtor completo.
   * 
   * @param message
   *            Mensagem de erro
   * @param cause
   *            Causa original da exception
   */
  public CurriculumException(String message, Throwable cause) {
    super(message, cause);
  }

}
