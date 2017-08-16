package com.hzh.rpcframework.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.hzh.rpcframework.entity.MessageReq;

import java.io.*;

/**
 * Created by huzhenhua on 2017/8/15.
 */
public class KryoTest {
    public void testObject(){
        MessageReq req = new MessageReq();
        req.setMessageId(1);
        req.setClassName("com.hzh.rpcframework.serialize.SerializeUtil");
        req.setMethodName("write");
        Class<?>[] types = new Class<?>[]{Integer.class, String.class};
        Object[] parameters = new Object[]{5,"dfjk"};
        req.setTypeParameters(types);
        req.setParameterVals(parameters);
        byte[] bs = serializeObject(req);
        MessageReq reqNew = deserializeObject(bs, req.getClass());
        System.out.println(reqNew);
    }
    private <T extends Serializable> byte[] serializeObject(T object){
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(object.getClass(), new JavaSerializer());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output out = new Output(bos);
        kryo.writeClassAndObject(out, object);
        out.flush();
        out.close();
        byte[] bs = bos.toByteArray();
        try {
            bos.flush();
            bos.close();
        }catch (IOException var){
            var.printStackTrace();
        }
        return bs;
    }

    private <T extends Serializable> T deserializeObject(byte[] bs, Class<?> clazz){
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(clazz, new JavaSerializer());
        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
        Input input = new Input(bais);
        return (T)kryo.readClassAndObject(input);
    }

    public static void main(String[] args) {
        KryoTest kryoTest = new KryoTest();
        kryoTest.testObject();
    }
}
