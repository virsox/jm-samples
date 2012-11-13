package br.com.jm.cvsearcher.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * POJO que representa o endere�o do candidato.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Entity
public class Address {
  /** Identificador do endere�o. */
  private Long id;
  /** Nome da rua. */
  private String streetName;
  /** N�mero da casa */
  private Integer number;
  /** Cidade */
  private String city;
  /** Estado */
  private String state;

  /**
   * @return the id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
   * @return the streetName
   */
  @Column
  public String getStreetName() {
    return streetName;
  }

  /**
   * @param streetName
   *            the streetName to set
   */
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  /**
   * @return the number
   */
  @Column
  public Integer getNumber() {
    return number;
  }

  /**
   * @param number
   *            the number to set
   */
  public void setNumber(Integer number) {
    this.number = number;
  }

  /**
   * @return the city
   */
  @Column
  public String getCity() {
    return city;
  }

  /**
   * @param city
   *            the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return the state
   */
  @Column
  public String getState() {
    return state;
  }

  /**
   * @param state
   *            the state to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * {@inheritDoc}
   * 
   * Retorna o endere�o em um formato String amig�vel ao usu�rio. O formato �:
   * 
   * <strong>nome da rua</strong>, <strong>n�mero</strong> -
   * <strong>cidade</strong> (<strong>estado</strong>)
   * 
   */
  @Override
  public String toString() {
    return streetName + ", " + number + " - " + city + "(" + state + ")";
  }

}
