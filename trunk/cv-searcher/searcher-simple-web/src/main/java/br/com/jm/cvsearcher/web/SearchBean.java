package br.com.jm.cvsearcher.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.SearchService;

@SessionScoped
@Named
public class SearchBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7942216607824066013L;

	public enum SearchType {
		NAME, CONTENT
	}

	private String keyword;
	private SearchType type;
	private List<Curriculum> results;

	public SearchBean() {
		this.results = Collections.emptyList();
		this.type = SearchType.NAME;
	}

	@Inject
	private SearchService service;

	public void search() {

		switch (type) {
		case NAME: {
			results = service.findCVByName(keyword);
		}
			;
			break;
		case CONTENT: {
			results = service.findCVByContent(keyword);
		}
			;
			break;
		default: {
			results = Collections.emptyList();
		}
			;
			break;
		}

	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public SearchType getType() {
		return type;
	}

	public void setType(SearchType type) {
		this.type = type;
	}

	public List<Curriculum> getResults() {
		return results;
	}

}
