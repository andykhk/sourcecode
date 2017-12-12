package com.SampleCode.ch4.pictureUploader;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;

/**
 * P.76 Upload picture publisher
 * @author andykwok
 *
 */
public class Listing4_8PicturesConsumer {
	public static void main(String[] args) {

		String ExName = "upload-pictures";
		String ExType = "fanout";
		String Qname = "add-points";
		String routeKey = "route_key";


		ConnectionFactory fact = new ConnectionFactory ();
		fact.setHost("127.0.0.1");
		fact.setPort(5672);
		fact.setUsername("guest");
		fact.setPassword("guest");

		try(Connection conn = fact.newConnection();
				Channel ch = conn.createChannel();) {

			PicConsumer consumer = new PicConsumer(ch);
			
			if (ch.exchangeDeclare(ExName, ExType, false, true, false, null) != null) {
				if (ch.queueDeclare(Qname , false, false, false, null) != null) {
					if (ch.queueBind(Qname, ExName, routeKey) != null) {
						System.out.println("Ready to consume!");
						ch.basicConsume(Qname, consumer);
						ch.basicConsume(Qname, consumer);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
