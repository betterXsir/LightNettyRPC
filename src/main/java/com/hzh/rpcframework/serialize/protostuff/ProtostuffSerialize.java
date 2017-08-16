package com.hzh.rpcframework.serialize.protostuff;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by huzhenhua on 2017/8/16.
 */
public class ProtostuffSerialize {
    private static SchemaCache schemaCache = SchemaCache.getInstance();
    private static Objenesis objenesis = new ObjenesisStd();

    public static void serialize(OutputStream out, Object object){
        Class<?> cls = object.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            Schema schema = schemaCache.getSchema(cls);
            ProtostuffIOUtil.writeTo(out, object, schema, buffer);
        }catch (Exception var4){
            throw new IllegalStateException(var4.getMessage(), var4);
        }finally {
            buffer.clear();
        }
    }

    public static Object deserialize(InputStream in, boolean selectMessage){
        try{
            Class<?> cls = selectMessage ? MessageReq.class : MessageResp.class;
            Object message = (Object)objenesis.newInstance(cls);
            Schema schema = schemaCache.getSchema(cls);
            ProtostuffIOUtil.mergeFrom(in, message, schema);
            return message;
        }catch (Exception var5){
            throw new IllegalStateException(var5.getMessage(), var5);
        }
    }
}
