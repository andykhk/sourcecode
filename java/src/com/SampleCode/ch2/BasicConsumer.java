package com.SampleCode.ch2;

import java.io.IOException;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


/**
 * Consumer used within ch2 example which simply print out the msg with prefix && Meta.
 * @author andykwok
 *
 */
public class BasicConsumer extends DefaultConsumer{

	public BasicConsumer(Channel channel) {
		super(channel);
	}
	
	
	public void handleDelivery(String consumerTag, Envelope envelope,
			AMQP.BasicProperties properties, byte[] body)
					throws IOException {
		String contentType = properties.getContentType();
		Map<String, Object> header = properties.getHeaders();
		long deliveryTag = envelope.getDeliveryTag();
		
		
		String message = new String(body, "UTF-8");
		System.out.println(" [x] Received '" + message + "'");
		System.out.println(" - ContentType: " + contentType);
		System.out.println(" - Header: " + header);
		System.out.println(" - DeliveryTag: " + deliveryTag);
	}

}
