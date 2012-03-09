package br.com.jm.cvsearcher.web;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigBean {

	public String getFilesDirectory() {
		return "/Users/virso/tmp/cv-searcher/files/";
	}
	
	public String getIndexDirectory() {
		return "/Users/virso/tmp/cv-searcher/index/";
	}
	
}
