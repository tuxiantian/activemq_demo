## DLQ-死信队列介绍
DLQ-死信队列(Dead Letter Queue)用来保存处理失败或者过期的消息。  
出现以下情况时，消息会被redelivered  
 A transacted session is used and rollback() is called.  
 A transacted session is closed before commit is called.  
 A session is using CLIENT_ACKNOWLEDGE and Session.recover() is called.  
当一个消息被redelivered超过maximumRedeliveries(缺省为6次，具体设置请参考后面的链接)次数时，会给broker发送一个"Poison ack"，这个消息被认为是a poison pill，这时broker会将这个消息发送到DLQ，以便后续处理。   
缺省的死信队列是ActiveMQ.DLQ，如果没有特别指定，死信都会被发送到这个队列。  
缺省持久消息过期，会被送到DLQ，非持久消息不会送到DLQ。  
可以通过配置文件(activemq.xml)来调整死信发送策略。  
## activemq.xml死信队列的配置
```
<destinationPolicy>
    <policyMap>
      <policyEntries>
        <policyEntry queue=">">
             <deadLetterStrategy>
                 <individualDeadLetterStrategy queuePrefix="DLQ."
                  useQueueForQueueMessages="true" />
             </deadLetterStrategy>
        </policyEntry>
        <policyEntry topic=">" >
            <!-- The constantPendingMessageLimitStrategy is used to prevent
                 slow topic consumers to block producers and affect other consumers
                 by limiting the number of messages that are retained
                 For more information, see:

                 http://activemq.apache.org/slow-consumer-handling.html

            -->
          <pendingMessageLimitStrategy>
            <constantPendingMessageLimitStrategy limit="1000"/>
          </pendingMessageLimitStrategy>
        </policyEntry>
      </policyEntries>
    </policyMap>
</destinationPolicy>
```
## 事务消息的配置方式
```
<bean id="jmsTransactionManager"
      class="org.springframework.jms.connection.JmsTransactionManager">
    <property name="connectionFactory" ref="connectionFactory" />
</bean>
<!-- 定义Queue监听器 -->
<jms:listener-container destination-type="queue" container-type="default" transaction-manager="jmsTransactionManager" connection-factory="connectionFactory" acknowledge="auto" receive-timeout="10000">
    <jms:listener destination="deferQueueDestination" ref="deferMessageListener" concurrency="20"/>
</jms:listener-container>
<bean id="deferMessageListener" class="com.tuxt.activemq.demo.listener.DeferMessageListener"/>
```
container-type="default"对应的类是：DefaultMessageListenerContainer
## 让消息重复投递的两种方式
```
public void onMessage(TextMessage textMessage, Session session) {
        try {
            logger.info("I received a message :{}",textMessage.getText());
            session.rollback();
        } catch (JMSException e) {
            logger.error(e.getMessage());
            throw JmsUtils.convertJmsAccessException(e);
        }
    }
```

```
public void onMessage(TextMessage textMessage, Session session) {
        try {
            logger.info("I received a message :{}",textMessage.getText());
            throw new JMSException("process failed");
        } catch (JMSException e) {
            logger.error(e.getMessage());
            throw JmsUtils.convertJmsAccessException(e);
        }
    }
```

## 延迟消息
延迟队列能做什么？

延迟队列多用于需要延迟工作的场景。最常见的是以下两种场景：

1. 延迟消费。比如：
用户生成订单之后，需要过一段时间校验订单的支付状态，如果订单仍未支付则需要及时地关闭订单。
用户注册成功之后，需要过一段时间比如一周后校验用户的使用情况，如果发现用户活跃度较低，则发送邮件或者短信来提醒用户使用。
2. 延迟重试。比如消费者从队列里消费消息时失败了，但是想要延迟一段时间后自动重试。

如果不使用延迟队列，那么我们只能通过一个轮询扫描程序去完成。这种方案既不优雅，也不方便做成统一的服务便于开发人员使用。但是使用延迟队列的话，我们就可以轻而易举地完成。

[官方使用介绍](http://activemq.apache.org/delay-and-schedule-message-delivery.html)

ActiveMQ from version 5.4 has an optional persistent scheduler built into the ActiveMQ message broker.
 It is enabled by setting the broker schedulerSupport attribute to true in the xml configuration.

意思是说要在mq的xml配置文件中，broker节点处加入schedulerSupport属性，才能使mq的任务计划生效，
于是，我尝试加入该属性修改activemq.xml 在`<broker>`里添加属性schedulerSupport="true" 重启mq后果然生效了。
## ActiveMQ 集群配置
(ActiveMQ 集群配置)[http://blog.csdn.net/u010739551/article/details/50676057]

Shared Filesystem Master-Slave方式 

shared filesystem Master-Slave部署方式主要是通过共享存储目录来实现master和slave的热备，所有的ActiveMQ应用都在不断地获取共享目录的控制权，哪个应用抢到了控制权，它就成为master。 

多个共享存储目录的应用，谁先启动，谁就可以最早取得共享目录的控制权成为master，其他的应用就只能作为slave。 
