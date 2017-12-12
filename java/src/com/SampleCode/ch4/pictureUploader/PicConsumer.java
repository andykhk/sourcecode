package com.SampleCode.ch4.pictureUploader;

import java.io.IOException;

import org.json.JSONObject;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class PicConsumer extends DefaultConsumer{

	public PicConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			throws IOException {
		System.out.println("Hello handle delivery");
		String message = new String(body, "UTF-8");
		JSONObject json = new JSONObject (message);
		System.out.println("Received msg: " + json.toString() );
		super.handleDelivery(consumerTag, envelope, properties, body);
	}
	

}
