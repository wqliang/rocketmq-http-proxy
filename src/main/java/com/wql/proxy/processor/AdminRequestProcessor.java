package com.wql.proxy.processor;

import com.google.common.net.HttpHeaders;
import com.wql.proxy.common.ResponseUtil;
import com.wql.proxy.common.protocol.HttpRequestCommand;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import com.wql.proxy.server.NettyHttpRequestProcessor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class AdminRequestProcessor implements NettyHttpRequestProcessor {
    @Override
    public HttpResponseCommand processRequest(ChannelHandlerContext ctx, HttpRequestCommand request) throws Exception {
        switch (request.getUri()) {
            case "/admin/ping": {
                return ping(ctx, request);
            }
        }
        return null;
    }

    private HttpResponseCommand ping(ChannelHandlerContext ctx, HttpRequestCommand request) {
        byte[] ret = "pong".getBytes();
        HttpResponseCommand response = new HttpResponseCommand();
        response.setStatus(HttpResponseStatus.OK);
        response.setBody(ret);
        response.setVersion(request.getVersion());

        return response;
    }

    @Override public boolean rejectRequest() {
        return false;
    }
}
