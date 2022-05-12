package com.wql.proxy.processor;

import com.wql.proxy.ProxyController;
import com.wql.proxy.common.ProxyMessageQueue;
import com.wql.proxy.common.protocol.HttpRequestCommand;
import com.wql.proxy.common.protocol.HttpResponseCommand;
import com.wql.proxy.common.protocol.commonds.SendMessageCommand;
import com.wql.proxy.common.serializer.JSONSerializer;
import com.wql.proxy.server.NettyHttpRequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.wql.proxy.common.AllowedURI.*;

public class RequestProcessor implements NettyHttpRequestProcessor {
    private static Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private final ProxyController controller;

    public RequestProcessor(final ProxyController controller) {
        this.controller = controller;
    }

    @Override
    public HttpResponseCommand processRequest(ChannelHandlerContext ctx, HttpRequestCommand request) throws Exception {
        switch (request.getUri()) {
            case SEND_MESSAGE: {
                return sendMessage(ctx, request);
            }
            case SEND_BATCH_MESSAGE: {
                return sendBatchMessage(ctx, request);
            }
        }
        return null;
    }

    @Override public boolean rejectRequest() {
        return false;
    }

    public HttpResponseCommand sendMessage(ChannelHandlerContext ctx, HttpRequestCommand request) {
        //decode body
        logger.info("receive request, {}", request);
        HttpResponseCommand response = new HttpResponseCommand();
        response.setVersion(request.getVersion());

        SendMessageCommand cmd = SendMessageCommand.decode(request.getBody());

        //do params validation

        //create message
        Message message = new Message();
        message.setTopic(cmd.getTopic());
        message.setBody(cmd.getBody().getBytes());
        if (StringUtils.isNotEmpty(cmd.getBuyerId())) {
            message.setBuyerId(cmd.getBuyerId());
        }
        if (cmd.getDelayTimeLevel() != null) {
            message.setDelayTimeLevel(cmd.getDelayTimeLevel());
        }
        if (cmd.getFlag() != null) {
            message.setFlag(cmd.getFlag());
        }
        if (StringUtils.isNotEmpty(cmd.getInstanceId())) {
            message.setInstanceId(cmd.getInstanceId());
        }
        if (StringUtils.isNotEmpty(cmd.getKeys())) {
            message.setKeys(cmd.getKeys());
        }
        if (StringUtils.isNotEmpty(cmd.getTags())) {
            message.setTags(cmd.getTags());
        }
        if (StringUtils.isNotEmpty(cmd.getTransactionId())) {
            message.setTransactionId(cmd.getTransactionId());
        }
        if (cmd.getWaitStoreMsgOK() != null) {
            message.setWaitStoreMsgOK(cmd.getWaitStoreMsgOK());
        }

        ProxyMessageQueue pmq = cmd.getQueue();
        MessageQueue mq = null;
        if (pmq != null) {
            mq = new MessageQueue();
            mq.setTopic(cmd.getTopic());
            mq.setQueueId(pmq.getId());
            mq.setBrokerName(pmq.getBrokerName());
        }
        //get producer
        DefaultMQProducer producer = controller.getProducerManager().getNextProducer(cmd.getGroup());

        //send message
        try {
            SendResult sendResult = null;
            if (mq == null) {
                sendResult = producer.send(message);
            }
            else {
                sendResult = producer.send(message, mq);
            }
            response.setStatus(HttpResponseStatus.OK);
            response.setBody(JSONSerializer.toJsonBytes(sendResult));
        } catch (Exception e) {
            logger.info("send message fail", e);
            response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        //return send result
        return response;
    }

    public HttpResponseCommand sendBatchMessage(ChannelHandlerContext ctx, HttpRequestCommand request) {
        //decode body

        //get producer

        //send message

        //return send result
        return new HttpResponseCommand();
    }
}
