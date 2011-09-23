package br.com.jm.musiclib.indexer.impl;

import java.util.logging.Logger;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import br.com.jm.musiclib.indexer.MusicIndexer;

/**
 * Message-Driven Bean que inicia o processo de indexa��o.
 * 
 */
@MessageDriven(mappedName = "queue/musicIndexerQueue")
public class MusicIndexerMessageBean implements MessageListener {
  /** Log */
  private Logger log = Logger.getLogger("br.com.jm.musiclib.indexer");

  /** MusicIndexer injetado pelo container. */
  @Inject
  private MusicIndexer indexer;

  /**
   * M�todo executado quando alguma mensagem � enviada para a fila queue/musicIndexerQueue.
   * Retira da mensagem recebida a pasta onde os arquivos ser�o indexados e chama o indexador.
   * 
   * @see MessageListener#onMessage(Message)
   * @see MusicIndexer
   * @see MusicIndexer#createIndex(String)
   */
  public void onMessage(Message message) {
    String folder;

    if (message instanceof TextMessage) {
      TextMessage textMessage = (TextMessage) message;

      try {
        folder = textMessage.getText();
      } catch (JMSException e) {
        folder = null;
      }
      if (folder != null) {
        log.info("Iniciando indexa��o para pasta: " + folder);
        indexer.createIndex(folder);
      }
    }

  }

}
