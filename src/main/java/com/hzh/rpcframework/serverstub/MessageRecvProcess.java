package com.hzh.rpcframework.serverstub;

import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class MessageRecvProcess implements Runnable{
    private MessageReq request;
    private MessageResp response;

    private Map<String, Object> handlerMap;

    private ChannelHandlerContext ctx;

    public MessageRecvProcess(MessageReq request, MessageResp response, Map<String, Object> handlerMap, ChannelHandlerContext ctx){
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
        this.ctx = ctx;
    }
    @Override
    public void run() {
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            response.setResult(result);
        }catch (Throwable throwable){
            response.setStatus(throwable.toString());
            throwable.printStackTrace();
            System.out.println("RPC Server invoke error.");
        }
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("RPC Server Send message-id response:" + response.getMessageId());
            }
        });
    }

    public Object reflect(MessageReq req) throws Throwable{
        String className = req.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = req.getMethodName();
        Object[] parameters = req.getParameterVals();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}
