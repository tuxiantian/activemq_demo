package com.tuxt.activemq.demo.producer;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * Created by tuxt on 2017/12/1.
 */
@Component
public class DeferMessageProducer {
    @Autowired
    private JmsTemplate deferQueueTemplate;

    public void sendMessage(){
        deferQueueTemplate.send(session ->{
            TextMessage message = session.createTextMessage("hello");
            long time = 60 * 1000;
            long period=10 * 1000;
            int repeat=1;
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
            return message;
        });
    }
}
