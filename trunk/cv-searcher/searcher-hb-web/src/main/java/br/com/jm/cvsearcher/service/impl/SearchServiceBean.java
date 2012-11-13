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
 * Implementação EJB da interface {@link SearchService}.
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
   */
  @Override
  public List<Curriculum> findCVByName(String name)
      throws CurriculumException
  {
    return executeFullTextQuery("name", name);
  }

  /**
   * {@inheritDoc}
   * 
   * @see #executeQuery(Query)
   */
  @Override
  public List<Curriculum> findCVByContent(String content)
      throws CurriculumException
  { 
    return executeFullTextQuery("content", content);
  }

  /**
   * Executa consulta "full text" no campo informado.
   * 
   * @param field Campo onde a busca será feita.
   * @param value Valor buscado.
   * @return Lista de currículos que satisfazem o filtro.
   */
  @SuppressWarnings("unchecked")
  private List<Curriculum> executeFullTextQuery(String field, String value) {
    FullTextEntityManager fullTextEntityManager = 
        org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

    // cria uma Query através da DSL para consultas
    QueryBuilder qb = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(Curriculum.class ).get();
    
    org.apache.lucene.search.Query query = qb
      .keyword()
      .onFields(field)
      .matching(value)
      .createQuery();

    // encapsula a query Lucene query em um javax.persistence.Query
    javax.persistence.Query persistenceQuery = 
        fullTextEntityManager.createFullTextQuery(query, Curriculum.class);

    // executa a busca
    return persistenceQuery.getResultList();
  }

}
