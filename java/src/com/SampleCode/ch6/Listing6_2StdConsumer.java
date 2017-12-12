package com.SampleCode.ch6;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
import com.SampleCode.util.NormalConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;

/**
 * Normal consumer which attempt to connect MQ Server only once.
 * And terminate either connection established successfully and
 * consume for once or Exception happen which lead to connection 
 * shutdown. (Won't re-attempt in either case).  
 * 
 * @author andykwok
 *
 */
public class Listing6_2StdConsumer {

	public static void main(String[] args) {
		
		
		//Exchange config
		String exName = "cluster_test";
		String exType = "direct";


		//Queue
		String qName = "cluster_test";
		String routingKey = "cluster_test";

		
		LocalConnFactory factory = new LocalConnFactory();
		
		try (Connection conn = factory.newConnection();
				Channel ch = conn.createChannel();) {
			
			if (ch.exchangeDeclare(exName, exType, false, true, null) != null) {
				if (ch.queueDeclare(qName, false, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routingKey)!=null) {
						
						Consumer consumer = new NormalConsumer(ch);
						ch.basicConsume(qName, consumer);
						
					}else {System.out.println("Queue binding error!");	}
				}else {System.out.println("Queue declare error!");}
			}else {System.out.println("Exchange declare error!");}
			
			
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		
	}
}
