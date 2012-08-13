package br.com.jm.cvsearcher.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.SearchService;

/**
 * Bean CDI para busca de currículos.
 * 
 * 
 * 
 */
@SessionScoped
@Named
public class SearchBean implements Serializable {
	/**
   * 
   */
	private static final long serialVersionUID = 7942216607824066013L;

	/**
	 * Enum para indicar qual é o tipo de busca a ser realizado.
	 * 
	 * @author Paulo Sigrist / Wilson A. Higashino
	 * 
	 */
	public enum SearchType {
		/** Buscas por nome */
		NAME,
		/** Buscas pelo conteúdo do currículo */
		CONTENT
	}

	/** Palavra chave a ser buscada */
	private String keyword;
	/** Tipo de busca a ser feita */
	private SearchType type;
	/** Resultado da busca */
	private List<Curriculum> results;

	/**
	 * Construtor padrão. Inicializa os atributos <tt>result</tt> como uma lista
	 * vazia e o campo <tt>type</tt> como o tipo {@link SearchType#CONTENT}.
	 */
	public SearchBean() {
		this.results = Collections.emptyList();
		this.type = SearchType.CONTENT;
	}

	/** Bean de serviço. Injetado pelo container. */
	@Inject
	private SearchService service;

	/**
	 * Efetua a busca.
	 * 
	 * Se o campo <tt>type</tt> for inválido, retorna uma lista vazia.
	 * 
	 * @see SearchService#findCVByContent(String)
	 * @see SearchService#findCVByName(String)
	 */
	public void search() {
		FacesContext context = FacesContext.getCurrentInstance();

		// Verifica o tipo de busca a ser feito
		switch (type) {
		case NAME: {
			// Caso seja por nome, chama o método respectivo do seriço de busca
			try {
				results = service.findCVByName(keyword);
			} catch (CurriculumException e) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
			}
			break;
		}
		case CONTENT: {
			// Caso seja por conteúdo, chama o método respectivo do seriço de
			// busca
			try {
				results = service.findCVByContent(keyword);
			} catch (CurriculumException e) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
			}
			break;
		}
		default: {
			// Caso seja por inválido, retorne uma lista vazia
			results = Collections.emptyList();
			break;
		}
		}

	}

	/**
	 * Método utilitário para facilitar o download do arquivo binário. Obtém os
	 * dados do curriculum e envia para o cliente indicando o formato Microsoft
	 * Word
	 * 
	 * @param curriculum
	 *            de qual currículo será extraído o arquivo.
	 * @return o conteúdo do arquivo no formato Microsoft Word
	 */
	public StreamedContent downloadFile(Curriculum curriculum) {
		InputStream stream = new ByteArrayInputStream(curriculum.getContent());
		StreamedContent file = new DefaultStreamedContent(
				stream,
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				curriculum.getName() + ".docx");
		return file;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the type
	 */
	public SearchType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(SearchType type) {
		this.type = type;
	}

	/**
	 * @return the results
	 */
	public List<Curriculum> getResults() {
		return results;
	}

}
