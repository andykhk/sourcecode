package com.SampleCode.ch6;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
import com.SampleCode.util.NormalConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;

/**
 * Similar to 6.2 but this time having a outer loop which let program automatically
 * reconnect once single consumer is done || connection failure. -> it match the case 
 * that once the connecting node is down, then the program would smart enough to 
 * auto connect. 
 * 
 * 
 * 
 * @author andykwok
 *
 */
public class Listing6_4ClusterAwareConsumer {

	public static void main(String[] args) {


		//Exchange config
		String exName = "cluster_test";
		String exType = "direct";

		//Queue
		String qName = "cluster_test";
		String routingKey = "cluster_test";


		LocalConnFactory factory = new LocalConnFactory();
		while (true) {
		//100 Times for demonstration purpose
		//for (int i=0 ; i<100 ; i++) {
			try (Connection conn = factory.newConnection();
					Channel ch = conn.createChannel();) {
				if (ch.exchangeDeclare(exName, exType, false, true, null) != null) {
					if (ch.queueDeclare(qName, false, false, true, null) != null) {
						if (ch.queueBind(qName, exName, routingKey)!=null) {
							
							System.out.println("Consumming.....");
							Consumer consumer = new NormalConsumer(ch);
							for (int i = 0; i < 100; i++) {
								try {
									Thread.sleep(1000);
									ch.basicConsume(qName, consumer);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								
							}
						}else {System.out.println("Queue binding error!");	}
					}else {System.out.println("Queue declare error!");}
				}else {System.out.println("Exchange declare error!");}
			} catch (IOException | TimeoutException e) {
				System.err.println("Exception occur, attempt to reconnect!");
			}
			
		}
	}
}
