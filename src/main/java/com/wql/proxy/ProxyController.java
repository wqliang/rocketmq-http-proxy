package com.wql.proxy;

import com.wql.proxy.common.AllowedURI;
import com.wql.proxy.common.NettyHttpServerConfig;
import com.wql.proxy.common.ProxyConfig;
import com.wql.proxy.processor.AdminRequestProcessor;
import com.wql.proxy.processor.RequestProcessor;
import com.wql.proxy.producer.ProducerManager;
import com.wql.proxy.server.NettyHttpRequestProcessor;
import com.wql.proxy.server.NettyHttpServer;
import io.netty.handler.codec.http.HttpMethod;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.rocketmq.common.ThreadFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyController {
    static Logger logger = LoggerFactory.getLogger(ProxyController.class);
    private ProxyConfig proxyConfig;
    private NettyHttpServer nettyHttpServer;
    private NettyHttpRequestProcessor requestProcessor;
    private ExecutorService requestExecutor;
    private BlockingQueue<Runnable> requestThreadPoolQueue;
    private NettyHttpRequestProcessor adminProcessor;
    private ExecutorService adminRequestExecutor;
    private BlockingQueue<Runnable> adminThreadPoolQueue;

    private ProducerManager producerManager;

    public ProxyController(final ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public void initialize() {
        NettyHttpServerConfig serverConfig = proxyConfig.getNettyHttpServerConfig();
        nettyHttpServer = new NettyHttpServer(serverConfig);

        requestProcessor = new RequestProcessor(this);
        requestThreadPoolQueue = new LinkedBlockingQueue<>(proxyConfig.getRequestThreadPoolQueueSize());
        requestExecutor = new ThreadPoolExecutor(
            proxyConfig.getRequestThreadNum(),
            proxyConfig.getRequestThreadMumMax(),
            1000 * 60,
            TimeUnit.MILLISECONDS,
            this.requestThreadPoolQueue,
            new ThreadFactoryImpl("RequestProcessThread_"));

        adminProcessor = new AdminRequestProcessor();
        adminThreadPoolQueue = new LinkedBlockingQueue<>(proxyConfig.getAdminThreadPoolQueueSize());
        adminRequestExecutor = new ThreadPoolExecutor(
            proxyConfig.getAdminThreadNum(),
            proxyConfig.getAdminThreadNumMax(),
            1000 * 60,
            TimeUnit.MILLISECONDS,
            this.adminThreadPoolQueue,
            new ThreadFactoryImpl("AdminProcessThread_"));


        registerHttpRequestProcessor();

        producerManager = new ProducerManager(proxyConfig);
        producerManager.initialize();
    }

    public void registerHttpRequestProcessor() {
        nettyHttpServer.registerProcessor(AllowedURI.SEND_MESSAGE, HttpMethod.POST, requestProcessor, requestExecutor);
        nettyHttpServer.registerProcessor(AllowedURI.SEND_BATCH_MESSAGE, HttpMethod.POST, requestProcessor, requestExecutor);

        nettyHttpServer.registerProcessor("/admin/ping", HttpMethod.POST, adminProcessor, adminRequestExecutor);
    }

    public void start() {
        if (nettyHttpServer != null) {
            nettyHttpServer.start();
        }
    }

    public ProducerManager getProducerManager() {
        return producerManager;
    }

    public void shutdown() {
        if (nettyHttpServer != null) {
            nettyHttpServer.shutdown();
        }
    }
}
