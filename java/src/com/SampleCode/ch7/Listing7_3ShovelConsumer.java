package com.SampleCode.ch7;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
import com.SampleCode.util.NormalConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;

/**
 * 
 * 
 * @author andykwok
 *
 */
public class Listing7_3ShovelConsumer {

	public static void main(String[] args) {



		//Exchange config
		String exName = "incoming_orders";
		String exType = "direct";

		//Queue
		String qName = "incoming_orders";
		String routingKey = "warehouse";


		LocalConnFactory factory = new LocalConnFactory();


		try (Connection conn = factory.newConnection();
				Channel ch = conn.createChannel();) {
			if (ch.exchangeDeclare(exName, exType, false, true, null) != null) {
				if (ch.queueDeclare(qName, false, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routingKey)!=null) {
						//listen for 100s refresh for every second
						for (int i = 0; i < 100; i++) {
					
								try {
									Thread.sleep(1000);
									System.out.println("Consumming.....");
									Consumer consumer = new NormalConsumer(ch);
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
