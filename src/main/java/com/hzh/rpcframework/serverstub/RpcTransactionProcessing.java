package com.hzh.rpcframework.serverstub;

import com.hzh.rpcframework.messagepack.MsgDecoder;
import com.hzh.rpcframework.messagepack.MsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by huzhenhua on 2017/8/3.
 */
public class RpcTransactionProcessing {
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private int port;

    public RpcTransactionProcessing(){
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        port = 8080;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void load() throws InterruptedException{
        ServerBootstrap b = new ServerBootstrap();
        /**
         * option() is for the NioServerSocketChannel that accepts incoming connections.
         * childOption() is for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
         */
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                        socketChannel.pipeline().addLast("msgpack decoder", new MsgDecoder());
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast("msgpack encoder", new MsgEncoder());
                        socketChannel.pipeline().addLast(new MessageRecvHandler());
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
