package com.SampleCode.ch4.alertService;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Listing 4.4 Critical alerts processor
 * @author andykwok
 *
 */
public class NotifyConsumer extends DefaultConsumer{

	String email;
	
	
	public NotifyConsumer(Channel channel) {
		super(channel);
		
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			 {
		
		
		try {
			super.handleDelivery(consumerTag, envelope, properties, body);
			String message = new String(body, "UTF-8");
			String recips = "ops.team@ourcompany.com";
			
			System.out.println ("Send alert via e-mail! Alert Text: " + message + 
								"\nRecipients: " + recips + "\n");
		}catch (Exception ex) {
			System.err.println("Exception happen on HandleDelivery!");
		}
		
	}
	
	
	

}
