package br.com.jm.cvsearcher.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.NumericField;

/**
 * Endereço associado ao currículo.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@Entity
public class Address {
  
  /** Identificador do endereço. */
  private Long id;
  
  /** Nome do logradouro. */
  private String streetName;
  
  /** Número da casa. */
  private Integer number;
  
  /** Cidade. */
  private String city;
  
  /** Estado. */
  private String state;
  
  public Address() { }
  
  /**
   * @return the id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set.
   */
  public void setId(Long id) {
    this.id = id;
  }
  
  /**
   * @return the street name.
   */
  @Column
  @Field
  public String getStreetName() {
    return streetName;
  }

  /**
   * @param streetName the street name to set.
   */
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  /**
   * @return the number.
   */
  @Column
  @NumericField
  public Integer getNumber() {
    return number;
  }

  /**
   * @param number the number to set.
   */
  public void setNumber(Integer number) {
    this.number = number;
  }

  /**
   * @return the city.
   */
  @Column
  @Field
  public String getCity() {
    return city;
  }

  /**
   * @param city the city to set.
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return the state.
   */
  @Column
  @Field
  public String getState() {
    return state;
  }

  /**
   * @param state the state to set.
   */
  public void setState(String state) {
    this.state = state;
  }
  
  
  
}
