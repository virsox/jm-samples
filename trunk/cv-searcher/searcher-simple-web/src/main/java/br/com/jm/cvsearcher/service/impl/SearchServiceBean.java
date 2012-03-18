package br.com.jm.cvsearcher.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.inject.Inject;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.SearchService;
import br.com.jm.cvsearcher.web.ConfigBean;

// TODO Verificar se tem que ser Statefull ou Stateless
@Stateful
@Local(SearchService.class)
public class SearchServiceBean implements SearchService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2946540838935661231L;

	@Inject
	private ConfigBean config;

	private static int ID = 1;

	// TODO REmover
	private static List<Curriculum> fakeResults;

	static {
		fakeResults = new ArrayList<Curriculum>();

		for (int i = 0; i < 100; i++) {
			fakeResults.add(new Curriculum("Name "+i, "email"+i+"@bol.com.br",
					"Content"+i));
		}
	}

	@Override
	public boolean addCurriculum(Curriculum cv) {
		boolean b = true;

		if (cv == null) {
			throw new IllegalArgumentException("Curriculum cannot be null");
		}
		/*
		 * PrintWriter writer = null;
		 * 
		 * try { writer = new PrintWriter(new
		 * FileWriter(config.getFilesDirectory() + (ID++) + ".txt"));
		 * 
		 * writer.println(cv.getName()); writer.println(cv.getEmail());
		 * writer.println(cv.getContent());
		 * 
		 * } catch (IOException e) { // FIXME e.printStackTrace(); b = false;
		 * 
		 * } finally { if (writer != null) { writer.close(); } }
		 */

		System.out.println("Curriculo adicionado:");
		System.out.println("\tNome: " + cv.getName());
		return b;
	}

	@Override
	public List<Curriculum> findCVByName(String name) {
		// TODO Auto-generated method stub
		return fakeResults;
	}

	@Override
	public List<Curriculum> findCVByContent(String content) {
		// TODO Auto-generated method stub
		return fakeResults;
	}

	@Override
	public void indexAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void index(Curriculum cv) {
		// TODO Auto-generated method stub

	}

}
