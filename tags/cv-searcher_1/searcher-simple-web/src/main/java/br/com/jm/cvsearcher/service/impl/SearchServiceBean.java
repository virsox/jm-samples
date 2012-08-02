package br.com.jm.cvsearcher.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.lucene.analysis.TokenStream;
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
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
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
      throws CurriculumException
  {
    Query q = new TermQuery(new Term(Constants.FIELD_NAME, name));
    return executeQuery(q, Constants.FIELD_NAME);
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
    }
    catch (ParseException pex) {
      throw new CurriculumException("Erro no parse da consulta.", pex);
    }

    return executeQuery(q, Constants.FIELD_CONTENT);
  }

  /**
   * Executa a busca a partir do objeto Query passado como parâmetro.
   * 
   * 
   * @param q
   *            Consulta a ser executada
   * @param field
   *            O nome do campo onde a busca está sendo feita
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
  private List<SearchResult> executeQuery(Query q, String field)
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

      // QueryScorer para pontuar os termos no resultado
      QueryScorer scorer = new QueryScorer(q);
      
      // Fragmenter para separar os termos
      Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
                            //new SimpleFragmenter()

      // Formatter para destacar o resultado
      Formatter formatter = new SimpleHTMLFormatter(
          "<span class=\"highlight\">", "</span>");
      
      // Hightlighter para poder marcar os termos encontrados
      Highlighter highlighter = new Highlighter(formatter, scorer);
      highlighter.setTextFragmenter(fragmenter);

      // Para cada item encontrado, recrie o currículo e adiciona na lista
      // de resultados
      for (int i = 0; i < hits.length; ++i) {
        // Obtém o documento a partir de seu ID
        int docId = hits[i].doc;
        Document d = searcher.doc(docId);

        // Recupera o currículo do disco
        File f = new File(config.getFilesDirectory(),
            d.get(Constants.FIELD_FILE));
        Curriculum cv = loadCurriculum(f);

        // Obtém os tokens do campo conteúdo
        TokenStream tokenStream = TokenSources.getAnyTokenStream(reader, docId,
            Constants.FIELD_CONTENT, analyzer);
        // analyzer.tokenStream(field, new
        // StringReader(cv.getContent()));

        // Executa o highlighting e armazena o melhor fragmento
        String bestFragment = highlighter.getBestFragment(tokenStream,
            cv.getContent());

        // Cria objeto encapsulando resultado da busca
        SearchResult result = new SearchResult(cv, hits[i].score, bestFragment);
        results.add(result);
      }
    }
    catch (CorruptIndexException e) {
      throw new CurriculumException("Erro ao abrir IndexReader.", e);
    }
    catch (IOException e) {
      throw new CurriculumException("Erro ao abrir arquivo.", e);
    }
    catch (InvalidTokenOffsetsException e) {
      throw new CurriculumException("Erro ao destacar os termos no resultado.",
          e);
    }

    return results;
  }

}
