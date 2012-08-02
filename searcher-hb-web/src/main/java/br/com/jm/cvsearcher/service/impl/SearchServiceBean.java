package br.com.jm.cvsearcher.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.SearchService;
import br.com.jm.cvsearcher.util.Constants;

/**
 * Implementação da interface {@link SearchService}.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Stateless
@Local(SearchService.class)
public class SearchServiceBean implements SearchService, Serializable
{
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
   * @see #executeQuery(Query)
   * @see Constants#FIELD_NAME
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Curriculum> findCVByName(String name)
      throws CurriculumException
  {
    FullTextEntityManager fullTextEntityManager = 
        org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

    // create native Lucene query unsing the query DSL
    // alternatively you can write the Lucene query using the Lucene query parser
    // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
    QueryBuilder qb = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(Curriculum.class ).get();
    
    org.apache.lucene.search.Query query = qb
      .keyword()
      .onFields("name")
      .matching(name)
      .createQuery();

    // wrap Lucene query in a javax.persistence.Query
    javax.persistence.Query persistenceQuery = 
        fullTextEntityManager.createFullTextQuery(query, Curriculum.class);

    // execute search
    return persistenceQuery.getResultList();
  }

  /**
   * {@inheritDoc}
   * 
   * @see #executeQuery(Query)
   * @see Constants#FIELD_CONTENT
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Curriculum> findCVByContent(String content)
      throws CurriculumException
  {
    
    FullTextEntityManager fullTextEntityManager = 
        org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

    // create native Lucene query unsing the query DSL
    // alternatively you can write the Lucene query using the Lucene query parser
    // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
    QueryBuilder qb = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(Curriculum.class ).get();
    
    org.apache.lucene.search.Query query = qb
      .keyword()
      .onFields("content")
      .matching(content)
      .createQuery();

    // wrap Lucene query in a javax.persistence.Query
    javax.persistence.Query persistenceQuery = 
        fullTextEntityManager.createFullTextQuery(query, Curriculum.class);

    // execute search
    return persistenceQuery.getResultList();
  }

}
