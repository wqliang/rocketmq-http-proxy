package com.wql.proxy.common;

public class ProxyConfig {
    private String namesrvAddress;

    private int requestThreadNum = 64;
    private int requestThreadMumMax = 64;
    private int requestThreadPoolQueueSize = 100000;

    private int adminThreadNum = 64;
    private int adminThreadNumMax = 64;
    private int adminThreadPoolQueueSize = 100000;

    private NettyHttpServerConfig nettyHttpServerConfig;

    public String getNamesrvAddress() {
        return namesrvAddress;
    }

    public void setNamesrvAddress(String namesrvAddress) {
        this.namesrvAddress = namesrvAddress;
    }

    public int getRequestThreadPoolQueueSize() {
        return requestThreadPoolQueueSize;
    }

    public void setRequestThreadPoolQueueSize(int requestThreadPoolQueueSize) {
        this.requestThreadPoolQueueSize = requestThreadPoolQueueSize;
    }

    public int getAdminThreadPoolQueueSize() {
        return adminThreadPoolQueueSize;
    }

    public void setAdminThreadPoolQueueSize(int adminThreadPoolQueueSize) {
        this.adminThreadPoolQueueSize = adminThreadPoolQueueSize;
    }

    public int getRequestThreadNum() {
        return requestThreadNum;
    }

    public void setRequestThreadNum(int requestThreadNum) {
        this.requestThreadNum = requestThreadNum;
    }

    public int getRequestThreadMumMax() {
        return requestThreadMumMax;
    }

    public void setRequestThreadMumMax(int requestThreadMumMax) {
        this.requestThreadMumMax = requestThreadMumMax;
    }

    public int getAdminThreadNum() {
        return adminThreadNum;
    }

    public void setAdminThreadNum(int adminThreadNum) {
        this.adminThreadNum = adminThreadNum;
    }

    public int getAdminThreadNumMax() {
        return adminThreadNumMax;
    }

    public void setAdminThreadNumMax(int adminThreadNumMax) {
        this.adminThreadNumMax = adminThreadNumMax;
    }

    public NettyHttpServerConfig getNettyHttpServerConfig() {
        return nettyHttpServerConfig;
    }

    public void setNettyHttpServerConfig(NettyHttpServerConfig nettyHttpServerConfig) {
        this.nettyHttpServerConfig = nettyHttpServerConfig;
    }
}
