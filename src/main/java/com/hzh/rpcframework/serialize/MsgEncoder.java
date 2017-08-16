package com.hzh.rpcframework.serialize;

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
    private MessageCodeUtil util;
    public MsgEncoder(MessageCodeUtil util){
        this.util =  util;
    }
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        util.encode(o, byteBuf);
    }
}
