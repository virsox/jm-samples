package br.com.jm.musiclib.web;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

@Named
@Singleton
public class IndexerBean {

	@Resource(mappedName = "queue/musicIndexerQueue")
	private Queue queue;

	@Resource(mappedName = "musicIndexerConnectionFactory")
	private QueueConnectionFactory connectionFactory;
	private String folder;

	public IndexerBean() {
		setFolder("C:/Users/Sigrist/Music");
	}

	public String doIndex() {
		try {
			// Enviar mensagem para um TOPIC
			QueueConnection connect = connectionFactory.createQueueConnection();
			QueueSession session = connect.createQueueSession(true, 0);
			MessageProducer producer = session.createProducer(queue);
			TextMessage textMsg = session.createTextMessage();
			textMsg.setText(getFolder());

			producer.send(textMsg);
			connect.start();
			connect.close();
			System.out.println("Mensagem enviada para topic: "
					+ queue.getQueueName());
		} catch (JMSException ex) {
			ex.printStackTrace();
		}

		return "status?faces-redirect=true";
	}
	
	public boolean isIndexed() {
		return false;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

}