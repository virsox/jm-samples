package br.com.jm.cvsearcher.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.CurriculumService;

/**
 * Implementa��o EJB de CurriculumService.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Stateless
@Local(CurriculumService.class)
public class CurriculumServiceBean implements CurriculumService {

  /** Entity manager. */
  @PersistenceContext(name = "default")
  protected EntityManager entityManager;

  /**
   * {@inheritDoc}
   * 
   * Salva o arquivo no banco de dados utilizando o EntityManager
   * 
   * @see EntityManager#persist(Object)
   * 
   */
  @Override
  public void addCurriculum(Curriculum cv) throws CurriculumException {

    // Verifica se o curr�culo n�o est� nulo
    if (cv == null) { throw new CurriculumException("Curriculum cannot be null"); }

    this.entityManager.persist(cv);

  }

}
