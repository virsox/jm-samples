package br.com.jm.musiclib.web;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

/**
 * Bean responsável por indexar as músicas. Como entrada, esse bean recebe uma
 * pasta que se encontra no servidor e envia essa configuração para uma fila
 * JMS, onde será consumida pelo indexador.
 * 
 * @author Paulo Sigrist / Wilson A. Higashino
 * 
 */
@Named
@SessionScoped
public class IndexerBean {

  /** Log */
  private Logger log = Logger.getLogger("br.com.jm.musiclib.web");

  /** Fila para enviar a mensagem */
  @Resource(mappedName = "queue/musicIndexerQueue")
  private Queue queue;

  /** Factory para poder enviar a mensagem para a fila */
  @Resource(mappedName = "musicIndexerConnectionFactory")
  private QueueConnectionFactory connectionFactory;
  /** Caminho da pasta que deverá ser indexado. */
  private String folder;

  /** Construtor padrão. */
  public IndexerBean() {
    setFolder("C:/Users/Public/Music");
  }

  /**
   * Indexa a pasta especificada pelo campo <tt>folder</tt>. Envia uma
   * mensagem para a fila JMS contendo a pasta que deve ser indexada.
   * 
   * @return redireciona para a página de status
   */
  public String doIndex() {
    try {
      // Obtem o QueueConnection
      QueueConnection connect = connectionFactory.createQueueConnection();
      // Obtem o QueueSession
      QueueSession session = connect.createQueueSession(true, 0);
      // Cria um MessageProducer
      MessageProducer producer = session.createProducer(queue);
      // Cria a mensagem do tipo TextMessage
      TextMessage textMsg = session.createTextMessage();
      // Coloca a pasta como corpo da mensagem
      textMsg.setText(getFolder());

      // Envia a mensagem para a fila
      producer.send(textMsg);
      // Inicia a conexão
      connect.start();
      // Fecha a conexão
      connect.close();
      log.info("Mensagem enviada para topic: " + queue.getQueueName());
    }
    catch (JMSException ex) {
      log.throwing("IndexerBean", "doIndex", ex);
      return "main?faces-redirect=true";
    }

    return "status?faces-redirect=true";
  }

  /**
   * 
   * @return o caminho da pasta que deve ser indexada.
   */
  public String getFolder() {
    return folder;
  }

  /**
   * Altera o caminho da pasta que deve ser indexada.
   * 
   * @param folder
   */
  public void setFolder(String folder) {
    this.folder = folder;
  }

}