package com.hzh.rpcframework.serverstub;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcThreadPool {
    public static Executor getExecutor(int threads, int queues){
        String name = "RPCThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues > 0 ? new LinkedBlockingDeque<Runnable>(queues) : new LinkedBlockingDeque<Runnable>(),
                new ThreadFactoryBuilder().setNameFormat("RPCServer-Thread-%d").build(), new AbortPolicyWithReport(name));
    }
}