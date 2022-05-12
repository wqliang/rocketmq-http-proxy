package com.wql.proxy.common;

import com.google.common.net.HttpHeaders;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ResponseUtil {
    public static FullHttpResponse createHttpResponse(HttpVersion version, HttpResponseStatus status, byte[] body) {
        FullHttpResponse response = null;
        if (body == null) {
            response = new DefaultFullHttpResponse(version, status);
            response.headers().add(HttpHeaders.CONTENT_LENGTH, 0);
        }
        else {
            response = new DefaultFullHttpResponse(version, status, Unpooled.wrappedBuffer(body));
            response.headers().add(HttpHeaders.CONTENT_LENGTH, body.length);
        }
        return response;
    }

    public static FullHttpResponse buildFullHttpResponse(final HttpResponseCommand responseCommand) {
        FullHttpResponse response = null;
        if (responseCommand.getBody() == null) {
            response = new DefaultFullHttpResponse(responseCommand.getVersion(), responseCommand.getStatus());
            response.headers().add(HttpHeaders.CONTENT_LENGTH, 0);
        }
        else {
            byte[] body = responseCommand.getBody();
            response = new DefaultFullHttpResponse(responseCommand.getVersion(), responseCommand.getStatus(),
                Unpooled.wrappedBuffer(body));
            response.headers().add(HttpHeaders.CONTENT_LENGTH, body.length);
        }

        if (responseCommand.getHeader() != null) {
            for (String key : responseCommand.getHeader().keySet()) {
                response.headers().add(key, responseCommand.getHeader().get(key));
            }
        }
        return response;
    }
}
