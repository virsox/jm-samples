package br.com.jm.cvsearcher.service;

import java.util.List;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Interface de serviço contendo os métodos necessários busca de currículos.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public interface SearchService {

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

}
