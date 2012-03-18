package br.com.jm.cvsearcher.service;

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
	 * Adiciona um novo curr�culo.
	 * 
	 * @param cv
	 *            Curr�culo a ser adicionado
	 * @return true se o curr�culo foi adicionado com sucesso.
	 */
	public boolean addCurriculum(Curriculum cv);

	/**
	 * Efetua a busca de curr�culos baseado no nome do candidato.
	 * 
	 * @param name
	 *            Parte do nome do candidato
	 * @return Lista de resultados da busca
	 */
	public List<Curriculum> findCVByName(String name);

	/**
	 * Efetua a busca de curr�culos baseado no conte�do do curr�culo.
	 * 
	 * @param content
	 *            Palavra chave a ser buscado no conta�do do curr�culo
	 * @return Lista de resultados da busca
	 */
	public List<Curriculum> findCVByContent(String content);

	/**
	 * Indexa um curr�culo espec�fico.
	 * 
	 * @param cv
	 *            Curr�culo a ser indexado
	 */
	public void index(Curriculum cv);

	/**
	 * Indexa todos os curr�culos existentes.
	 * 
	 * O �ndice existente � completamente substitu�do por um novo.
	 */
	public void indexAll();
}
