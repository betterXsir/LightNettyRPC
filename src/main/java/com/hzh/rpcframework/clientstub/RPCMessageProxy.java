package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.entity.MessageReq;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class RPCMessageProxy implements InvocationHandler {
    private Object obj;
    public RPCMessageProxy(Object obj){
        this.obj = obj;
    }

//    public static Object getProxyInstance(Object obj){
//        return Proxy.newProxyInstance(((Class)obj).getClassLoader(),
//                ((Class)obj).getInterfaces(),
//                new RPCMessageProxy(obj));
//    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageReq request = new MessageReq();
        request.setMessageId(new AtomicLong().getAndDecrement());
        request.setClassName(proxy.getClass().getName());
        request.setMethodName(method.getName());
        if(args != null && args.length > 0) {
            request.setTypeParameters(method.getParameterTypes());
            request.setParameterVals(args);
        }
        System.out.println(request.toString());
        // TODO: 2017/8/4 调用利用Netty发送请求的功能模块,此时RpcService已经成功加载
        MessageSendHandler handler = RpcServiceLoad.getInstance().getMessageHandlder();
        MessageCallback callback = handler.sendMessage(request);
        return callback.getResult();
    }
}
