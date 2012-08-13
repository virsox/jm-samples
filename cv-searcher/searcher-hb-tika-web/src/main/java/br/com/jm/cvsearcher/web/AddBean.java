package br.com.jm.cvsearcher.web;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.CurriculumService;
import br.com.jm.cvsearcher.util.Constants;

/**
 * Bean CDI para adi��o de novos curr�culos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@RequestScoped
@Named
public class AddBean {
	/** Curr�culo a ser adicionado. */
	private Curriculum cv;

	/** Objeto para receber o arquivo com o curr�culo do candidato */
	private UploadedFile file;

	/** Bean de servi�o. Injetado pelo container. */
	@Inject
	private CurriculumService service;

	/**
	 * Construtor padr�o. Cria uma nova inst�ncia de Curriculum
	 */
	public AddBean() {
		this.cv = new Curriculum();
	}

	/**
	 * @return Retorna a inst�ncia de Curriculum
	 */
	public Curriculum getCv() {
		return cv;
	}

	/**
	 * Adiciona e indexa um novo curr�culo.
	 * 
	 * @return {@link Constants#MAIN} se o curr�culo foi adicionado corretamente
	 *         ou {@link Constants#ADD_CV} caso ocorra algum erro
	 */
	public String addCurriculum() {
		String r;
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			// Chama o servi�o para adicionar e indexar o curr�culo
			service.addCurriculum(cv);
			// Usu�rio ser� redirecionado para a p�gina principal
			r = Constants.MAIN;
			// Adicionar uma mensagem de sucesso
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Sucesso",
					"Curr�culo adicionado com sucesso."));

		} catch (CurriculumException e) {
			// Algum erro aconteceu ao inserir o curr�culo.
			// Redirecionar o usuario para a p�gina de adi��o
			// para outra tentativa
			r = Constants.ADD_CV;
			// Adicionar a mensagem de erro no contexto
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
		}

		return r;
	}

	/**
	 * 
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * Ao receber o arquivo enviado pelo formul�rio, ja extrai o conte�do do
	 * arquivo e adiciona no curr�culo
	 * 
	 * @param file
	 *            the file to set
	 * 
	 * @see UploadedFile#getContents()
	 * @see Curriculum#setContent(byte[])
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
		this.cv.setContent(file.getContents());
	}

}
