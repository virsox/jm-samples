package br.com.jm.cvsearcher.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Classe abstrata contendo atributos e métodos comuns às classes que
 * utilizam o Lucene.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 *
 */
public class AbstractLuceneBean {

  /**
   * Objeto responsável por executar a análise dos documentos que são
   * adicionados ao índice.
   */
  protected Analyzer analyzer;
  
  /** Local onde o índice é armazenado. */
  protected Directory dir;
  
  /** Bean de configuração. */
  @Inject
  protected ConfigBean config;

  public AbstractLuceneBean() {
    super();
  }

  /**
   * Método de inicialização. Inicializa o Analyzer e o Directory do
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

    }
    catch (IOException e) {
      // Caso aconteça algum erro, lance uma RuntimeException
      throw new RuntimeException("Erro ao abrir o diretório de indexação", e);
    }

  }

  /**
   * Carrega o conteúdo do currículo que está armazenado em disco em um
   * objeto do tipo Curriculum.
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
  protected Curriculum loadCurriculum(File file) throws IOException {
    // Cria uma instância de currículo
    Curriculum cv = new Curriculum();
    // Cria um FileInputStream e um BufferedReader para ler o arquivo
    FileInputStream fr = new FileInputStream(file);
    BufferedReader reader = new BufferedReader(new InputStreamReader(fr, "UTF-8"));
    // StringBuilder para carregar os dados do conteúdo
    StringBuilder builder = new StringBuilder();
    String s = null;

    // Nome
    cv.setName(reader.readLine());
    // Email
    cv.setEmail(reader.readLine());
    // Conteudo
    String newLine = System.getProperty("line.separator");
    while ((s = reader.readLine()) != null) {
      builder.append(s).append(newLine);
    }
    // Fecha o FileReader e o BufferedReader
    reader.close();
    fr.close();
    cv.setContent(builder.toString());

    return cv;
  }
}