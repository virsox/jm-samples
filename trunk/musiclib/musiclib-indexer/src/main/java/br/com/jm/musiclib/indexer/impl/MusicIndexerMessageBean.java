package br.com.jm.musiclib.indexer.impl;

import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import br.com.jm.musiclib.indexer.MusicIndexer;

/**
 * Message-Driven Bean implementation class for: MusicIndexerMessageBean
 * 
 */
@MessageDriven(mappedName = "queue/musicIndexerQueue")
public class MusicIndexerMessageBean implements MessageListener {

	@EJB
	private MusicIndexer indexer;

	/**
	 * Método executado quando alguma mensagem é enviada para a fila queue/musicIndexerQueue.
	 * Retira da mensagem recebida a pasta onde os arquivos serão indexados e chama o indexador.
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
				System.out.println("Iniciando indexação para pasta: "+folder);
				indexer.createIndex(folder);
			}
		}

	}

}
