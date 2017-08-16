package com.hzh.rpcframework.serialize;

import com.hzh.rpcframework.serialize.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class MsgDecoder extends ByteToMessageDecoder {
    private MessageCodeUtil util;

    public MsgDecoder(MessageCodeUtil util){
        this.util = util;
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() < MessageCodeUtil.MESSAGE_LENGTH){
            return;
        }
        byteBuf.markReaderIndex();
        //读取消息的内容长度
        int messageLenth = byteBuf.readInt();

        if(messageLenth < 0){
            channelHandlerContext.close();
        }

        if(byteBuf.readableBytes() < messageLenth){
            byteBuf.resetReaderIndex();
            return;
        }
        else{
            byte[] array = new byte[messageLenth];
            byteBuf.readBytes(array);
            try{
                Object object = util.decode(array);
                list.add(object);
            }catch (IOException var){
                Logger.getLogger(MsgDecoder.class.getName()).log(Level.SEVERE,null,var);
            }
        }

    }
}
