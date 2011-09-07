package br.com.jm.musiclib.model;

import java.util.Date;

/**
 * Classe que representa um coment�rio das m�sicas.
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Comment implements Comparable<Comment> {

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
	 * @param date Data de postagem.
	 * @param grade Nota dada para a m�sica.
	 * @param message Mensagem associada ao coment�rio.
	 * @param userName Nome do usu�rio associado ao coment�rio.
	 */
	public Comment(Date date, double grade, String message, String userName) {
		this.postDate = date;
		this.grade = grade;
		this.message = message;
		this.userName = userName;
	}
	
	
	/**
	 * Obt�m data de postagem.
	 * @return Data de postagem do coment�rio.
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * Obt�m nota dada para a m�sica.
	 * @return Nota dada para a m�sica. 
	 */
	public double getGrade() {
		return grade;
	}

	/**
	 * Obt�m mensagem associada ao coment�rio.
	 * @return Mensagem associada ao coment�rio.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Obt�m usu�rio associado ao coment�rio.
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
	
}