package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.entity.MessageReq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class RPCMessageProxy implements InvocationHandler {
    private Object obj;
    private AtomicLong count;
    public RPCMessageProxy(Object obj){
        this.obj = obj;
        count = new AtomicLong();
    }

    public static Object getProxyInstance(Object obj){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new RPCMessageProxy(obj));
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageReq request = new MessageReq();
        request.setMessageId(count.getAndDecrement());
        request.setClassName(proxy.getClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(args.getClass().getClasses());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameterVals(args);
        //TODO 调用利用Netty发送请求的功能模块,此时RpcService已经成功加载
        MessageSendHandler handler = RpcServiceLoad.getInstance().getMessageHandlder();
        return null;
    }
}
