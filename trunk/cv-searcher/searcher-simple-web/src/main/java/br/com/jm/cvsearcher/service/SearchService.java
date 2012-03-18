package br.com.jm.cvsearcher.service;

import java.io.File;
import java.util.List;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de servi�o contendo os m�todos necess�rios para adi��o, indexa��o e
 * busca de curr�culos
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
public interface SearchService {

	/**
	 * Adiciona e indexa um novo curr�culo.
	 * 
	 * @param cv
	 *            Curr�culo a ser adicionado
	 * 
	 * @throws CurriculumException
	 */
	public void addCurriculum(Curriculum cv) throws CurriculumException;

	/**
	 * Efetua a busca de curr�culos baseado no nome do candidato.
	 * 
	 * @param name
	 *            Parte do nome do candidato
	 * @return Lista de resultados da busca
	 * @throws CurriculumException
	 */
	public List<Curriculum> findCVByName(String name)
			throws CurriculumException;

	/**
	 * Efetua a busca de curr�culos baseado no conte�do do curr�culo.
	 * 
	 * @param content
	 *            Palavra chave a ser buscado no conta�do do curr�culo
	 * @return Lista de resultados da busca
	 * @throws CurriculumException
	 */
	public List<Curriculum> findCVByContent(String content)
			throws CurriculumException;

	/**
	 * Indexa um curr�culo espec�fico.
	 * 
	 * @param cv
	 *            Curr�culo a ser indexado
	 * @param file
	 *            Caminho do arquivo que est� sendo indexado
	 * @throws CurriculumException
	 */
	public void index(Curriculum cv, File file) throws CurriculumException;

	/**
	 * Reindexa todos os curr�culos existentes.
	 * 
	 * O �ndice existente � completamente substitu�do por um novo.
	 * 
	 * @throws CurriculumException
	 */
	public void indexAll() throws CurriculumException;
}
