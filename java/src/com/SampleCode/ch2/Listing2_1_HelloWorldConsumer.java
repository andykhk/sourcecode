package com.SampleCode.ch2;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Queue;

/**
 * Java version of Listing2.1 Example (Hello World consumer)
 * P.31
 * @author andykwok
 *
 */
public class Listing2_1_HelloWorldConsumer {
	public static void main(String[] args) {


		
		//Exchange config
		String exName = "Hello-exchange";
		String exType = "direct";


		//Queue
		String qName = "hello-queue";
		String routing_key = "hola";
		
		//Properties
		BasicProperties properties = new AMQP.BasicProperties.Builder()
				.contentType("text/plain")
				.build();


		//Construct a factory upon above parameters
		LocalConnFactory factory = new LocalConnFactory();

		//ExecutorService es = Executors.newSingleThreadExecutor();
		//try(Connection conn = factory.newConnection(es);
		try(Connection conn = factory.newConnection();
			Channel ch = conn.createChannel();) {

			//Boolean: durable || autoDelete || internal (Passive)?
			DeclareOk declareEx = ch.exchangeDeclare(exName, exType, true, false, false, null);
	
			if(declareEx != null) {
			
				//Declare exchange
				Queue.DeclareOk declareQ = ch.queueDeclare(qName, true, false, true, null);
				if (declareQ != null) {
					
					ch.queueBind(qName, exName, routing_key);
					
					System.out.println(" - All thing set (Start to consume!)");
					
					BasicConsumer consumer = new BasicConsumer(ch);
					for (int i=0 ; i<100 ; i++) {
						System.out.println("Consumer loop[" + (i+1) + "]");
						ch.basicConsume(qName, consumer);
					
					try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.err.println("Thread sleep error");
						}
						
					}
					
					
				}else {
					System.err.println("Error, queue[" + qName + "] can't not be create!");
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
