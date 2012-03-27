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
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
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
   * @see #findCVByField(String, String)
   * @see Constants#FIELD_NAME
   */
  @Override
  public List<SearchResult> findCVByName(String name)
      throws CurriculumException
  {
    return findCVByField(name, Constants.FIELD_NAME);
  }

  /**
   * {@inheritDoc}
   * 
   * @see #findCVByField(String, String)
   * @see Constants#FIELD_CONTENT
   */
  @Override
  public List<SearchResult> findCVByContent(String content)
      throws CurriculumException
  {
    return findCVByField(content, Constants.FIELD_CONTENT);
  }

  /**
   * Executa a busca por <TT>content</TT> no campo <TT>field</TT> no índice.
   * 
   * 
   * @param content
   *            conteúdo a ser procurado
   * @param field
   *            nome do campo onde será feita a busca
   * @return uma lista com os currículos encontrados
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
  private List<SearchResult> findCVByField(String content, String field)
      throws CurriculumException
  {
    // Inicializa a lista de resultados
    List<SearchResult> results = new ArrayList<SearchResult>();

    try {
      // Cria um objeto Query para efetuar a busca
      Query q = new QueryParser(Version.LUCENE_35, field, analyzer)
          .parse(content);

      // Quantidade de hits por página
      int hitsPerPage = 10;
      // Abre o índice
      IndexReader reader = IndexReader.open(dir);
      // Cria um IndexSearcher para o índice
      IndexSearcher searcher = new IndexSearcher(reader);
      // Cria um Collector para a busca
      TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage,
          true);
      // Executa a busca
      searcher.search(q, collector);
      // Obtém o resultado da busca
      ScoreDoc[] hits = collector.topDocs().scoreDocs;

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
    catch (ParseException e) {
      throw new CurriculumException("Erro ao criar QueryParser.", e);
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
