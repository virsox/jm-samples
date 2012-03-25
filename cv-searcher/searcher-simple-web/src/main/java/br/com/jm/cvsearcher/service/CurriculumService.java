package br.com.jm.cvsearcher.service;

import java.io.File;

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
