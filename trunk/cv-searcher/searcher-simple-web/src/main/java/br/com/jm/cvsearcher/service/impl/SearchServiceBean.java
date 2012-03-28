package br.com.jm.cvsearcher.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.model.SearchResult;
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
public class SearchServiceBean extends AbstractLuceneBean implements
    SearchService, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 2946540838935661231L;

  /**
   * {@inheritDoc}
   * 
   * @see #executeQuery(Query)
   * @see Constants#FIELD_NAME
   */
  @Override
  public List<SearchResult> findCVByName(String name)
      throws CurriculumException {
    Query q = new TermQuery(new Term(Constants.FIELD_NAME, name));
    return executeQuery(q);
  }

  /**
   * {@inheritDoc}
   * 
   * @see #executeQuery(Query)
   * @see Constants#FIELD_CONTENT
   */
  @Override
  public List<SearchResult> findCVByContent(String content)
      throws CurriculumException
  {
    // Cria um objeto Query para efetuar a busca
    Query q = null;
    try {
      q = new QueryParser(Version.LUCENE_35, Constants.FIELD_CONTENT, analyzer)
          .parse(content);
    } catch (ParseException pex) {
      throw new CurriculumException("Erro no parse da consulta.", pex);
    }

    return executeQuery(q);
  }

  /**
   * Executa a busca a partir do objeto Query passado como parâmetro.
   * 
   * 
   * @param q Consulta a ser executada
   * @throws CurriculumException
   * 
   * @see #findCVByContent(String)
   * @see #findCVByName(String)
   * @see #loadCurriculum(File)
   * 
   * @see Query
   * @see QueryParser
   * @see IndexReader
   * @see IndexSearcher
   * @see TopScoreDocCollector
   * @see ScoreDoc
   * @see Document
   */
  private List<SearchResult> executeQuery(Query q)
      throws CurriculumException
  {
    // Inicializa a lista de resultados
    List<SearchResult> results = new ArrayList<SearchResult>();

    try {

      // Quantidade de hits por página
      int hitsPerPage = 30;
    
      // Abre o índice e cria um IndexSearcher
      IndexReader reader = IndexReader.open(dir);
      IndexSearcher searcher = new IndexSearcher(reader);
      
      // Executa a busca
      TopDocs topDocs = searcher.search(q, hitsPerPage);
      ScoreDoc[] hits = topDocs.scoreDocs;

      // Para cada item encontrado, recrie o currículo e adiciona na lista
      // de resultados
      for (int i = 0; i < hits.length; ++i) {
        // Obtém o ID do documento encontrado
        int docId = hits[i].doc;
        // Obtém a instância do documento pelo ID
        Document d = searcher.doc(docId);
        // Cria um file para recuperar o currículo do disco
        File f = new File(config.getFilesDirectory(),
            d.get(Constants.FIELD_FILE));
        // Carrega o currículo
        Curriculum cv = loadCurriculum(f);

        // Cria objeto encapsulando resultado da busca 
        SearchResult result = new SearchResult(cv, hits[i].score);
        // Adiciona na lista de resultados
        results.add(result);
      }
    }
    catch (CorruptIndexException e) {
      throw new CurriculumException("Erro ao abrir IndexReader.", e);
    }
    catch (IOException e) {
      throw new CurriculumException("Erro ao abrir arquivo.", e);
    }

    return results;
  }

}
