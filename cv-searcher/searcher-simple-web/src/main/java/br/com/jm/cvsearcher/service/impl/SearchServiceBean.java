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
 * Implementação da interface {@link SearchService}.
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
	 * Método de inicialização do bean. Inicializa o Analyzer e o Directory do
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
			// Caso aconteça algum erro, lance uma RuntimeException
			throw new RuntimeException(
					"Erro ao abrir o diretório de indexação", e);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * Salva o arquivo na pasta de armazenamento de arquivos.
	 * 
	 * O arquivo do currículo é salvao da seguinte forma:
	 * <ol>
	 * <li>A primeira linha contém o nome do candidato</li>
	 * <li>A segunda linha contém o email do candidati</li>
	 * <li>A terceira linha em dianta, contém o conteúdo do currículo</li>
	 * </ol>
	 * 
	 * O nome do arquivo é formado pela quantidade de arquivos na pasta de
	 * armazenamento + 1, adicionado a extensão '.txt'.
	 * 
	 * Após armazenar o arquivo em disco, o engine de indexação é chamado para
	 * indexar o novo currículo.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 * @see #getNextId()
	 * @see #index(Curriculum, File)
	 */
	@Override
	public void addCurriculum(Curriculum cv) throws CurriculumException {
		File file = null;

		// Verifica se o currpiculo não está nulo
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

			// Chama a indexação
			this.index(cv, file);
		} catch (IOException e) {
			// Caso ocorra algum erro, lançar uma exception
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
	 *         armazenamento de currículos, mais um.
	 * 
	 * @see ConfigBean#getFilesDirectory()
	 */
	private int getNextId() {
		// Cria um File apontando para a pasta de armazenamento de currículos
		File file = new File(config.getFilesDirectory());

		// Verifica se a pasta existe
		if (file.exists()) {
			// Obtém a quantidade de arquivos mais um
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
	private List<Curriculum> findCVByField(String content, String field)
			throws CurriculumException {
		// Inicializa a lista de resultados
		List<Curriculum> results = new ArrayList<Curriculum>();

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
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					hitsPerPage, true);
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
				// Armazena o score da busca no currículo TODO Manter?
				cv.setScore(hits[i].score);
				// Adiciona o currículo nos resultados
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
	 * Carrega o conteúdo do currículo que está armazenado em disco
	 * 
	 * @param file
	 *            File do currículo a ser carregado
	 * @return uma instância de {@link Curriculum} com os seus atributos
	 *         preenchidos
	 * @throws IOException
	 * 
	 * @see FileReader
	 * @see BufferedReader
	 */
	private Curriculum loadCurriculum(File file) throws IOException {
		// Cria uma instância de currículo
		Curriculum cv = new Curriculum();
		// Cria um FileReader e um BufferedReader para ler o arquivo
		FileReader fr = new FileReader(file);
		BufferedReader reader = new BufferedReader(fr);
		// StringBuilder para carregar os dados do conteúdo
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
	 * Procura por todos os arquivos na pasta de armazenamento de currículos e
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

		// Obtém os arquivos da pasta de armazenamento de currículos
		File file = new File(config.getFilesDirectory());
		File files[] = file.listFiles(new FilenameFilter() {

			/**
			 * Filtra os arquivos com extensão '.txt'
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
			throw new CurriculumException("Índice corrompido.", e);
		} catch (LockObtainFailedException e) {
			throw new CurriculumException("Índice já está sendo utilizado.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar índice.", e);
		}

		// Para cada arquivo encontrado, carregue o currículo e adicione no
		// indexador
		for (File f : files) {

			try {
				// Carrega o currículo
				cv = this.loadCurriculum(f);
				// Adiciona no indexador
				w.addDocument(createDocument(cv, f));
			} catch (CorruptIndexException e) {
				throw new CurriculumException("Índice corrompido.", e);
			} catch (IOException e) {
				throw new CurriculumException("Erro ao atualizar índice.", e);
			}

		}
		// Fecha o indexador
		try {
			w.close();
		} catch (CorruptIndexException e) {
			throw new CurriculumException("Índice corrompido.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar índice.", e);
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
			throw new CurriculumException("Índice corrompido.", e);
		} catch (LockObtainFailedException e) {
			throw new CurriculumException("Índice já está sendo utilizado.", e);
		} catch (IOException e) {
			throw new CurriculumException("Erro ao atualizar índice.", e);
		}

	}

	/**
	 * Cria um {@link Document} baseado nas informações do currículo e do
	 * arquivo onde ele está armazenado.
	 * 
	 * @param cv
	 *            Currículo a ser indexado
	 * @param file
	 *            Arquivo onde o currículo está armazenado
	 * @return Uma instância de Document com os dados do currículo
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
		// Adicionar o conteúdo do currículo
		doc.add(new Field(Constants.FIELD_CONTENT, cv.getContent(), Store.NO,
				Index.ANALYZED));

		return doc;
	}
}
