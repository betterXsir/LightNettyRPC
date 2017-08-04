package com.hzh.rpcframework;

import com.hzh.rpcframework.serverstub.RpcServerManage;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class RpcServerTest {
    public static void main(String[] args) {
        try {
            RpcServerManage.start();
        }catch (Exception var){
            var.printStackTrace();
        }
    }
}
