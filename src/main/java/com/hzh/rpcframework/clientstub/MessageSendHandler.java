package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter{
    //多个线程共用一个连接
    private ConcurrentHashMap<Long, MessageCallback> map = new ConcurrentHashMap<Long, MessageCallback>();
    private Channel ch;

    public Channel getCh(){
        return ch;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.ch = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageResp res = (MessageResp)msg;
        System.out.println("receive message: " + res);
        MessageCallback callback = map.get(res.getMessageId());
        if(callback != null)
            callback.setResult(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public void close(){
        //冲洗所有写回的数据，并在flush完成时关闭channel
        ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public MessageCallback sendMessage(MessageReq request){
        MessageCallback callback = new MessageCallback();
        map.put(request.getMessageId(), callback);
        System.out.println("message sended");
        ch.writeAndFlush(request);
        System.out.println("send message: "+request);
        return callback;
    }
}
