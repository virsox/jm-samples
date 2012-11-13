package br.com.jm.cvsearcher.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.SearchService;

/**
 * Implementação EJB da interface {@link SearchService}.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Stateless
@Local(SearchService.class)
public class SearchServiceBean implements SearchService, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2946540838935661231L;

  /** Entity manager. */
  @PersistenceContext(name = "default")
  protected EntityManager entityManager;

  /**
   * {@inheritDoc}
   * 
   * Utiliza a implementação do Hibernate Search para buscar os currículos.
   * 
   * @see FullTextEntityManager
   * @see Search
   * @see Query
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Curriculum> findCVByName(String name) throws CurriculumException {
    FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
        .getFullTextEntityManager(entityManager);

    // cria uma Query através da DSL para consultas
    QueryBuilder qb = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(Curriculum.class).get();

    org.apache.lucene.search.Query query = qb
        .keyword()
        .onFields("name")
        .matching(name)
        .createQuery();

    // encapsula a query Lucene query em um javax.persistence.Query
    javax.persistence.Query persistenceQuery = fullTextEntityManager
        .createFullTextQuery(query, Curriculum.class);

    // executa a busca
    return persistenceQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   * 
   * Utiliza a implementação do Hibernate Search para buscar os currículos.
   * 
   * @see FullTextEntityManager
   * @see Search
   * @see Query
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Curriculum> findCVByContent(String content)
      throws CurriculumException
  {

    FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
        .getFullTextEntityManager(entityManager);

    // cria uma Query através da DSL para consultas
    QueryBuilder qb = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(Curriculum.class).get();

    org.apache.lucene.search.Query query = qb
        .keyword()
        .onFields("content")
        .ignoreFieldBridge()
        .matching(content)
        .createQuery();

    // encapsula a query Lucene query em um javax.persistence.Query
    javax.persistence.Query persistenceQuery = fullTextEntityManager
        .createFullTextQuery(query, Curriculum.class);

    // executa a busca
    return persistenceQuery.getResultList();
  }

}
