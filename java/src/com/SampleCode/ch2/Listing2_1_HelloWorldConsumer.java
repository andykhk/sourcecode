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


		//Construct a factory upon above parameters
		LocalConnFactory factory = new LocalConnFactory();
		try(Connection conn = factory.newConnection();
				Channel ch = conn.createChannel();) {

			//Boolean: durable || autoDelete || internal (Passive)?
			if(ch.exchangeDeclare(exName, exType, true, false, false, null) != null) {
				if (ch.queueDeclare(qName, true, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routing_key) != null) {
						
						System.out.println(" - Fetch msg once per second, repeat for 100 times");
						//Reuse consumer instance
						BasicConsumer consumer = new BasicConsumer(ch);
						for (int i=0 ; i<100 ; i++) {
							System.out.println("/////////////Consumer loop[" + (i+1) + "]");
							//ch.basicConsume(qName, consumer);
							ch.basicConsume(qName, true, consumer);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {System.err.println("Thread sleep error"); }
						}
					}else {System.err.println("Bind Queue Error!");}
				}else {System.err.println("Error, queue[" + qName + "] can't not be create!");}	
			}else {System.err.println("Error, can't declare exchange <Hello-exchange>!");}
		} catch (IOException | TimeoutException e) {
			System.err.println("Exception happen, triggered autocloseable to shut down connection, bye~");}
	}
}
