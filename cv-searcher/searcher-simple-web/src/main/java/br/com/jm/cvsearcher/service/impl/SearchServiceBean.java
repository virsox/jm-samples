package br.com.jm.cvsearcher.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

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

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.SearchService;
import br.com.jm.cvsearcher.util.Constants;
import br.com.jm.cvsearcher.web.ConfigBean;

/**
 * Implementa��o da interface {@link SearchService}.
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

	private Analyzer analyzer;
	private Directory dir;
	@Inject
	private ConfigBean config;

	/**
	 * M�todo de inicializa��o do bean. Inicializa o Analyzer e o Directory do
	 * Engine Lucene
	 * 
	 * @see Analyzer
	 * @see StandardAnalyzer
	 * @see Directory
	 * @see FSDirectory
	 */
	@PostConstruct
	public void doInit() {
		// Inicializar o Analyzer
		analyzer = new StandardAnalyzer(Version.LUCENE_35);

		// Inicializa o Directory
		try {
			dir = FSDirectory.open(new File(config.getIndexDirectory()));

		} catch (IOException e) {
			// Caso aconte�a algum erro, lance uma RuntimeException
			throw new RuntimeException(
					"Erro ao abrir o diret�rio de indexa��o", e);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * Salva o arquivo na pasta de armazenamento de arquivos.
	 * 
	 * O arquivo do curr�culo � salvao da seguinte forma:
	 * <ol>
	 * <li>A primeira linha cont�m o nome do candidato</li>
	 * <li>A segunda linha cont�m o email do candidati</li>
	 * <li>A terceira linha em dianta, cont�m o conte�do do curr�culo</li>
	 * </ol>
	 * 
	 * O nome do arquivo � formado pela quantidade de arquivos na pasta de
	 * armazenamento + 1, adicionado a extens�o '.txt'.
	 * 
	 * Ap�s armazenar o arquivo em disco, o engine de indexa��o � chamado para
	 * indexar o novo curr�culo.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 * @see #getNextId()
	 * @see #index(Curriculum, File)
	 */
	@Override
	public void addCurriculum(Curriculum cv) throws CurriculumException {
		File file = null;

		// Verifica se o currpiculo n�o est� nulo
		if (cv == null) {
			throw new CurriculumException("Curriculum cannot be null");
		}

		// Salvar o arquivo em disco
		PrintWriter writer = null;
		try {
			// Abreo arquivo
			file = new File(config.getFilesDirectory(), getNextId() + ".txt");
			writer = new PrintWriter(new FileWriter(file));

			// Escreve os dados no arquivo
			writer.println(cv.getName());
			writer.println(cv.getEmail());
			writer.println(cv.getContent());

			// Chama a indexa��o
			this.index(cv, file);
		} catch (IOException e) {
			// Caso ocorra algum erro, lan�ar uma exception
			throw new CurriculumException("Erro ao salvar arquivo.", e);
		} finally {
			// FInalmente, fechar o arquivo
			if (writer != null) {
				writer.close();
			}
		}

	}

	/**
	 * @return Retorna a quantidade de arquivos existentes na pasta de
	 *         armazenamento de curr�culos, mais um.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 */
	private int getNextId() {
		// Cria um File apontando para a pasta de armazenamento de curr�culos
		File file = new File(config.getFilesDirectory());

		// Verifica se a pasta existe
		if (file.exists()) {
			// Obt�m a quantidade de arquivos mais um
			return (file.list().length + 1);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see #findCVByField(String, String)
	 * @see Constants#FIELD_NAME
	 */
	@Override
	public List<Curriculum> findCVByName(String name)
			throws CurriculumException {
		return findCVByField(name, Constants.FIELD_NAME);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see #findCVByField(String, String)
	 * @see Constants#FIELD_CONTENT
	 */
	@Override
	public List<Curriculum> findCVByContent(String content)
			throws CurriculumException {
		return findCVByField(content, Constants.FIELD_CONTENT);
	}

	/**
	 * Executa a busca por <TT>content</TT> no campo <TT>field</TT> no �ndice.
	 * 
	 * 
	 * @param content
	 *            conte�do a ser procurado
	 * @param field
	 *            nome do campo onde ser� feita a busca
	 * @return uma lista com os curr�culos encontrados
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
	private List<Curriculum> findCVByField(String content, String field)
			throws CurriculumException {
		// Inicializa a lista de resultados
		List<Curriculum> results = new ArrayList<Curriculum>();

		try {
			// Cria um objeto Query para efetuar a busca
			Query q = new QueryParser(Version.LUCENE_35, field, analyzer)
					.parse(content);

			// Quantidade de hits por p�gina
			int hitsPerPage = 10;
			// Abre o �ndice
			IndexReader reader = IndexReader.open(dir);
			// Cria um IndexSearcher para o �ndice
			IndexSearcher searcher = new IndexSearcher(reader);
			// Cria um Collector para a busca
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					hitsPerPage, true);
			// Executa a busca
			searcher.search(q, collector);
			// Obt�m o resultado da busca
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// Para cada item encontrado, recrie o curr�culo e adiciona na lista
			// de resultados
			for (int i = 0; i < hits.length; ++i) {
				// Obt�m o ID do documento encontrado
				int docId = hits[i].doc;
				// Obt�m a inst�ncia do documento pelo ID
				Document d = searcher.doc(docId);
				// Cria um file para recuperar o curr�culo do disco
				File f = new File(config.getFilesDirectory(),
						d.get(Constants.FIELD_FILE));
				// Carrega o curr�culo
				Curriculum cv = loadCurriculum(f);
				// Armazena o score da busca no curr�culo TODO Manter?
				cv.setScore(hits[i].score);
				// Adiciona o curr�culo nos resultados
				results.add(cv);
			}
		} catch (ParseException e) {
			throw new CurriculumException("Erro ao criar QueryParser.", e);
		} catch (CorruptIndexException e) {
			throw new CurriculumException("Erro ao abrir IndexReader.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao abrir arquivo.", e);
		}

		return results;
	}

	/**
	 * Carrega o conte�do do curr�culo que est� armazenado em disco
	 * 
	 * @param file
	 *            File do curr�culo a ser carregado
	 * @return uma inst�ncia de {@link Curriculum} com os seus atributos
	 *         preenchidos
	 * @throws IOException
	 * 
	 * @see FileReader
	 * @see BufferedReader
	 */
	private Curriculum loadCurriculum(File file) throws IOException {
		// Cria uma inst�ncia de curr�culo
		Curriculum cv = new Curriculum();
		// Cria um FileReader e um BufferedReader para ler o arquivo
		FileReader fr = new FileReader(file);
		BufferedReader reader = new BufferedReader(fr);
		// StringBuilder para carregar os dados do conte�do
		StringBuilder builder = new StringBuilder();
		String s = null;

		// Nome
		cv.setName(reader.readLine());
		// Email
		cv.setEmail(reader.readLine());
		// Conteudo
		while ((s = reader.readLine()) != null) {
			builder.append(s);
		}
		// Fecha o FileReader e o BufferedReader
		reader.close();
		fr.close();
		cv.setContent(builder.toString());

		return cv;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Procura por todos os arquivos na pasta de armazenamento de curr�culos e
	 * indexa o arquivo
	 * 
	 * @see #loadCurriculum(File)
	 * @see ConfigBean#getFilesDirectory()
	 * @see ConfigBean#getFiles
	 * 
	 * @see Directory
	 * @see Analyzer
	 * @see IndexWriterConfig
	 * @see IndexWriter
	 * @see Document
	 */
	@Override
	public void indexAll() throws CurriculumException {
		Curriculum cv;

		// Obt�m os arquivos da pasta de armazenamento de curr�culos
		File file = new File(config.getFilesDirectory());
		File files[] = file.listFiles(new FilenameFilter() {

			/**
			 * Filtra os arquivos com extens�o '.txt'
			 */
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});

		// Cria o IndexWriterConfig
		IndexWriterConfig indexConfig = new IndexWriterConfig(
				Version.LUCENE_35, analyzer);

		// Cria o IndexWriter
		IndexWriter w = null;
		try {
			w = new IndexWriter(dir, indexConfig);
		} catch (CorruptIndexException e) {
			throw new CurriculumException("�ndice corrompido.", e);
		} catch (LockObtainFailedException e) {
			throw new CurriculumException("�ndice j� est� sendo utilizado.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar �ndice.", e);
		}

		// Para cada arquivo encontrado, carregue o curr�culo e adicione no
		// indexador
		for (File f : files) {

			try {
				// Carrega o curr�culo
				cv = this.loadCurriculum(f);
				// Adiciona no indexador
				w.addDocument(createDocument(cv, f));
			} catch (CorruptIndexException e) {
				throw new CurriculumException("�ndice corrompido.", e);
			} catch (IOException e) {
				throw new CurriculumException("Erro ao atualizar �ndice.", e);
			}

		}
		// Fecha o indexador
		try {
			w.close();
		} catch (CorruptIndexException e) {
			throw new CurriculumException("�ndice corrompido.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar �ndice.", e);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Directory
	 * @see Analyzer
	 * @see IndexWriterConfig
	 * @see IndexWriter
	 * @see Document
	 */
	@Override
	public void index(Curriculum cv, File file) throws CurriculumException {

		// Cria o IndexWriterConfig
		IndexWriterConfig indexConfig = new IndexWriterConfig(
				Version.LUCENE_35, analyzer);

		// Cria o IndexWriter
		IndexWriter w = null;
		try {
			w = new IndexWriter(dir, indexConfig);
			// Adiciona no indexador
			w.addDocument(createDocument(cv, file));
			// Fechar o indexador
			w.close();

		} catch (CorruptIndexException e) {
			throw new CurriculumException("�ndice corrompido.", e);
		} catch (LockObtainFailedException e) {
			throw new CurriculumException("�ndice j� est� sendo utilizado.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar �ndice.", e);
		}

	}

	/**
	 * Cria um {@link Document} baseado nas informa��es do curr�culo e do
	 * arquivo onde ele est� armazenado.
	 * 
	 * @param cv
	 *            Curr�culo a ser indexado
	 * @param file
	 *            Arquivo onde o curr�culo est� armazenado
	 * @return Uma inst�ncia de Document com os dados do curr�culo
	 * @throws IOException
	 * 
	 * @see Document
	 * @See Field
	 * @See Index
	 */
	private Document createDocument(Curriculum cv, File file)
			throws IOException {
		// Criar o documento
		Document doc = new Document();

		// TODO Verificar os tipos de indexacao
		// Adicionar o campo nome
		doc.add(new Field(Constants.FIELD_NAME, cv.getName(), Store.YES,
				Index.ANALYZED_NO_NORMS));
		// Adicionar o campo email
		doc.add(new Field(Constants.FIELD_EMAIL, cv.getEmail(), Store.YES,
				Index.NOT_ANALYZED_NO_NORMS));
		// Adicionar o campo file
		doc.add(new Field(Constants.FIELD_FILE, file.getName(), Store.YES,
				Index.ANALYZED));
		// Adicionar o conte�do do curr�culo
		doc.add(new Field(Constants.FIELD_CONTENT, cv.getContent(), Store.NO,
				Index.ANALYZED));

		return doc;
	}
}
