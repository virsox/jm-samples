package br.com.jm.cvsearcher.web;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.SearchService;
import br.com.jm.cvsearcher.util.Constants;

@RequestScoped
@Named
public class AddBean {
	private Curriculum cv;
	@Inject
	private SearchService service;

	public AddBean() {
		this.cv = new Curriculum();
	}

	public Curriculum getCv() {
		return cv;
	}

	public String addCurriculum() {
		boolean b;
		String r;
		FacesContext context = FacesContext.getCurrentInstance();

		b = service.addCurriculum(cv);

		
		if (b) {
			r = Constants.MAIN;
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Sucesso",
					"Currículo adicionado com sucesso."));
		} else {
			r = Constants.ADD_CV;
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro",
					"Erro ao adicionar currículo"));

		}

		return r;
	}
}
