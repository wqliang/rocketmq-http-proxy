package com.wql.proxy.server;

import com.wql.proxy.common.Triple;
import io.netty.handler.codec.http.HttpMethod;
import java.util.concurrent.ExecutorService;

public interface NettyServer<T> {
    void start();

    void shutdown();

    void registerRPCHook(RPCHook rpcHook);

    void registerProcessor(final T urlOrCode, final HttpMethod method, final NettyHttpRequestProcessor processor,
        final ExecutorService executor);

    void registerDefaultProcessor(final NettyHttpRequestProcessor processor, final ExecutorService executor);

    int localListenPort();

    Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService> getProcessorPair(final String uri);
}
