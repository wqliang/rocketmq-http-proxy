package com.wql.proxy.common.protocol.commonds;

import com.wql.proxy.common.ProxyMessageQueue;
import com.wql.proxy.common.serializer.JSONSerializer;
import java.util.Map;

public class SendMessageCommand extends BaseCommand {
    private String group;
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
    private ProxyMessageQueue queue;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
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

    public void setDelayTimeLevel(Integer delayTimeLevel) {
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

    public Boolean getWaitStoreMsgOK() {
        return waitStoreMsgOK;
    }

    public void setWaitStoreMsgOK(Boolean waitStoreMsgOK) {
        this.waitStoreMsgOK = waitStoreMsgOK;
    }

    public ProxyMessageQueue getQueue() {
        return queue;
    }

    public void setQueue(ProxyMessageQueue queue) {
        this.queue = queue;
    }

    public static SendMessageCommand decode(byte[] data) {
        return JSONSerializer.parseObject(data, SendMessageCommand.class);
    }

    @Override public String toString() {
        return "SendMessageCommand{" +
            "group='" + group + '\'' +
            ", topic='" + topic + '\'' +
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
            ", queue=" + queue +
            '}';
    }
}
