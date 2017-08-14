package com.hzh.rpcframework.messagepack;

import com.hzh.rpcframework.serialize.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
//        final int length = byteBuf.readableBytes();
//        array = new byte[length];
//        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        array = SerializeUtil.readBuffer(byteBuf);
        list.add(SerializeUtil.bytes2Object(array));
    }
}
