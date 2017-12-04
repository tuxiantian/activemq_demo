package com.tuxt.activemq.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.JmsUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by tuxt on 2017/12/1.
 */
public class DeferMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(DeferMessageListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            TextMessage textMessage= (TextMessage) message;
            try {
                logger.info("I received a message :{}",textMessage.getText());
                throw new JMSException("process failed");
            } catch (JMSException e) {
                logger.error(e.getMessage());
                throw JmsUtils.convertJmsAccessException(e);
            }

        }
    }
}
