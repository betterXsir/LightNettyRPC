package com.hzh.rpcframework.clientstub;

import com.hzh.rpcframework.entity.InetAdress;
import com.hzh.rpcframework.messagepack.MsgDecoder;
import com.hzh.rpcframework.messagepack.MsgEncoder;
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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: rpc服务器配置加载
 * Created by huzhenhua on 2017/8/2.
 */
public class RpcServiceLoad {
    private String host;
    private int port;
    private MessageSendHandler messageHandlder;
    private static volatile RpcServiceLoad loader;
    private ReentrantLock lock;
    private Condition signal;
    // TODO: 2017/8/3 根据cpu并发数来初始化netty线程组的个数
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
        messageHandlder = null;
        lock = new ReentrantLock();
        signal = lock.newCondition();
        group = new NioEventLoopGroup();
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
                        // TODO: 2017/8/2 增加粘包半包支持,编码，解码
                        socketChannel.pipeline().addLast("frameDecoder",
                                new LengthFieldBasedFrameDecoder(65535, 0, 2,0,2));
                        socketChannel.pipeline().addLast("msgpack decoder", new MsgDecoder());
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast("msgpack encoder", new MsgEncoder());
                        socketChannel.pipeline().addLast(new MessageSendHandler());
                    }
                });
        ChannelFuture f = b.connect(host, port);

        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    messageHandlder = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                }
            }
        });
    }

    public void setMessageHandlder(MessageSendHandler handlder){
        try {
            lock.lock();
            messageHandlder = handlder;
            signal.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageHandlder() throws InterruptedException{
        try{
            lock.lock();
            if(messageHandlder == null){
                signal.await();
            }
            return messageHandlder;
        }catch (InterruptedException var1){
            throw var1;
        }finally {
            lock.unlock();
        }
    }

    public void unload(){
        group.shutdownGracefully();
    }
}
