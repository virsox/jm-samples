package br.com.jm.cvsearcher.web;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigBean {

	public String getFilesDirectory() {
		return "c:/Temp/files/";
	}
	
	public String getIndexDirectory() {
		return "c:/Temp/index/";
	}
	
}
