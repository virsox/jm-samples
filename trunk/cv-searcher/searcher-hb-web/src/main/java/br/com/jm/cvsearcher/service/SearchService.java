package br.com.jm.cvsearcher.service;

import java.util.List;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de servi�o contendo os m�todos necess�rios busca de curr�culos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public interface SearchService {

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

}
