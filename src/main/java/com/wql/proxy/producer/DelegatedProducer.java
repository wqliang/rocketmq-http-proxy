package com.wql.proxy.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class DelegatedProducer {
    private DefaultMQProducer producer;

    public DelegatedProducer() {
        producer = new DefaultMQProducer();
    }

    public void start() throws MQClientException {
        producer.start();
    }

    public void setNamesrvAddr(String addr) {
        producer.setNamesrvAddr(addr);
    }

    public SendResult send(Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return producer.send(message);
    }

    protected void shutdown() {
        producer.shutdown();
    }
}
