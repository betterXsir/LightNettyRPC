package com.hzh.rpcframework.serialize.protostuff;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by huzhenhua on 2017/8/16.
 */
public class SchemaCache {
    //基于类初始化单实例设计模式
    private static class SchemaCacheHolder{
        public static SchemaCache schemaCache = new SchemaCache();
    }
    public static SchemaCache getInstance(){
        return SchemaCacheHolder.schemaCache;
    }

    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    public Schema<?> getSchema(final Class<?> cls) throws ExecutionException{
        try{
            return cache.get(cls, new Callable<RuntimeSchema<?>>() {
                public RuntimeSchema<?> call() throws Exception {
                    return RuntimeSchema.createFrom(cls);
                }
            });
        }catch (ExecutionException var){
            var.printStackTrace();
            throw var;
        }
    }
}
