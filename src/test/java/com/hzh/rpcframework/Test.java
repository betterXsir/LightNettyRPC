package com.hzh.rpcframework;

import com.hzh.rpcframework.clientstub.RPCMessageProxy;
import com.hzh.rpcframework.clientstub.RemoteServiceExecutor;
import com.hzh.rpcframework.services.Calculate;
import com.hzh.rpcframework.services.CalculateImpl;
import org.apache.commons.lang3.time.StopWatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class Test {
    private static CountDownLatch start = new CountDownLatch(1);
    private static CountDownLatch end = new CountDownLatch(2000);
    public static void main(String[] args){
        StopWatch sw = new StopWatch();
        sw.start();
        RemoteServiceExecutor executor = new RemoteServiceExecutor();
        executor.upstart();
        for(int i=0; i<2000; i++){
            System.out.println(i);
            Thread thread = new Thread(new CalculateThread(executor), "Thread-"+i);
            thread.start();
        }
        start.countDown();
        try {
            end.await();
        }catch (InterruptedException var){

        }
        sw.stop();
        String tip = String.format("RPC调用总共耗时 : [%s] 毫秒", sw.getTime());
        System.out.println(tip);
        executor.stop();
    }

    static class CalculateThread implements Runnable{
        private RemoteServiceExecutor executor;
        public CalculateThread(RemoteServiceExecutor executor){
            this.executor = executor;
        }
        @Override
        public void run() {
            try {
                start.await();
            }catch (InterruptedException var){

            }
            Random random = new Random();
            int m = random.nextInt(100);
            int n = random.nextInt(100);
            CalculateImpl func = new CalculateImpl();
            Calculate proxyCalculate = (Calculate) executor.execute(func);
            System.out.println(m+n + " equal: " + Thread.currentThread().getName() + " : " + proxyCalculate.add(m, n));
            end.countDown();
            return;
        }
    }
}
