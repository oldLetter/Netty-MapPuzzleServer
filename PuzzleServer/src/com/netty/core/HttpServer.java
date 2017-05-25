package com.netty.core;
  
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.ChannelFuture;   
import io.netty.channel.ChannelInitializer;   
import io.netty.channel.ChannelOption;   
import io.netty.channel.EventLoopGroup;   
import io.netty.channel.nio.NioEventLoopGroup;   
import io.netty.channel.socket.SocketChannel;   
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
  
public class HttpServer {  
	private static Log log = LogFactory.getLog(HttpServer.class);  

    public void start(int port) throws Exception {  
        //���÷���˵�NIO�߳���  
        EventLoopGroup bossGroup = new NioEventLoopGroup();  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap();  
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)  
                    .childHandler(new ChannelInitializer<SocketChannel>() {  
                                @Override  
                                public void initChannel(SocketChannel ch) throws Exception {  
                                	ch.pipeline().addLast("decoder", new LengthFieldBasedFrameDecoder(10*1024,0,4,0,4,true));  
                                	ch.pipeline().addLast("handler",new ServerHandler());
                                }  
                            }).option(ChannelOption.SO_BACKLOG, 1024) //���ͻ���������Ϊ1024 
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
            //�󶨶˿ڣ�ͬ���ȴ��ɹ�  
            ChannelFuture f = b.bind(port).sync();  
            //�ȴ�����˼����˿ڹر�  
            f.channel().closeFuture().sync();  
        } finally {  
            //�����˳����ͷ��̳߳���Դ  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
}  