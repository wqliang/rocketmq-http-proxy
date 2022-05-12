package com.wql.proxy.producer;

import com.wql.proxy.common.ProxyConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerManager {
    private static final Logger logger = LoggerFactory.getLogger(ProducerManager.class);
    private static final String PRODUCER_GROUP = "PROXY_PRODUCER_GROUP";
    private boolean producerPoolEnabled = true;

    private ProxyConfig config;

    private DefaultMQProducer producer;

    public ProducerManager(final ProxyConfig config) {
        this.config = config;
    }

    public void initialize() {
        producer = new DefaultMQProducer();
        producer.setProducerGroup(PRODUCER_GROUP);
        producer.setNamesrvAddr(config.getNamesrvAddress());
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.info("start producer fail", e);
        }
    }

    public DefaultMQProducer getNextProducer(String producerGroup) {
        if (producerPoolEnabled) {
            //return from pool;
            return producer;
        } else {
            //create a new producer if no instance for given group
            return producer;
        }
    }
}
