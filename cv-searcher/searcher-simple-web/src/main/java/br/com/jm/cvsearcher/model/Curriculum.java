package br.com.jm.cvsearcher.model;

/**
 * POJO que representa um curr�culo.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
public class Curriculum {

  /** Nome do candidato */
  private String name;
  /** Email do candidato */
  private String email;
  /** Conte�do do curr�culo do candidato */
  private String content;

  /**
   * Construtor padr�o.
   */
  public Curriculum() {

  }

  /**
   * Construtor completo. Inicializa as propriedades <tt>name</tt>,
   * <tt>email</tt> e <tt>content</tt>.
   * 
   * @param name
   *            Nome do candidato
   * @param email
   *            Email do candidato
   * @param content
   *            Conte�do do curr�culo do candidato
   * 
   * @see #setName(String)
   * @see #setEmail(String)
   * @see #setContent(String)
   */
  public Curriculum(String name, String email, String content) {
    this.setName(name);
    this.setEmail(email);
    this.setContent(content);
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *            the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *            the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content
   *            the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

}
