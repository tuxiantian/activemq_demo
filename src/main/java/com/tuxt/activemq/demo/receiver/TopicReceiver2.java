package com.tuxt.activemq.demo.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by tuxt on 2017/12/6.
 */
public class TopicReceiver2 implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(TopicReceiver2.class);
    
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)  {
            TextMessage textMessage= (TextMessage) message;
            try {
                logger.info(textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
