package br.com.jm.cvsearcher.service;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de serviço contendo métodos para adição e indexação de currículos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
public interface CurriculumService {

	/**
	 * Adiciona e indexa um novo currículo.
	 * 
	 * @param cv
	 *            Currículo a ser adicionado
	 * 
	 * @throws CurriculumException
	 */
	public void addCurriculum(Curriculum cv) throws CurriculumException;

}
