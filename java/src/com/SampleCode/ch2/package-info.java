/**
 * A basic workable pair of producer and consumer to demonstrate basic 
 * Rabbit usage. Listign 2_3 is simalr as 2_1, the only different is 
 * 2_3 would wait 5s after msg send, if the msg deliver to consumer 
 * with in the period of time, acknowledge stage would send back to producer
 * and ConfirmListener would print out acknowledge msg.
 */
/**
 * @author andykwok
 *
 */
package com.SampleCode.ch2;