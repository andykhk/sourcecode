package com.SampleCode.ch3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.ch2.Listing2_1_HelloWorldConsumer;
import com.SampleCode.util.LocalConnFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;

/**
 * Java version of Listing3.3 example (Log Listeners)
 * 
 * As you may notice queue name is not specified at all, in this case server-side would
 * automatically generate a random name zip it along with the DeclareOK object when 
 * ch.queueDecalre( ) is called. As the queue name is generated randomly which intend 
 * to be disposable and one time use -only. Which are exclusive / auto-delete / non durable
 * 
 * P.54
 * @author andykwok
 *
 */
public class Listing3_3_LogListeners {
	
	public static void main(String[] args) {
		
		
		String ExName = "amq.rabbitmq.log";
		String ExType = "topic";
		
		
		ConnectionFactory fact = new LocalConnFactory();


		try(Connection conn = fact.newConnection();
				Channel ch = conn.createChannel();) {
			
			if (ch.exchangeDeclare(ExName, ExType, true, false, true, null) != null) {
				
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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
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
