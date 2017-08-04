package com.hzh.rpcframework.clientstub;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class RemoteServiceExecutor {
    private RpcServiceLoad rpcService;

    public RemoteServiceExecutor(){
        rpcService = RpcServiceLoad.getInstance();
    }

    public void setInetAdress(String host, int port){
        rpcService.setInetAdress(host, port);
    }

    public void upstart(){
        rpcService.load();
    }

    public void stop(){
        rpcService.unload();
    }

    public Object execute(Object obj){
        return RPCMessageProxy.getProxyInstance(obj);
    }
}
