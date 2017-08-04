package com.hzh.rpcframework.serverstub;

/**
 * @Description: 加载rpcserver
 * Created by huzhenhua on 2017/8/3.
 */
public class RpcServerManage {
    private static RpcTransactionProcessing rpcBackService = new RpcTransactionProcessing();
    public static void start() throws Exception{
        rpcBackService.load();
    }
}
