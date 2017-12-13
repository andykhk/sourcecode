package com.SampleCode.ch4.alertService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Account need to be created in advanced as book say, CLI can be found in following:
 * 
 *  - rabbitmqctl add_user alert_user alertme
 *  - rabbitmqctl set_permissions alert_user ".*" ".*" ".*"
 *  
 *  
 *  This piece of code used to demonstrate how routing key can be used to filter || route the 
 *  msg, as you see critical.* || *.rate_limit are set for routing, all msg which dun meet 
 *  the routing key woudl be simply ignored.
 * @author andykwok
 *
 */
public class Listing4_1_2Alert {
	public static void main(String[] args) {
		String ExName = "alerts_test";
		String ExType = "topic";

		String QnameCritical = "critical";
		String QnameRate_limit = "rate_limit";

		ConnectionFactory fact = new ConnectionFactory ();
		fact.setHost("127.0.0.1");
		fact.setPort(5672);
		fact.setUsername("alert_user");
		fact.setPassword("alertme");

		try(Connection conn = fact.newConnection();
				Channel ch = conn.createChannel();) {

			//Declare Exchange
			if ( ch.exchangeDeclare(ExName, ExType) !=null) {
				/**
				 * Listing 4.2
				 */

				//Declare queue
				ch.queueDeclare(QnameCritical, false, false, false, null);
				ch.queueDeclare(QnameRate_limit, false, false, false, null);

				//Bind Queue
				ch.queueBind(QnameCritical, ExName, "critical.*");
				ch.queueBind(QnameRate_limit, ExName, "*.rate_limit");

				System.out.println(" - Declare work done !");
				//produce
				//ch.basicPublish(ExName, "critical.xxx", null, "XXXX".getBytes());

				//A reasonable rate. 
				for (int i=0 ; i<2 ; i++) {			
					try {
						ch.basicPublish("alerts_test", "Test.rate_limit", null, ("Hello world[" + i + "]").getBytes());
						ch.basicPublish("alerts_test", "critical.sdf", null,("Hello world[-" + i + "]").getBytes());
					}catch (Exception ex) {
						System.err.println("Sender error");
					}

				}

				System.out.println("After publish!");

				/**
				 * Listing 4.3 Consume msg
				 * 
				 * Remember to catch exception from basucConsume!
				 * If you let throw all the way up, it would reach with Catch block 
				 * for try-with-resources and trigger connection to close, which is not 
				 * it suppose to be. 
				 */

				System.out.println("Start consume!");


				NotifyConsumer consumer = new NotifyConsumer(ch);
				//Only can acknowledge when autoack turn to false
				ch.basicConsume("critical", false, consumer);
				ch.basicConsume("rate_limit", false,  consumer);



				System.out.println("Finish consume!");

			}

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
