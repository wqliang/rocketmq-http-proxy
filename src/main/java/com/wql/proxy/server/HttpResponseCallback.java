package com.wql.proxy.server;

import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseCallback {
    void onResponse(HttpResponseCommand response);
}
