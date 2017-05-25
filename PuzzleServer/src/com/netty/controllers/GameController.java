package com.netty.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dyuproject.protostuff.Tag;
import com.netty.controllers.UserController.LoginReq;
import com.netty.core.MysqlOperation;
import com.netty.core.ServerHandler;
import com.netty.util.ProtoStuffSerializerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.PemPrivateKey;

public class GameController {
	public static class LevelUpReq{
		@Tag(value=1)
		public int currentLevel;
		@Tag(value=2)
		public String acount;
	}
	public static class HintReq{
		@Tag(value=1)
		public String acount;
		@Tag(value=2)
		public int hintcount;
	}
	 private static Log log = LogFactory.getLog(ServerHandler.class);  
	 private static GameController instance=new GameController();
	 public static GameController getInstance() {
		return instance;
	} 

	public void LevelUp(byte[] byt,ChannelHandlerContext ctx){
		LevelUpReq req=ProtoStuffSerializerUtil.deserialize(byt, LevelUpReq.class);
		MysqlOperation.getInstance().UpdateOther("currentLevel",Integer.toString( req.currentLevel+1), req.acount);
		UserInfo resp=MysqlOperation.getInstance().QueryUserInfo("acount", req.acount);
		//ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp, RespDataID.UserInfoResp);
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.writeAndFlush(buf);

	}
	public void updateHintCount(byte[] byt,ChannelHandlerContext ctx) {
		HintReq req=ProtoStuffSerializerUtil.deserialize(byt, HintReq.class);
		MysqlOperation.getInstance().UpdateOther("hintcount",Integer.toString( req.hintcount), req.acount);
		UserInfo resp=MysqlOperation.getInstance().QueryUserInfo("acount", req.acount);
		//ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp, RespDataID.UserInfoResp);
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.writeAndFlush(buf);
	}
}
