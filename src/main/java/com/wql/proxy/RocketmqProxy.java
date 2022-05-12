package com.wql.proxy;

import com.wql.proxy.common.ConfigUtil;
import com.wql.proxy.common.ProxyConfig;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import javax.security.auth.login.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketmqProxy {
    static Logger logger = LoggerFactory.getLogger(RocketmqProxy.class);

    public static void main(String[] args) {
        try {
            String confFile = System.getProperty("rocketmq.http.proxy.conf", "rocketmq-proxy.properties");
            InputStream in = new BufferedInputStream(new FileInputStream(confFile));
            Properties properties = new Properties();
            properties.load(in);

            ProxyConfig proxyConfig = new ProxyConfig();
            ConfigUtil.decodeConfig(proxyConfig, properties);

            ProxyController controller = new ProxyController(proxyConfig);
            controller.initialize();
            controller.start();
            logger.info("Rocketmq Http Proxy start success.");

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                private volatile boolean hasShutdown = false;
                private AtomicInteger shutdownTimes = new AtomicInteger(0);

                @Override
                public void run() {
                    synchronized (this) {
                        logger.info("Shutdown hook was invoked, {}", this.shutdownTimes.incrementAndGet());
                        if (!this.hasShutdown) {
                            this.hasShutdown = true;
                            long beginTime = System.currentTimeMillis();
                            controller.shutdown();
                            long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                            logger.info("Shutdown hook over, consuming total time(ms): {}", consumingTimeTotal);
                        }
                    }
                }
            }, "ShutdownHook"));
        } catch (Exception ex) {
            logger.error("Rocketmq Proxy start fail.", ex);
            System.exit(-1);
        }
    }
}
