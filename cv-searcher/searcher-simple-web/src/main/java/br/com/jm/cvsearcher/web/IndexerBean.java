package br.com.jm.cvsearcher.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

@ApplicationScoped
@Named
public class IndexerBean {

	@Inject
	private ConfigBean config;
	
	public String createIndex() {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
		
		Directory dir = null;
		try {
			dir = FSDirectory.open(new File(config.getIndexDirectory()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_35,
				analyzer);
		
		IndexWriter w = null;
		try {
			w = new IndexWriter(dir, indexConfig);
			
			File cvsDir = new File(config.getFilesDirectory());
			for (File file : cvsDir.listFiles()) {
				
				BufferedReader reader = new BufferedReader(new FileReader(
						file));
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
	
	private void createDocument(BufferedReader reader, IndexWriter writer) throws IOException {
		Document doc = new Document();		
		
		String name = reader.readLine();
		doc.add(new Field("name", name, Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		
		String email = reader.readLine();
		doc.add(new Field("email", email, Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		
		StringBuilder builder = new StringBuilder();
		String s = null;
		while ((s = reader.readLine()) != null) {
			builder.append(s);
		}
		
		doc.add(new Field("description", builder.toString(), Store.NO, Index.ANALYZED));
		

		writer.addDocument(doc);
	}
	
	
	
}
