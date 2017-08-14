package com.hzh.rpcframework.serialize;

import io.netty.buffer.ByteBuf;

import java.io.*;

public class SerializeUtil {
    public static byte[] readBuffer(ByteBuf buffer){
        byte[] bs = new byte[buffer.readableBytes()];
        buffer.readBytes(bs);
        return bs;
    }

    public static byte[] object2Bytes(Object obj) throws IOException{
        byte[] bs = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bs = bos.toByteArray();
        }finally {
            oos.close();
            bos.close();
        }
        return bs;
    }

    public static Object bytes2Object(byte[] bs) throws Exception{
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bs);
        ObjectInputStream ois = null;
        try{
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        }finally {
            ois.close();
            bis.close();
        }
        return  obj;
    }
}