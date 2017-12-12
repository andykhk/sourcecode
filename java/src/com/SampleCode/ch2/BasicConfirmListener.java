package com.SampleCode.ch2;

import java.io.IOException;

import com.rabbitmq.client.ConfirmListener;

public class BasicConfirmListener implements ConfirmListener {

	@Override
	public void handleAck(long deliveryTag, boolean multiple) throws IOException {
		System.out.println(" - (BasicConfirmListener); handle ack msg for: " + deliveryTag);
		
	}

	@Override
	public void handleNack(long deliveryTag, boolean multiple) throws IOException {
		System.out.println(" - (BasicConfirmListener); handle NNNack msg for: " + deliveryTag);
		
	}

}
