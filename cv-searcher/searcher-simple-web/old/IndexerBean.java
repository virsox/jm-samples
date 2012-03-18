package br.com.jm.cvsearcher.web;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.jm.cvsearcher.server.SearchBean;

@ApplicationScoped
@Named
public class IndexerBean {

	@Inject
	private SearchBean searchBean;
	
	public String createIndex() {
		searchBean.createIndex();
		
		return "indexer";
	}
	
	
	
	
}
