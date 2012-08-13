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

public class TikaBridge implements StringBridge, ParameterizedBridge {
	private enum SupportedTypes {
		DOC
	}

	private static final String TYPE_PROPERTY = "type";
	private SupportedTypes type;

	@Override
	public String objectToString(Object object) {
		
		switch (type) {
		case DOC:
			return docIndex((byte [])object);
		default:
			break;
		}
		return null;
	}

	@Override
	public void setParameterValues(Map<String, String> parameters) {
		type = SupportedTypes.valueOf(parameters.get(TYPE_PROPERTY));

	}
	
	private String docIndex(byte[] data) {
		
		InputStream inputStream = new ByteArrayInputStream(data);		
		Parser parser = new AutoDetectParser();
		
		MyContentHandler myHandler = new MyContentHandler();
		BodyContentHandler handler = new BodyContentHandler(myHandler);
		
		ParseContext context = new ParseContext();
		Metadata metadata = new Metadata();
		
		try {
			parser.parse(inputStream, handler, metadata, context);
			inputStream.close();
		} catch (IOException e) {
			// error reading file
			e.printStackTrace();
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (TikaException e) {
			// corrupt
			e.printStackTrace();
		} 
		
		return myHandler.getContent();
	}

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

}
