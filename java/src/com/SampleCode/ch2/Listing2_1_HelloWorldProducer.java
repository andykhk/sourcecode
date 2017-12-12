package com.SampleCode.ch2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Java version of Listing2.1 Example (Hello World producer)
 * P.29
 * 
 * Noted that as queue is not specificed in this case yet, all msg publish from
 * this metho wouldn't be received by {@link Listing2_1_HelloWorldConsumer} which
 * have specify "hello-queue" as its queue to use.
 * @author andykwok
 *
 */
public class Listing2_1_HelloWorldProducer {

	
	public static void main(String[] args) {
		
		//Var declaration 
		String host = "127.0.0.1";
		int port = 5672;
		String username = "guest";
		String password = "guest";
		
		//Exchange config
		String exName = "Hello-exchange";
		String exType = "direct";
		
		//Queue
		String qName = "hello-queue";
		
		String routing_key = "hola";
		String payload = "Hello RabbitMQ";
		//Properties
		BasicProperties properties = new AMQP.BasicProperties.Builder()
				.contentType("text/plain")
				.build();
		
		//Construct a factory upon above parameters
		ConnectionFactory factory = new ConnectionFactory ();
		
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		
		try(Connection conn = factory.newConnection();
			/*
			 * No com.rabbitmq.client.AMQP,
			 * No com.rabbitmq.client.impl.AMQPImpl
			 * But com.rabbitmq.client.Channel
			 */
			Channel ch = conn.createChannel();) {
			
			//Passive == internal???
			DeclareOk declareOk = ch.exchangeDeclare(exName, exType, true, false, false, null);
			
			
			if(declareOk != null) {
				
				//Bind to queue "hello-queue" if want to let helloWorldConsumer to be workable.
				Queue.DeclareOk declareQ = ch.queueDeclare(qName, true, false, true, null);
				if (declareQ != null) {
					ch.queueBind(qName, exName, routing_key);
					//Unlike python, properties can set upon publish time
					System.out.println(" - Everything set");
					ch.basicPublish(exName, routing_key, properties, payload.getBytes());
					System.out.println(" - Publish msg: " + payload);	
				}	
			}else {
				System.err.println("Error, can't declare exchange <Hello-exchange>!");
			}
			
			
			
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
