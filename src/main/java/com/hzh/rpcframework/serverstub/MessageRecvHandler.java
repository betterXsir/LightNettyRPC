package com.hzh.rpcframework.serverstub;

import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.hzh.rpcframework.services.*;

import java.lang.reflect.Method;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq req = (MessageReq)msg;
        Class classobject = null;
        MessageResp resp = new MessageResp();
        resp.setMessageId(req.getMessageId());
        Object service = null;
        Method method = null;
        Object res = null;
        System.out.println("message received");
        try {
            classobject = Class.forName("com.hzh.rpcframework.services." + req.getClassName());
            service = classobject.newInstance();
            method = classobject.getMethod(req.getMethodName(), req.getTypeParameters());
        }catch (ClassNotFoundException | NoSuchMethodException var4){
            resp.setStatus("ClassName or MethodName or ParameterType Error");
        }
        if(method != null) {
            try {
                res = classobject.getMethod(req.getMethodName(), req.getTypeParameters())
                        .invoke(service, req.getParameterVals());
                resp.setResult(res);
                resp.setStatus("OK");
            } catch (Exception var4) {
                MessageResp result = new MessageResp();
                result.setMessageId(req.getMessageId());
                result.setResult(null);
                result.setStatus("Service Error");
            }
        }
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
