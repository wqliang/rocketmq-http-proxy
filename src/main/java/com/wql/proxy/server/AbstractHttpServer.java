package com.wql.proxy.server;

import com.google.common.net.HttpHeaders;
import com.wql.proxy.common.ResponseUtil;
import com.wql.proxy.common.Triple;
import com.wql.proxy.common.protocol.HttpRequestCommand;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpServer {
    private static Logger logger = LoggerFactory.getLogger(AbstractHttpServer.class);
    /**
     * custom rpc hooks
     */
    protected List<RPCHook> rpcHooks = new ArrayList<RPCHook>();

    protected final HashMap<String/* url */, Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService>> processorTable =
        new HashMap<String, Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService>>(64);

    protected Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService> defaultRequestProcessor;

    public void processRequestReceived(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        processRequest(ctx, fullHttpRequest);
    }

    protected void doBeforeRpcHooks(String addr, FullHttpRequest request) {
        if (rpcHooks.size() > 0) {
            for (RPCHook rpcHook: rpcHooks) {
                rpcHook.doBeforeRequest(addr, request);
            }
        }
    }

    protected void doAfterRpcHooks(String addr, FullHttpRequest request, HttpResponseCommand response) {
        if (rpcHooks.size() > 0) {
            for (RPCHook rpcHook: rpcHooks) {
                rpcHook.doAfterResponse(addr, request, response);
            }
        }
    }


    /**
     * Process incoming request command issued by remote peer.
     *
     * @param ctx channel handler context.
     * @param request request .
     */
    public void processRequest(final ChannelHandlerContext ctx, final FullHttpRequest request) {
        final Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService> matched = this.processorTable.get(request.getUri());
        final Triple<HttpMethod, NettyHttpRequestProcessor, ExecutorService> triple = null == matched ? this.defaultRequestProcessor : matched;

        if (triple != null) {
            HttpRequestCommand requestCommand = parseRequestCommand(request);
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        doBeforeRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), request);

                        HttpMethod method = request.getMethod();
                        HttpMethod methodSupported = triple.getObject1();
                        if (method != methodSupported) {
                            //TODO: HTTP应该返回什么状态？
                            throw new Exception(String.format("%s not support %s", request.getUri(), method.name()));
                        }
                        final HttpResponseCallback callback = new HttpResponseCallback() {
                            @Override
                            public void onResponse(HttpResponseCommand response) {
                                doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), request, response);
                                if (response != null) {
                                    try {
                                        FullHttpResponse result = ResponseUtil.buildFullHttpResponse(response);
                                        ctx.writeAndFlush(result);
                                    } catch (Throwable e) {
                                        logger.error("process request over, but response failed", e);
                                        logger.error(request.toString());
                                        logger.error(response.toString());
                                    }
                                } else {
                                }
                            }
                        };
                        if (triple.getObject2() instanceof AsyncNettyHttpRequestProcessor) {
                            AsyncNettyHttpRequestProcessor processor = (AsyncNettyHttpRequestProcessor)triple.getObject2();
                            processor.asyncProcessRequest(ctx, requestCommand, callback);
                        } else {
                            NettyHttpRequestProcessor processor = triple.getObject2();
                            HttpResponseCommand responseCommand = processor.processRequest(ctx, requestCommand);
                            callback.onResponse(responseCommand);
                        }
                    } catch (Throwable e) {
                        logger.error("process request exception", e);
                        logger.error(request.toString());

                        final FullHttpResponse response = createErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR,
                            RemotingHelper.exceptionSimpleDesc(e));
                        ctx.writeAndFlush(response);
                    }
                }
            };

            if (triple.getObject2().rejectRequest()) {
                final FullHttpResponse response = createErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    "[REJECTREQUEST]system busy, start flow control for a while");
                ctx.writeAndFlush(response);
                return;
            }

            try {
                final RequestTask requestTask = new RequestTask(run, ctx.channel(), request);
                triple.getObject3().submit(requestTask);
            } catch (RejectedExecutionException e) {
                if ((System.currentTimeMillis() % 10000) == 0) {
                    logger.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel())
                        + ", too many requests and system thread pool busy, RejectedExecutionException "
                        + triple.getObject3().toString()
                        + " request uri: " + request.getUri());
                }

                final FullHttpResponse response = createErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR,
                    "[OVERLOAD]system busy, start flow control for a while");
                ctx.writeAndFlush(response);
            }
        } else {
            String error = " request uri " + request.getUri() + " not supported";
            final FullHttpResponse response = createErrorResponse(HttpResponseStatus.NOT_FOUND, error);
            ctx.writeAndFlush(response);
            logger.error(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) + error);
        }
    }

    private FullHttpResponse createErrorResponse(HttpResponseStatus status, String message) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(message.getBytes()));
        response.headers().add(HttpHeaders.CONTENT_LENGTH, message.length());
        return response;
    }

    private HttpRequestCommand parseRequestCommand(final FullHttpRequest request) {
        HttpRequestCommand cmd = new HttpRequestCommand();
        cmd.setUri(request.getUri());
        cmd.setMethod(request.getMethod());
        cmd.setVersion(request.getProtocolVersion());

        //parse http header
        for (String key : request.headers().names()) {
            cmd.getHeader().put(key, request.headers().get(key));
        }

        //parse http body
        int length = request.content().readableBytes();
        if (length > 0) {
            byte[] body = new byte[length];
            request.content().readBytes(body);
            cmd.setBody(body);
        }
        return cmd;
    }
}
