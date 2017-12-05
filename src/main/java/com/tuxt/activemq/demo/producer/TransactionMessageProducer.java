package com.tuxt.activemq.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * Created by tuxt on 2017/12/5.
 */
@Component
public class TransactionMessageProducer {
    @Autowired
    private JmsTemplate transactionQueueTemplate;

    public void sendMessage(){
        transactionQueueTemplate.send(session ->{
            TextMessage message = session.createTextMessage("hello");
            return message;
        });
    }
}
