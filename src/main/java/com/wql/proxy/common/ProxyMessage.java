package com.wql.proxy.common;

import java.util.Map;

public class ProxyMessage {
    private String topic;
    private Integer flag;
    private Map<String, String> properties;
    private String body;
    private String transactionId;
    private String buyerId;
    private Integer delayTimeLevel;
    private String instanceId;
    private String keys;
    private String tags;
    private Boolean waitStoreMsgOK;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean isWaitStoreMsgOK() {
        return waitStoreMsgOK;
    }

    public void setWaitStoreMsgOK(boolean waitStoreMsgOK) {
        this.waitStoreMsgOK = waitStoreMsgOK;
    }

    @Override public String toString() {
        return "ProxyMessage{" +
            "topic='" + topic + '\'' +
            ", flag=" + flag +
            ", properties=" + properties +
            ", body='" + body + '\'' +
            ", transactionId='" + transactionId + '\'' +
            ", buyerId='" + buyerId + '\'' +
            ", delayTimeLevel=" + delayTimeLevel +
            ", instanceId='" + instanceId + '\'' +
            ", keys='" + keys + '\'' +
            ", tags='" + tags + '\'' +
            ", waitStoreMsgOK=" + waitStoreMsgOK +
            '}';
    }
}
