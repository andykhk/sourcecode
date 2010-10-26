###############################################
# RabbitMQ in Action
# Chapter 3.3.3 - RPC Server
# 
# Requires: pika >= 0.5
# 
# Author: Jason J. W. Williams
# (C)2010
###############################################

import sys, asyncore, time, json
import pika

# Establish connection to broker
creds_broker = pika.PlainCredentials("rpc_user", "rpcme")
conn_params = pika.ConnectionParameters("localhost",
                                        virtual_host = "/",
                                        credentials = creds_broker,
                                        heartbeat = 10)
conn_broker = pika.AsyncoreConnection(conn_params)
channel = conn_broker.channel()

# Declare Exchange & "ping" Call Queue
channel.exchange_declare(exchange="rpc",
                         type="direct",
                         auto_delete=False)
channel.queue_declare(queue="ping", auto_delete=False)
channel.queue_bind(queue="ping",
                   exchange="rpc",
                   routing_key="ping")

# Wait for RPC calls and reply
def api_ping(channel, method, header, body):
    """'ping' API call."""
    channel.basic_ack(delivery_tag=method.delivery_tag)
    msg_dict = json.loads(body)
    print "Received API call...replying..."
    channel.basic_publish(body="Pong!" + str(msg_dict["time"]),
                          exchange="",
                          routing_key=header.reply_to)

channel.basic_consume(api_ping,
                      queue="ping",
                      consumer_tag="ping")
asyncore.loop()