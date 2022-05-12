package com.wql.proxy.server;

import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface RPCHook {
    void doBeforeRequest(final String remoteAddr, final FullHttpRequest request);

    void doAfterResponse(final String remoteAddr, final FullHttpRequest request, final HttpResponseCommand response);
}
