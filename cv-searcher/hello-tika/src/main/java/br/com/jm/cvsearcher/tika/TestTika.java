package br.com.jm.cvsearcher.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class TestTika {

	public static void main(String[] args) throws Exception {

		// Obter o Stream com os dados
		File f = new File("Teste.docx");
		InputStream inputStream = new FileInputStream(f);

		// Objeto que ir� conter os meta dados 
		Metadata metadata = new Metadata();
		// Content Handler que ir� conter os dados
		ContentHandler handler = new BodyContentHandler();
		// Objeto para passar parametros para o Parser
		ParseContext context = new ParseContext();
		// Implementa��o do Parser.
		Parser parser = new OOXMLParser(); 

		try {
			// Executar o parser do input stream 
			parser.parse(inputStream, handler, metadata, context);
		} finally {
			inputStream.close();
		}

		// Obt�m o conte�do extra�do
		String content = handler.toString();
		System.out.println(content);
		
		// Metadados
		System.out.println("---[ METADATA ]");
		for (String name : metadata.names()) {
			String value = metadata.get(name);
			System.out.println(name + ": " + value);
		}

	}
}
