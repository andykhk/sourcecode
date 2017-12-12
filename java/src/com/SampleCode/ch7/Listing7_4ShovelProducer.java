package com.SampleCode.ch7;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.SampleCode.util.LocalConnFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Listing7_4ShovelProducer {
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
			if(ch.exchangeDeclare(exName, exType, false, true, null) != null ) {
				if(ch.queueDeclare(qName, false, false, true, null) != null) {
					if (ch.queueBind(qName, exName, routingKey) != null) {
						Order order = new Order(1, "AVOCADO_TYPE");
						ch.basicPublish(exName, routingKey, null, order.toString().getBytes());
					}else {System.out.println("Queue binding error!");	}
				}else {System.out.println("Queue declare error!");}
			}else {System.out.println("Exchange declare error!");}	
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
