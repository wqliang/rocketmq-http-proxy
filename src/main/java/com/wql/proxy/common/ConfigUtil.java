package com.wql.proxy.common;

import java.util.Properties;

public class ConfigUtil {
    public static void decodeConfig(final ProxyConfig config, final Properties properties) {
        if (config != null && properties != null) {
            config.setRequestThreadMumMax(Integer.parseInt(properties.getProperty("requestThreadNumMax", "64")));
            config.setRequestThreadNum(Integer.parseInt(properties.getProperty("requestThreadNum", "64")));
            config.setRequestThreadPoolQueueSize(Integer.parseInt(properties.getProperty("requestThreadPoolQueueSize", "100000")));
            config.setAdminThreadNum(Integer.parseInt(properties.getProperty("adminThreadNum", "64")));
            config.setAdminThreadNumMax(Integer.parseInt(properties.getProperty("adminThreadNumMax", "64")));
            config.setAdminThreadPoolQueueSize(Integer.parseInt(properties.getProperty("adminThreadPoolQueueSize", "100000")));

            NettyHttpServerConfig nettyHttpServerConfig = new NettyHttpServerConfig();
            nettyHttpServerConfig.setListenPort(Integer.parseInt(properties.getProperty("listenPort", "9898")));

            config.setNamesrvAddress(properties.getProperty("namesrvAddress"));

            config.setNettyHttpServerConfig(nettyHttpServerConfig);
        }
    }
}
