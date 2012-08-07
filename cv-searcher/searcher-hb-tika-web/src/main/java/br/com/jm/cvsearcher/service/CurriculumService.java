package br.com.jm.cvsearcher.service;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de servi�o contendo m�todos para adi��o e indexa��o de curr�culos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
public interface CurriculumService {

	/**
	 * Adiciona e indexa um novo curr�culo.
	 * 
	 * @param cv
	 *            Curr�culo a ser adicionado
	 * 
	 * @throws CurriculumException
	 */
	public void addCurriculum(Curriculum cv) throws CurriculumException;

}
