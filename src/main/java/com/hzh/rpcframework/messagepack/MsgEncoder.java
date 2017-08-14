package com.hzh.rpcframework.messagepack;

import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.serialize.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MsgEncoder extends MessageToByteEncoder<Object>{
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] raw = SerializeUtil.object2Bytes(o);
        byteBuf.writeBytes(raw);
    }
}
