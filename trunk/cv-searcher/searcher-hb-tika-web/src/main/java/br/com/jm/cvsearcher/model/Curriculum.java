package br.com.jm.cvsearcher.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;

import br.com.jm.cvsearcher.bridge.TikaBridge;

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
	private byte[] content;

	/**
	 * Construtor padrão.
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
	 *            Conteúdo do currículo do candidato
	 * 
	 * @see #setName(String)
	 * @see #setEmail(String)
	 */
	public Curriculum(String name, String email, byte[] content) {
		this.setName(name);
		this.setEmail(email);
		this.setContent(content);
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

	/**
	 * @return the content
	 */
	@Column(length = 100000)
	@Lob
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, bridge =

	@FieldBridge(impl = TikaBridge.class, params = @Parameter(name = "type", value = "DOC")))
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		if (name != null) {
			return name.hashCode();
		}
		return super.hashCode();
	}
}
