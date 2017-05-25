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
        //配置服务端的NIO线程组  
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
                            }).option(ChannelOption.SO_BACKLOG, 1024) //最大客户端连接数为1024 
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
            //绑定端口，同步等待成功  
            ChannelFuture f = b.bind(port).sync();  
            //等待服务端监听端口关闭  
            f.channel().closeFuture().sync();  
        } finally {  
            //优雅退出，释放线程池资源  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
}  