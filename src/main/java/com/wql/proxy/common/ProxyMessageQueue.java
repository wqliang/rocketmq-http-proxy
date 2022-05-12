package com.wql.proxy.common;

public class ProxyMessageQueue {
    private String brokerName;
    private int id;

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override public String toString() {
        return "ProxyMessageQueue{" +
            "brokerName='" + brokerName + '\'' +
            ", id=" + id +
            '}';
    }
}
