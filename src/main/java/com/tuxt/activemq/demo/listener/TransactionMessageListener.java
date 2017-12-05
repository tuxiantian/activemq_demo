package com.tuxt.activemq.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tuxt on 2017/12/5.
 */
public class TransactionMessageListener implements SessionAwareMessageListener<TextMessage> {
    private static final Logger logger = LoggerFactory.getLogger(TransactionMessageListener.class);
    AtomicInteger num=new AtomicInteger(0);
    @Override
    public void onMessage(TextMessage message, Session session) throws JMSException {
        try {
            logger.info("I received a transaction message :{}",message.getText());
            num.incrementAndGet();
            if (num.get()>2){
                throw new JMSException("test transaction");
            }
            message.acknowledge();

        } catch (JMSException e) {
            logger.error(e.getMessage());
            session.rollback();
        }
    }
}
