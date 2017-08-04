package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.entity.InetAdress;
import com.hzh.rpcframework.entity.MessageReq;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter{
    //多个线程共用一个连接
    private ConcurrentHashMap<Long, MessageCallback> map = new ConcurrentHashMap<Long, MessageCallback>();
    private Channel ch;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.ch = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetAdress.MessageResp res = (InetAdress.MessageResp)msg;
        MessageCallback callback = map.get(res.getMessageId());
        if(callback != null)
            callback.setResult(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public MessageCallback sendMessage(MessageReq request){
        MessageCallback callback = new MessageCallback();
        map.put(request.getMessageId(), callback);
        ch.writeAndFlush(request);
        return callback;
    }
}
