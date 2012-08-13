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
 * Bean CDI para adição de novos currículos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
@RequestScoped
@Named
public class AddBean {
	/** Currículo a ser adicionado. */
	private Curriculum cv;

	/** Objeto para receber o arquivo com o currículo do candidato */
	private UploadedFile file;

	/** Bean de serviço. Injetado pelo container. */
	@Inject
	private CurriculumService service;

	/**
	 * Construtor padrão. Cria uma nova instância de Curriculum
	 */
	public AddBean() {
		this.cv = new Curriculum();
	}

	/**
	 * @return Retorna a instância de Curriculum
	 */
	public Curriculum getCv() {
		return cv;
	}

	/**
	 * Adiciona e indexa um novo currículo.
	 * 
	 * @return {@link Constants#MAIN} se o currículo foi adicionado corretamente
	 *         ou {@link Constants#ADD_CV} caso ocorra algum erro
	 */
	public String addCurriculum() {
		String r;
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			// Chama o serviço para adicionar e indexar o currículo
			service.addCurriculum(cv);
			// Usuário será redirecionado para a página principal
			r = Constants.MAIN;
			// Adicionar uma mensagem de sucesso
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Sucesso",
					"Currículo adicionado com sucesso."));

		} catch (CurriculumException e) {
			// Algum erro aconteceu ao inserir o currículo.
			// Redirecionar o usuario para a página de adição
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
	 * Ao receber o arquivo enviado pelo formulário, ja extrai o conteúdo do
	 * arquivo e adiciona no currículo
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
