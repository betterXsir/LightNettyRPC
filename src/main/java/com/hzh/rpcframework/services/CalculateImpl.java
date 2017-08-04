package com.hzh.rpcframework.services;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class CalculateImpl implements Calculate{
    public Integer add(int m,int n){
        Integer result = Integer.valueOf(m+n);
        System.out.println(result);
        return result;
    }
}
