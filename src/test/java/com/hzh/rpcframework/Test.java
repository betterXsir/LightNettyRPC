package com.hzh.rpcframework;

import com.hzh.rpcframework.clientstub.RPCMessageProxy;
import com.hzh.rpcframework.clientstub.RemoteServiceExecutor;
import com.hzh.rpcframework.services.Calculate;
import com.hzh.rpcframework.services.CalculateImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class Test {
    public static void main(String[] args){
        RemoteServiceExecutor executor = new RemoteServiceExecutor();
        executor.upstart();
        CalculateImpl func = new CalculateImpl();
        Calculate proxyCalculate = (Calculate)executor.execute(func);
        proxyCalculate.add(2,3);
        executor.stop();
    }
}
