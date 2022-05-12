package com.wql.proxy.server;

import com.wql.proxy.common.protocol.HttpRequestCommand;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.rocketmq.remoting.netty.RemotingResponseCallback;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public abstract class AsyncNettyHttpRequestProcessor implements NettyHttpRequestProcessor {
    public void asyncProcessRequest(
        ChannelHandlerContext ctx, HttpRequestCommand request, HttpResponseCallback responseCallback) throws Exception {
        HttpResponseCommand response = processRequest(ctx, request);
        responseCallback.onResponse(response);
    }
}
