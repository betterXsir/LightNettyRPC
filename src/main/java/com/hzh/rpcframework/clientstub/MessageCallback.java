package com.hzh.rpcframework.clientstub;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huzhenhua on 2017/8/2.
 */
public class MessageCallback {
    private Object result;

    public void setResult(Object result) {
        synchronized (this) {
            this.result = result;
            this.notifyAll();
        }

    }

    public Object getResult() throws InterruptedException{
        synchronized (this){
            if(result == null)
                this.wait();
            return result;
        }
    }
}
