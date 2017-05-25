package com.netty.util;

import java.util.ArrayList;
import java.util.Map;

import com.mysql.fabric.Server;
import com.netty.core.MysqlOperation;
import com.netty.core.ServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

public class Test {
	public static void main(String[] args) throws Exception {

		while(true){
			Channel channel=ServerHandler.channelGroup.find((ChannelId)ServerHandler.usermap.getKey("whh"));
			channel.writeAndFlush("fdfaf");
			Thread.sleep(2000);
		}
	}
	
}
