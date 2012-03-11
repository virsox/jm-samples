package br.com.jm.cvsearcher.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

@RequestScoped
@Named
public class SearchBean {

	private String keywords;
	private List<SearchResult> result;
	
	@Inject
	private ConfigBean config;
	
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public List<SearchResult> getResult() {
		return this.result;
	}
	
	
	
	public String search() {
		
		Directory dir = null;
		try {
			dir = FSDirectory.open(new File(config.getIndexDirectory()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		try {
			IndexReader reader = IndexReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			
			//StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			
			Query query = new TermQuery(new Term("description",
					this.getKeywords()));
			
			this.result = new ArrayList<SearchResult>();
			TopDocs hits = searcher.search(query, 10);
			
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(new SearchResult(doc.get("name"), doc.get("email")));				
			}
			
			//QueryParser parser = new QueryParser(Version.LUCENE_35,
			//		"description", analyzer);			
			//Query query = parser.parse(this.getKeywords());
			
			searcher.close();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "search";
	}
	
	
}
