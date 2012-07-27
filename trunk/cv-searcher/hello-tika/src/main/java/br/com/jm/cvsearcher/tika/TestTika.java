package br.com.jm.cvsearcher.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestTika {
	
	private static class MyContentHandler extends DefaultHandler {

		private int nestLevel = -1;
		private boolean lastWasElement = true;
		private StringBuilder content = new StringBuilder();
		
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (lastWasElement)
				ident(nestLevel + 1);
				lastWasElement = false;
			 
			content.append(ch, start, length);			
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			
			content.append("\n");
			ident(nestLevel);
			content.append("</").append(localName).append(">\n");
			nestLevel--;

		}

		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			
			if (!lastWasElement)
				content.append("\n");			
			
			nestLevel++;
			
			ident(nestLevel);
			content.append("<").append(localName).append(">").append("\n");
			lastWasElement = true;
			
			//ident(nestLevel + 1);
		}

		public void ident(int level) {
			for (int i = 0; i < level; i++) {
				content.append("\t");
			}
		}
		
		public String getContent() {
			return content.toString();
		}
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File f = new File("/Users/xxxxx.docx");
		
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
		
		InputStream inputStream = new FileInputStream(f);		
		Parser parser = new AutoDetectParser();
		
		MyContentHandler myHandler = new MyContentHandler();
		ContentHandler handler = new BodyContentHandler(myHandler);
		//ContentHandler handler = new BodyContentHandler();
		
		ParseContext context = new ParseContext();
		
		try {
			parser.parse(inputStream, handler, metadata, context);
		} catch (IOException e) {
			// error reading file
			e.printStackTrace();
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (TikaException e) {
			// corrupt
			e.printStackTrace();
		}
		
		System.out.println(myHandler.getContent());
		
		System.out.println("------------------------------- METADATA");
		for(String name : metadata.names()) {
			String value = metadata.get(name);
			System.out.println(name + " - " + value);
		}		

	}
}
