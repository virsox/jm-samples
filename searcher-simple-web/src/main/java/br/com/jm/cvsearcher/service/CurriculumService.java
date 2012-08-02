package br.com.jm.cvsearcher.service;

import java.io.File;

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
