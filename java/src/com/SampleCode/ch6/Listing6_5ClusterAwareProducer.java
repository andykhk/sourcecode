package com.SampleCode.ch6;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import com.SampleCode.util.LocalConnFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;



/**
 * Producer for Listing 6.2/4 which send msg to exchange "Cluster_test"
 * for testing purpose.
 * @author andykwok
 *
 */
public class Listing6_5ClusterAwareProducer {

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
			if(ch.exchangeDeclare(exName, exType, false, true, null) != null ) {
				if(ch.queueDeclare(qName, false, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routingKey) != null) {
						
						//Composite a JSon string
						JSONObject msg = new JSONObject();
						msg.put("Content", "Cluster Test!");
						msg.put("time",  LocalDateTime.now().toString());
							ch.basicPublish(exName, routingKey, null, msg.toString().getBytes());
					}else {System.out.println("Queue binding error!");	}
				}else {System.out.println("Queue declare error!");}
			}else {System.out.println("Exchange declare error!");}	
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
