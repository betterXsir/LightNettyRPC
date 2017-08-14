package com.hzh.rpcframework.serverstub;

import com.hzh.rpcframework.entity.MessageKeyVal;
import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.hzh.rpcframework.services.*;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter{
    private Map<String, Object> handlerMap;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("message received");
        MessageReq req = (MessageReq)msg;
        MessageResp resp = new MessageResp();
        RpcTransactionProcessing.submit(new MessageRecvProcess(req, resp, handlerMap, ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
