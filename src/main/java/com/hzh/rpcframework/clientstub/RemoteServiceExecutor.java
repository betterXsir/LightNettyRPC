package com.hzh.rpcframework.clientstub;

import java.lang.reflect.Proxy;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class RemoteServiceExecutor {
    private RpcServiceLoad rpcService;

    public RemoteServiceExecutor(){
        rpcService = RpcServiceLoad.getInstance();
    }

    public void upstart(){
        rpcService.load();
    }

    public void stop(){
        rpcService.unload();
    }

    public Object execute(Object obj){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new RPCMessageProxy(obj));
    }
}
