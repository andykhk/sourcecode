package com.SampleCode.ch2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
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
		LocalConnFactory factory = new LocalConnFactory();
	
		try(Connection conn = factory.newConnection();
			/*
			 * No com.rabbitmq.client.AMQP,
			 * No com.rabbitmq.client.impl.AMQPImpl
			 * But com.rabbitmq.client.Channel
			 */
			Channel ch = conn.createChannel();) {

			//Passive == internal???
			if(ch.exchangeDeclare(exName, exType, true, false, false, null) != null) {
				//Bind to queue "hello-queue" if want to let helloWorldConsumer to be workable.
				if (ch.queueDeclare(qName, true, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routing_key) != null ) {
						
						System.out.println(" - Everything set, start to consume once per second for 50 times: ");
						for (int i=0 ; i<50 ; i++) {
							try {
								Thread.sleep(1000);
								ch.basicPublish(exName, routing_key, properties, (payload + "[" + i +  "]") .getBytes());
								System.out.println(" - Publish msg: " + payload  + "[" + i +  "]");	
							} catch (InterruptedException e) {e.printStackTrace();}
						}	
					}else {System.err.println("Queue declare Error!"); }
				}else {System.err.println("Declare Queue error!");}
			}else {System.err.println("Error, can't declare exchange <Hello-exchange>!");}
		} catch (IOException | TimeoutException e) {System.err.println("Exception happen, triggered autocloseable bye~");}


	}
	
	
	
}
