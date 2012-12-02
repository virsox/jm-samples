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

/**
 * Classe de teste para o framework Tika.
 *
 * @author Paulo Sigrist / Wilson A. Higashino
 */
public class TestTika {


  public static void main(String[] args) throws FileNotFoundException,
      UnsupportedEncodingException
  {

    File f = new File("Teste.docx");
    InputStream inputStream = new FileInputStream(f);
    
    Metadata metadata = new Metadata();
    metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());

    ParseContext context = new ParseContext();
    ContentHandler handler = new BodyContentHandler();
//  MyContentHandler myHandler = new MyContentHandler();
//  ContentHandler handler = new BodyContentHandler(myHandler);

    Parser parser = new AutoDetectParser();
    try {
      parser.parse(inputStream, handler, metadata, context);
    }
    catch (IOException ioex) {
      throw new RuntimeException("Error parsing file", ioex);
    }
    catch (SAXException saxex) {
      throw new RuntimeException("Error parsing file", saxex);
    }
    catch (TikaException tex) {
      throw new RuntimeException("Error parsing file", tex);
    }
    finally {
      try {
        inputStream.close();
      }
      catch (IOException e) { }
    }
    
    System.out.println("---[ METADATA ]");
    for (String name : metadata.names()) {
      String value = metadata.get(name);
      System.out.println(name + " - " + value);
    }

  }
  
  /**
   * SAX Handler de teste.
   * @author Paulo Sigrist / Wilson A. Higashino
   */
  private static class MyContentHandler extends DefaultHandler {

    private int nestLevel = -1;
    private boolean lastWasElement = true;
    private StringBuilder content = new StringBuilder();

    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
      if (lastWasElement) ident(nestLevel + 1);
      lastWasElement = false;
      content.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
      content.append("\n");
      ident(nestLevel);
      content.append("</").append(localName).append(">\n");
      nestLevel--;
    }

    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException
    {
      if (!lastWasElement) content.append("\n");

      nestLevel++;
      ident(nestLevel);
      content.append("<").append(localName).append(">").append("\n");
      lastWasElement = true;
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
