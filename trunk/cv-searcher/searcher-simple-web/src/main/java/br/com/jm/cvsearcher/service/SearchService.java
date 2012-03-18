package br.com.jm.cvsearcher.service;

import java.io.File;
import java.util.List;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de serviço contendo os métodos necessários para adição, indexação e
 * busca de currículos
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
public interface SearchService {

	/**
	 * Adiciona e indexa um novo currículo.
	 * 
	 * @param cv
	 *            Currículo a ser adicionado
	 * 
	 * @throws CurriculumException
	 */
	public void addCurriculum(Curriculum cv) throws CurriculumException;

	/**
	 * Efetua a busca de currículos baseado no nome do candidato.
	 * 
	 * @param name
	 *            Parte do nome do candidato
	 * @return Lista de resultados da busca
	 * @throws CurriculumException
	 */
	public List<Curriculum> findCVByName(String name)
			throws CurriculumException;

	/**
	 * Efetua a busca de currículos baseado no conteúdo do currículo.
	 * 
	 * @param content
	 *            Palavra chave a ser buscado no contaúdo do currículo
	 * @return Lista de resultados da busca
	 * @throws CurriculumException
	 */
	public List<Curriculum> findCVByContent(String content)
			throws CurriculumException;

	/**
	 * Indexa um currículo específico.
	 * 
	 * @param cv
	 *            Currículo a ser indexado
	 * @param file
	 *            Caminho do arquivo que está sendo indexado
	 * @throws CurriculumException
	 */
	public void index(Curriculum cv, File file) throws CurriculumException;

	/**
	 * Reindexa todos os currículos existentes.
	 * 
	 * O índice existente é completamente substituído por um novo.
	 * 
	 * @throws CurriculumException
	 */
	public void indexAll() throws CurriculumException;
}
