package br.com.jm.cvsearcher.web;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.cvsearcher.server.SearchBean;

@ApplicationScoped
@Named
public class SimpleSearchBean {
	private String keyword;
	@Inject
	private SearchBean searchBean;

	public void search() {
		
		List<ResultBean> results = searchBean.simpleSearch(keyword);
		System.out.println("Tamanho: "+results.size());
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
}
