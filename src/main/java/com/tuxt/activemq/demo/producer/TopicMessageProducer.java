package com.tuxt.activemq.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by tuxt on 2017/12/6.
 */
@Component
public class TopicMessageProducer {
    @Autowired
    private JmsTemplate testTopicTemplate;

    public void sendMessage(){
        testTopicTemplate.send(session -> session.createTextMessage("topic message"));
    }
}
