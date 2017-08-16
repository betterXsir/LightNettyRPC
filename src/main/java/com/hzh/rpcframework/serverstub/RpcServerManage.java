package com.hzh.rpcframework.serverstub;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description: 加载rpcserver
 * Created by huzhenhua on 2017/8/3.
 */
public class RpcServerManage {
    public static void start() throws Exception{
        new ClassPathXmlApplicationContext("rpc-invoke-config.xml");
    }
}
