package br.com.jm.cvsearcher.bridge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.StringBridge;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Implementa��o de StringBridge para indexar conte�dos bin�rios.
 * 
 * Essa bridge pode ser parametrizada. Hoje, os seguintes formatos s�o suportados:
 * 
 * - DOC (Microsoft Word)
 * - DOCX (Microsoft Word - p�s 2007)
 * - PDF
 * 
 * A implementa��o extrai o conte�do do bin�rio atrav�s do Framework Tika e
 * retorna o conte�do em formato String para o indexador Lucene.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * 
 */
public class TikaBridge implements StringBridge, ParameterizedBridge {
  /**
   * Enum que define os tipos de documentos suportados
   */
  private enum SupportedTypes {
    DOC, DOCX, PDF
  }

  /**
   * Constante que define o nome da propriedade que ir� identificar o tipo de
   * documento que ser� decodificado.
   */
  private static final String TYPE_PROPERTY = "type";

  /** Tipo do documento configurado. */
  private SupportedTypes type;

  /** Log */
  private Log log = LogFactory.getLog(TikaBridge.class);

  /**
   * {@inheritDoc}
   * 
   * Dentre os decodificadores dispon�veis, verifica qual est� configurado e
   * executa o decodificaror.
   */
  @Override
  public String objectToString(Object object) {

    switch (type) {
      case DOC:
      case DOCX:
      case PDF:
        return index((byte[]) object);
      default:
        throw new RuntimeException("Tipo inv�lido configurado.");
    }
  }

  /**
   * {@inheritDoc}
   * 
   * Recebe o parametro configurado na anota��o e converte para um tipo
   * suportado.
   */
  @Override
  public void setParameterValues(Map<String, String> parameters) {
    type = SupportedTypes.valueOf(parameters.get(TYPE_PROPERTY));

  }

  /**
   * Converte os dados bin�rios para o formato String para poder ser indexado pelo
   * Lucene.
   * 
   * @param data
   *            Dados a serem decodificados
   * @return dados codificados no formato String. Caso ocorra algum erro, o
   *         retorna ser� uma String vazia.
   * 
   * @see Curriculum#getContent()
   * @see Parser
   */
  private String index(byte[] data) {

    InputStream inputStream = new ByteArrayInputStream(data);
    Parser parser = null;
    switch (type) {
      case DOC:
        parser = new OfficeParser();
        break;
      case DOCX:
        parser = new OOXMLParser();
        break;
      case PDF:
        parser = new PDFParser();
        break;
    }

    ContentHandler handler = new BodyContentHandler();
    ParseContext context = new ParseContext();
    Metadata metadata = new Metadata();

    try {
      parser.parse(inputStream, handler, metadata, context);
      inputStream.close();
    }
    catch (IOException e) {
      log.error("Erro lendo o arquivo.", e);
      return "";

    }
    catch (SAXException e) {
      log.error("Erro ao processar o arquivo.", e);
      return "";
    }
    catch (TikaException e) {
      log.error("Arquivo corrompido ou formato inv�lido.", e);
      return "";
    }

    return handler.toString();
  }

}
