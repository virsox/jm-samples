package br.com.jm.cvsearcher.bridge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.StringBridge;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.jm.cvsearcher.model.Curriculum;

/**
 * Implementação de StringBridge para poder indexar conteúdos binários do
 * Microsoft Word.
 * 
 * Essa bridge pode ser parametrizada. Hoje o único formato suportado é DOC
 * (Microsoft Word), mas futuramente, outros formatos também podem ser
 * adicionados (como o Adobe PDF).
 * 
 * A implementação extrai o conteúdo do binário através do Framework Tika e
 * retorna o conteúdo em formato String para o indexador Lucene.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 * 
 */
public class TikaBridge implements StringBridge, ParameterizedBridge {
	/**
	 * Enum que define os tipos de documentos suportados
	 * 
	 */
	private enum SupportedTypes {
		/**
		 * Define o tipo DOC - Microsoft Word
		 */
		DOC
	}

	/**
	 * Constante que define o nome da propriedade que irá identificar o tipo de
	 * documento que será decodificado.
	 */
	private static final String TYPE_PROPERTY = "type";
	/** Tipo de decodificador que será utilizado. */
	private SupportedTypes type;

	/**
	 * {@inheritDoc}
	 * 
	 * Dentre os decodificadores disponíveis, verifica qual está configurado e
	 * executa o decodificaror.
	 * 
	 */
	@Override
	public String objectToString(Object object) {

		switch (type) {
		case DOC:
			return docIndex((byte[]) object);
		default:
			break;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Recebe o parametro configurado na anotação e converte para um tipo
	 * suportado.
	 */
	@Override
	public void setParameterValues(Map<String, String> parameters) {
		type = SupportedTypes.valueOf(parameters.get(TYPE_PROPERTY));

	}

	/**
	 * Converte os dados binários em formato String para poder ser indexado pelo
	 * Lucene.
	 * 
	 * @param data
	 *            Dados para serem decodificados
	 * @return dados codificados no formato String
	 * 
	 * @see Curriculum#getContent()
	 * @see Parser
	 */
	private String docIndex(byte[] data) {

		InputStream inputStream = new ByteArrayInputStream(data);
		Parser parser = new AutoDetectParser();

		DocContentHandler myHandler = new DocContentHandler();
		BodyContentHandler handler = new BodyContentHandler(myHandler);

		ParseContext context = new ParseContext();
		Metadata metadata = new Metadata();

		try {
			parser.parse(inputStream, handler, metadata, context);
			inputStream.close();
		} catch (IOException e) {
			// error reading file
			// FIXME PRocessar exception
			e.printStackTrace();

		} catch (SAXException e) {
			e.printStackTrace();
			// FIXME PRocessar exception

		} catch (TikaException e) {
			// corrupt
			e.printStackTrace();
			// FIXME PRocessar exception
		}

		return myHandler.getContent();
	}

	/**
	 * TODO Adicionar JavaDOC para o DocContentHandler
	 * 
	 * @author Paulo Sigrist / Wilson A. Higashino
	 * 
	 */
	private static class DocContentHandler extends DefaultHandler {
		/** TODO JavaDoc */
		private int nestLevel = -1;
		/** TODO JavaDoc */
		private boolean lastWasElement = true;
		/** TODO JavaDoc */
		private StringBuilder content = new StringBuilder();

		/** TODO JavaDoc */
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (lastWasElement)
				ident(nestLevel + 1);
			lastWasElement = false;

			content.append(ch, start, length);
		}

		/** TODO JavaDoc */
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			content.append("\n");
			ident(nestLevel);
			content.append("</").append(localName).append(">\n");
			nestLevel--;

		}

		/** TODO JavaDoc */
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {

			if (!lastWasElement)
				content.append("\n");

			nestLevel++;

			ident(nestLevel);
			content.append("<").append(localName).append(">").append("\n");
			lastWasElement = true;

			// ident(nestLevel + 1);
		}

		/**
		 * TODO JavaDoc
		 * 
		 * @param level
		 *            TODO
		 */
		public void ident(int level) {
			for (int i = 0; i < level; i++) {
				content.append("\t");
			}
		}

		/**
		 * TODO JavaDoc
		 * 
		 * @return TODO
		 */
		public String getContent() {
			return content.toString();
		}

	}

}
