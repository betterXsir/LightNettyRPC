package com.hzh.rpcframework.serialize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

/**
 * Created by huzhenhua on 2017/8/16.
 */
public interface MessageCodeUtil {
    public final static int MESSAGE_LENGTH = 4;
    void encode(Object object, ByteBuf byteBuf) throws IOException;
    Object decode(byte[] bytes) throws IOException;
}
