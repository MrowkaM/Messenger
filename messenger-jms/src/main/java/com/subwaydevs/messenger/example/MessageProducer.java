package com.subwaydevs.messenger.example;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageProducer {

	
	public static void main(String[] args) throws NamingException {
		sendMessage();
	}
	
	public static void sendMessage() throws NamingException {
		InitialContext context = configureContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context
				.lookup("jms/RemoteConnectionFactory");
		Queue messages = (Queue) context.lookup("java:jms/queue/messagesQueue");
		try (JMSContext jmsContext = connectionFactory.createContext("sdevs", "sdevs")) {
			JMSProducer producer = jmsContext.createProducer();
			producer.setDeliveryMode(DeliveryMode.PERSISTENT).send(messages, "special message 1");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(messages, "not special message");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT).send(messages, "special message 2");
		}
	}
	
	private static InitialContext configureContext() throws NamingException {
		final Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		p.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
		p.put(Context.SECURITY_PRINCIPAL, "sdevs");
		p.put(Context.SECURITY_CREDENTIALS, "sdevs");
		InitialContext context = new InitialContext(p);
		return context;
	}
}
