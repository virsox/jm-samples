package br.com.jm.cvsearcher.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

/**
 * POJO que representa um currículo.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Entity
@Indexed
public class Curriculum {

  /** Identificador do currículo. */
  private Long id;
	
  /** Nome do candidato */
  private String name;
  /** Email do candidato */
  private String email;
  /** Endereço do candidato. */
  private Address address = new Address();
  /** Conteúdo do currículo do candidato */
  private String content;

  /**
   * Construtor padrão.
   */
  public Curriculum() { }

  /**
   * Construtor completo. Inicializa as propriedades <tt>name</tt>,
   * <tt>email</tt>, <tt>content</tt> e <tt>address</tt>.
   * 
   * @param name
   *            Nome do candidato
   * @param email
   *            Email do candidato
   * @param content
   *            Conteúdo do currículo do candidato
   * @param address
   *            Endereço do candidato.
   * 
   * @see #setName(String)
   * @see #setEmail(String)
   * @see #setContent(String)
   */
  public Curriculum(String name, String email, String content, Address address) {
    this.setName(name);
    this.setEmail(email);
    this.setContent(content);
    this.setAddress(address);
  }

  /**
   * @return the id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @DocumentId
  public Long getId() {
    return id;
  }
  
  /**
   * @param id
   *            the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }
  
  /**
   * @return the name
   */
  @Column
  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
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
  @Column
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
  @Column
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
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

  /**
   * @return the address
   */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "fk_address_id")
  @IndexedEmbedded
  public Address getAddress() {
    return this.address;
  }
  
  /**
   * @param address
   *            the address to set
   */
  public void setAddress(Address address) {
    this.address = address;
  }
  
}
