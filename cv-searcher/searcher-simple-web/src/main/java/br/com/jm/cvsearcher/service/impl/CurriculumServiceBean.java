package br.com.jm.cvsearcher.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import br.com.jm.cvsearcher.model.Curriculum;
import br.com.jm.cvsearcher.service.CurriculumException;
import br.com.jm.cvsearcher.service.CurriculumService;
import br.com.jm.cvsearcher.util.Constants;

/**
 * Implementa��o EJB de CurriculumService.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Stateless
@Local(CurriculumService.class)
public class CurriculumServiceBean extends AbstractLuceneBean implements
    CurriculumService
{

  /**
   * {@inheritDoc}
   * 
   * Salva o arquivo na pasta de armazenamento de arquivos.
   * 
   * O arquivo do curr�culo � salvao da seguinte forma:
   * <ol>
   * <li>A primeira linha cont�m o nome do candidato</li>
   * <li>A segunda linha cont�m o email do candidato</li>
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

    // Verifica se o curr�culo n�o est� nulo
    if (cv == null) { throw new CurriculumException("Curriculum cannot be null"); }

    // Salva o arquivo em disco
    PrintWriter writer = null;
    try {
      // Abre o arquivo
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
    IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_35,
        analyzer);

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
    IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_35,
        analyzer);

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
  private Document createDocument(Curriculum cv, File file) throws IOException {
    // Criar o documento
    Document doc = new Document();

    // Adicionar o campo nome
    doc.add(new Field(Constants.FIELD_NAME, cv.getName(), Store.YES,
        Index.NOT_ANALYZED_NO_NORMS));
    // Adicionar o campo email
    doc.add(new Field(Constants.FIELD_EMAIL, cv.getEmail(), Store.YES,
        Index.NOT_ANALYZED_NO_NORMS));
    // Adicionar o campo file
    doc.add(new Field(Constants.FIELD_FILE, file.getName(), Store.YES,
        Index.NOT_ANALYZED_NO_NORMS));
    // Adicionar o conte�do do curr�culo
    doc.add(new Field(Constants.FIELD_CONTENT, cv.getContent(), Store.NO,
        Index.ANALYZED));

    return doc;
  }

}
