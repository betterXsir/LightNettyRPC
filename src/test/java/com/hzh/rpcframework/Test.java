package com.hzh.rpcframework;

import com.hzh.rpcframework.clientstub.RemoteServiceExecutor;
import com.hzh.rpcframework.services.Calculate;
import com.hzh.rpcframework.services.CalculateImpl;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class Test {
    public static void main(String[] args) {
        RemoteServiceExecutor executor = new RemoteServiceExecutor();
        executor.upstart();
        Object obj = executor.execute(new CalculateImpl());
        ((Calculate)obj).add(1,2);
        executor.stop();
    }
}
