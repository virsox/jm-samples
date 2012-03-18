package br.com.jm.cvsearcher.service;

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
	 * Adiciona um novo currículo.
	 * 
	 * @param cv
	 *            Currículo a ser adicionado
	 * @return true se o currículo foi adicionado com sucesso.
	 */
	public boolean addCurriculum(Curriculum cv);

	/**
	 * Efetua a busca de currículos baseado no nome do candidato.
	 * 
	 * @param name
	 *            Parte do nome do candidato
	 * @return Lista de resultados da busca
	 */
	public List<Curriculum> findCVByName(String name);

	/**
	 * Efetua a busca de currículos baseado no conteúdo do currículo.
	 * 
	 * @param content
	 *            Palavra chave a ser buscado no contaúdo do currículo
	 * @return Lista de resultados da busca
	 */
	public List<Curriculum> findCVByContent(String content);

	/**
	 * Indexa um currículo específico.
	 * 
	 * @param cv
	 *            Currículo a ser indexado
	 */
	public void index(Curriculum cv);

	/**
	 * Indexa todos os currículos existentes.
	 * 
	 * O índice existente é completamente substituído por um novo.
	 */
	public void indexAll();
}
