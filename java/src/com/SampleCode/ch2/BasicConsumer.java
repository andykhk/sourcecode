package com.SampleCode.ch2;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class BasicConsumer extends DefaultConsumer{

	public BasicConsumer(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	
	public void handleDelivery(String consumerTag, Envelope envelope,
			AMQP.BasicProperties properties, byte[] body)
					throws IOException {
		/*String contentType = properties.getContentType();
		Map<String, Object> header = properties.getHeaders();
		long deliveryTag = envelope.getDeliveryTag();
		
		System.out.println("ContentType: " + contentType);
		System.out.println("Header: " + header);
		System.out.println("DeliveryTag: " + deliveryTag);*/
		
		
		
		String message = new String(body, "UTF-8");
		System.out.println(" [x] Received '" + message + "'");
	}

}
