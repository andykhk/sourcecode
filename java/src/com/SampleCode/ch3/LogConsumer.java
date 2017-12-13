package com.SampleCode.ch3;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



/**
 * Custom consumer which print out the msg content while received.
 * @author andykwok
 *
 */
public class LogConsumer extends DefaultConsumer {

	String level;
	
	public LogConsumer(Channel channel) {
		super(channel);
	}
	
	/**
	 * Super always the first line
	 * Ref: https://stackoverflow.com/questions/23395513/implicit-super-constructor-person-is-undefined-must-explicitly-invoke-another
	 * @param channel
	 * @param level
	 */
	public LogConsumer(Channel channel, String level) {
		super(channel);
		this.level = level;
	}



	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			throws IOException {
		String message = new String(body, "UTF-8");
		System.out.println(level + ": " + message + "\n");
		
		//Acknowledge the msg , it drag the performance down, use it with caution.
		/*Channel ch = getChannel();
		 long deliveryTag = envelope.getDeliveryTag();
		ch.basicAck(deliveryTag, false);*/
		
		
		super.handleDelivery(consumerTag, envelope, properties, body);
	}
	
	
	



}
