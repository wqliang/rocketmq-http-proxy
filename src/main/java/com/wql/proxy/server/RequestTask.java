package com.wql.proxy.server;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public class RequestTask implements Runnable {
    private final Runnable runnable;
    private final long createTimestamp = System.currentTimeMillis();
    private final Channel channel;
    private final FullHttpRequest request;
    private boolean stopRun = false;

    public RequestTask(final Runnable runnable, final Channel channel, final FullHttpRequest request) {
        this.runnable = runnable;
        this.channel = channel;
        this.request = request;
    }

    @Override
    public int hashCode() {
        int result = runnable != null ? runnable.hashCode() : 0;
        result = 31 * result + (int) (getCreateTimestamp() ^ (getCreateTimestamp() >>> 32));
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (isStopRun() ? 1 : 0);
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RequestTask task = (RequestTask) o;
        return createTimestamp == task.createTimestamp &&
            stopRun == task.stopRun &&
            runnable.equals(task.runnable) &&
            channel.equals(task.channel) &&
            request.equals(task.request);
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public boolean isStopRun() {
        return stopRun;
    }

    public void setStopRun(final boolean stopRun) {
        this.stopRun = stopRun;
    }

    @Override
    public void run() {
        if (!this.stopRun)
            this.runnable.run();
    }

    public void returnResponse(int code, String remark) {
        final RemotingCommand response = RemotingCommand.createResponseCommand(code, remark);
//        response.setOpaque(request.getOpaque());
        this.channel.writeAndFlush(response);
    }
}
