package br.com.jm.cvsearcher.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import br.com.jm.cvsearcher.web.ConfigBean;
import br.com.jm.cvsearcher.web.ResultBean;

@Named
@Stateful
public class SearchBean {
	private Analyzer analyzer;
	private Directory dir = null;
	@Inject
	private ConfigBean config;

	@PostActivate
	@PostConstruct
	public void doInit() {
		// Inicializar o Analyzer
		analyzer = new StandardAnalyzer(Version.LUCENE_35);
		
		try {
			dir = FSDirectory.open(new File(config.getIndexDirectory()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public List<ResultBean> simpleSearch(String keyword) {
		List<ResultBean> results = new ArrayList<ResultBean>();
		
		try {
			Query q = new QueryParser(Version.LUCENE_35, "description", analyzer).parse(keyword);
			
			int hitsPerPage = 10;
			IndexReader reader = IndexReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0;i<hits.length;++i) {
			    int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			    ResultBean result = new ResultBean();
			    result.setName(d.get("nome"));
			    result.setEmail(d.get("email"));
			    result.setResult(d.get("description"));
			    results.add(result);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}

	public String createIndex() {


		IndexWriterConfig indexConfig = new IndexWriterConfig(
				Version.LUCENE_35, analyzer);

		IndexWriter w = null;
		try {
			w = new IndexWriter(dir, indexConfig);

			File cvsDir = new File(config.getFilesDirectory());
			for (File file : cvsDir.listFiles()) {

				BufferedReader reader = new BufferedReader(new FileReader(file));
				createDocument(reader, w);

				reader.close();

			}

			w.close();

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "indexer";
	}

	private void createDocument(BufferedReader reader, IndexWriter writer)
			throws IOException {
		Document doc = new Document();

		String name = reader.readLine();
		doc.add(new Field("name", name, Store.YES, Index.NOT_ANALYZED_NO_NORMS));

		String email = reader.readLine();
		doc.add(new Field("email", email, Store.YES,
				Index.NOT_ANALYZED_NO_NORMS));

		StringBuilder builder = new StringBuilder();
		String s = null;
		while ((s = reader.readLine()) != null) {
			builder.append(s);
		}

		doc.add(new Field("description", builder.toString(), Store.NO,
				Index.ANALYZED));

		writer.addDocument(doc);
	}
}
