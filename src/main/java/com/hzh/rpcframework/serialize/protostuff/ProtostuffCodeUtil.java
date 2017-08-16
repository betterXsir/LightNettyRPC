package com.hzh.rpcframework.serialize.protostuff;

import com.google.common.io.Closer;
import com.hzh.rpcframework.serialize.MessageCodeUtil;
import io.netty.buffer.ByteBuf;

import java.io.*;

/**
 * Created by huzhenhua on 2017/8/16.
 */
public class ProtostuffCodeUtil implements MessageCodeUtil{
    private static Closer closer = Closer.create();

    private boolean selectMessage = false;

    public void setSelectMessage(boolean value){
        selectMessage = value;
    }

    @Override
    public void encode(Object object, ByteBuf byteBuf) throws IOException{
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            closer.register(baos);

            ProtostuffSerialize.serialize(baos, object);
            byte[] body = baos.toByteArray();
            byteBuf.writeBytes(body);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] bytes) throws IOException {
        Object res = null;
        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            closer.register(bais);

            res = ProtostuffSerialize.deserialize(bais, selectMessage);
        }finally {
            closer.close();
        }
        return res;
    }
}
