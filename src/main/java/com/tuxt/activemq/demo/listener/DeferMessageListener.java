package com.tuxt.activemq.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.JmsUtils;

import javax.jms.*;

/**
 * Created by tuxt on 2017/12/1.
 */
public class DeferMessageListener implements SessionAwareMessageListener<TextMessage> {
    private static final Logger logger = LoggerFactory.getLogger(DeferMessageListener.class);

    @Override
    public void onMessage(TextMessage textMessage, Session session) {
        try {
            logger.info("I received a message :{}",textMessage.getText());
            session.rollback();
//                throw new JMSException("process failed");
        } catch (JMSException e) {
            logger.error(e.getMessage());
            throw JmsUtils.convertJmsAccessException(e);
        }
    }
}
