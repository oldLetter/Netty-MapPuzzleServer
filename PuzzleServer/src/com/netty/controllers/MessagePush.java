 package com.netty.controllers;

import com.mysql.fabric.Server;
import com.netty.core.ServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

public class MessagePush extends Thread {
    public void run()  
    {  
    	while(true){
    		while(true){
    			try {
					Thread.sleep(3000);
					if(ServerHandler.channelGroup.size()>0){
						ByteBuf resp = Unpooled.copiedBuffer("woqu".getBytes());  
						ServerHandler.channelGroup.writeAndFlush(resp);
					}    			
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    } 
}
