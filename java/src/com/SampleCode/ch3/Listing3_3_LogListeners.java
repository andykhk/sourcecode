package com.SampleCode.ch3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.ch2.Listing2_1_HelloWorldConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;

/**
 * Java version of Listing3.3 example (Log Listeners)
 * 
 * P.54
 * @author andykwok
 *
 */
public class Listing3_3_LogListeners {
	
	public static void main(String[] args) {
		
		
		String ExName = "amq.rabbitmq.log";
		String ExType = "topic";
		
		
		ConnectionFactory fact = new ConnectionFactory ();
		fact.setHost("127.0.0.1");
		fact.setPort(5672);
		fact.setUsername("guest");
		fact.setPassword("guest");

		try(Connection conn = fact.newConnection();
				Channel ch = conn.createChannel();) {
			
			DeclareOk declare = ch.exchangeDeclare(ExName, ExType, true, false, true, null);
			if (declare != null) {
				
				/*
				 * Declare three queues  (Error || Warning || Info),
				 * As name is not specified, MQ server would generate a
				 * random name and return it along with declare ok msg.
				 */
				String QnameError = ch.queueDeclare().getQueue();
				String QnameWarning = ch.queueDeclare().getQueue();
				String QnameInfo = ch.queueDeclare().getQueue();
				//Bind Queue
				ch.queueBind(QnameError, ExName, "Error");
				ch.queueBind(QnameWarning, ExName, "Warning");
				ch.queueBind(QnameInfo, ExName, "Info");
				//Callback
				LogConsumer CeError = new LogConsumer(ch, "Error");
				LogConsumer CeWarning= new LogConsumer(ch, "Warning");
				LogConsumer CeInfo = new LogConsumer(ch, "Info");
				
				//Publish
				for (int i=0 ;i <100  ; i++) {
					ch.basicPublish("", QnameError , null, ("Hello world[" + i + "]").getBytes());
					ch.basicPublish("", QnameWarning , null, ("Hello world[" + i + "]").getBytes());
					ch.basicPublish("", QnameInfo , null, ("Hello world[" + i + "]").getBytes());
				}
				
				
				//Wait till all msg send out 
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					//consume
					ch.basicConsume(QnameError, CeError );
					ch.basicConsume(QnameWarning, CeWarning );
					ch.basicConsume(QnameInfo, CeInfo );
				}catch (IOException ex) {
					System.out.println("End of queue!");
				}
				
				
			}
			
		
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}
}
