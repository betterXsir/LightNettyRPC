package com.hzh.rpcframework.serverstub;

import com.hzh.rpcframework.entity.MessageKeyVal;
import com.hzh.rpcframework.serialize.MsgDecoder;
import com.hzh.rpcframework.serialize.MsgEncoder;
import com.hzh.rpcframework.serialize.protostuff.ProtostuffCodeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class RpcTransactionProcessing implements ApplicationContextAware, InitializingBean {
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private int port = 8080;

    private static ThreadPoolExecutor threadPoolExecutor;

    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    public RpcTransactionProcessing(){

    }

    public RpcTransactionProcessing(int port){
        this.port = port;
    }

    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (MessageRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            MessageKeyVal keyVal = (MessageKeyVal) applicationContext.getBean(Class.forName("com.hzh.rpcframework.entity.MessageKeyVal"));
            Map<String, Object> map = keyVal.getVal();
            Set set = map.entrySet();
            Iterator<Map.Entry<String, Object>> it = set.iterator();
            Map.Entry<String, Object> entry = null;

            while (it.hasNext()){
                entry = it.next();
                handlerMap.put(entry.getKey(), entry.getValue());
            }
        }catch (ClassNotFoundException var0){
            var0.printStackTrace();
        }
    }

    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 204)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ProtostuffCodeUtil util = new ProtostuffCodeUtil();
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                        socketChannel.pipeline().addLast("msgpack encoder", new MsgEncoder(util));
                        socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        socketChannel.pipeline().addLast("msgpack decoder", new MsgDecoder(util));
                        socketChannel.pipeline().addLast(new MessageRecvHandler(handlerMap));
                    }
                });
        try {
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
