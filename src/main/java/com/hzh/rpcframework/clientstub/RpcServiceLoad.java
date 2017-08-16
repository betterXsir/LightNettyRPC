package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.serialize.MsgDecoder;
import com.hzh.rpcframework.serialize.MsgEncoder;
import com.hzh.rpcframework.serialize.protostuff.ProtostuffCodeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: rpc服务器配置加载
 * Created by huzhenhua on 2017/8/2.
 */
public class RpcServiceLoad {
    private String host;
    private int port;

    private ArrayList<MessageSendHandler> messageSendHandlers;
    private static volatile RpcServiceLoad loader;

    private ReentrantLock lock;
    private Condition signal;
    //java虚拟机可用的处理器数量
    private final static int parallel = Runtime.getRuntime().availableProcessors()*2;
    private NioEventLoopGroup group;

    public static RpcServiceLoad getInstance(){
        if(loader == null){
            synchronized (RpcServiceLoad.class){
                if(loader == null){
                    loader = new RpcServiceLoad();
                }
            }
        }
        return loader;
    }

    public RpcServiceLoad(){
        messageSendHandlers = new ArrayList<>();
        lock = new ReentrantLock();
        signal = lock.newCondition();
        group = new NioEventLoopGroup(parallel);
        host = "127.0.0.1";
        port = 8080;
    }

    public void setInetAdress(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void load(){
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ProtostuffCodeUtil util = new ProtostuffCodeUtil();
                        util.setSelectMessage(true);
                        socketChannel.pipeline().addLast("frameDecoder",
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                        socketChannel.pipeline().addLast("msgpack decoder", new MsgDecoder(util));
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                        socketChannel.pipeline().addLast("msgpack encoder", new MsgEncoder(util));
                        socketChannel.pipeline().addLast(new MessageSendHandler());
                    }
                });
        ChannelFuture f = b.connect(host, port);

        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.printf("%s","connect completed");
                    MessageSendHandler messageHandlder = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                    try{
                        lock.lock();
                        messageSendHandlers.add(messageHandlder);
                        signal.signalAll();
                    }finally {
                        lock.unlock();
                    }
                }
            }
        });
    }

    public MessageSendHandler getMessageHandlder() throws InterruptedException{
        try{
            lock.lock();
            if(messageSendHandlers.isEmpty())
                signal.await();
            return messageSendHandlers.get(0);
        }finally {
            lock.unlock();
        }
    }

    public void unload(){
        MessageSendHandler handler = messageSendHandlers.remove(0);
        handler.close();
        group.shutdownGracefully();
    }
}
