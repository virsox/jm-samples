package br.com.jm.musiclib.model;

import java.util.Date;

/**
 * Classe que representa um comentário das músicas.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class Comment implements Comparable<Comment> {

	/** Data de postagem. */
	private Date postDate;

	/** Nota dada para a música. */
	private double grade;

	/** Mensagem associada ao comentário. */
	private String message;

	/** Nome do usuário associado ao comentário. */
	private String userName;

	/**
	 * Construtor.
	 * 
	 * @param date
	 *            Data de postagem.
	 * @param grade
	 *            Nota dada para a música.
	 * @param message
	 *            Mensagem associada ao comentário.
	 * @param userName
	 *            Nome do usuário associado ao comentário.
	 */
	public Comment(Date date, double grade, String message, String userName) {
		this.postDate = date;
		this.grade = grade;
		this.message = message;
		this.userName = userName;
	}

	public Comment() {

	}

	/**
	 * Obtém data de postagem.
	 * 
	 * @return Data de postagem do comentário.
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * Obtém nota dada para a música.
	 * 
	 * @return Nota dada para a música.
	 */
	public double getGrade() {
		return grade;
	}

	/**
	 * Obtém mensagem associada ao comentário.
	 * 
	 * @return Mensagem associada ao comentário.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Obtém usuário associado ao comentário.
	 * 
	 * @return Usuário associado ao comentário.
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
