package com.wql.proxy.server;

import com.wql.proxy.common.protocol.HttpRequestCommand;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface NettyHttpRequestProcessor {
    HttpResponseCommand processRequest(ChannelHandlerContext ctx, HttpRequestCommand request)
        throws Exception;

    boolean rejectRequest();
}
