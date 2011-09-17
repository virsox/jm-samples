package br.com.jm.musiclib.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe que representa um coment�rio das m�sicas.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Comment implements Comparable<Comment>, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5879534563674225503L;

  /** Data de postagem. */
  private Date postDate;

  /** Nota dada para a m�sica. */
  private double grade;

  /** Mensagem associada ao coment�rio. */
  private String message;

  /** Nome do usu�rio associado ao coment�rio. */
  private String userName;

  /**
   * Construtor.
   * 
   * @param date
   *            Data de postagem.
   * @param grade
   *            Nota dada para a m�sica.
   * @param message
   *            Mensagem associada ao coment�rio.
   * @param userName
   *            Nome do usu�rio associado ao coment�rio.
   */
  public Comment(Date date, double grade, String message, String userName) {
    this.postDate = date;
    this.grade = grade;
    this.message = message;
    this.userName = userName;
  }

  /**
   * Construtor vazio.
   */
  public Comment() {

  }

  /**
   * Obt�m data de postagem.
   * 
   * @return Data de postagem do coment�rio.
   */
  public Date getPostDate() {
    return postDate;
  }

  /**
   * Obt�m nota dada para a m�sica.
   * 
   * @return Nota dada para a m�sica.
   */
  public double getGrade() {
    return grade;
  }

  /**
   * Obt�m mensagem associada ao coment�rio.
   * 
   * @return Mensagem associada ao coment�rio.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Obt�m usu�rio associado ao coment�rio.
   * 
   * @return Usu�rio associado ao coment�rio.
   */
  public String getUserName() {
    return userName;
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(Comment obj) {
    return this.postDate.compareTo(obj.postDate);
  }

  /**
   * @param postDate
   *            the postDate to set
   */
  public void setPostDate(Date postDate) {
    this.postDate = postDate;
  }

  /**
   * @param grade
   *            the grade to set
   */
  public void setGrade(double grade) {
    this.grade = grade;
  }

  /**
   * @param message
   *            the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @param userName
   *            the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

}
