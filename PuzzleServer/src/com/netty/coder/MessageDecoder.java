package com.netty.coder;

import java.util.Arrays;
import java.util.List;

import javax.xml.crypto.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netty.proto.*;
import com.netty.controllers.GameController.HintReq;
import com.netty.controllers.GameController.LevelUpReq;
import com.netty.controllers.UserController.*;
import com.netty.core.ServerHandler;
import com.netty.util.CoderUtil;
import com.netty.util.ProtoStuffSerializerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder{
    private static Log log = LogFactory.getLog(ServerHandler.class);  
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
		// TODO Auto-generated method stub
		byte[] temp=new byte[arg1.readableBytes()];
		byte[] date=new byte[arg1.readableBytes()-4];
		byte[] bytID=new byte[4];
		arg1.readBytes(temp);
		System.arraycopy(temp, 0, bytID, 0, 4);
		System.arraycopy(temp, 4, date, 0, temp.length-4);
		int id=CoderUtil.bytesToInt(bytID, 0);
		if(id==ReqDataID.LoginReq){
			LoginReq msg=ProtoStuffSerializerUtil.deserialize(date, LoginReq.class);
			arg2.add(msg);
		}
		else if (id==ReqDataID.RegistReq) {
			RegistReq msg=ProtoStuffSerializerUtil.deserialize(date, RegistReq.class);
			arg2.add(msg);
		}
		else if(id==ReqDataID.UserInfoReq){
			UserInfoReq msg=ProtoStuffSerializerUtil.deserialize(date, UserInfoReq.class);
			arg2.add(msg);
		}
		else if(id==ReqDataID.LevelUpReq){
			LevelUpReq msg=ProtoStuffSerializerUtil.deserialize(date, LevelUpReq.class);
			arg2.add(msg);
		}
		else if(id==ReqDataID.HintReq) {
			HintReq msg=ProtoStuffSerializerUtil.deserialize(date, HintReq.class);
			arg2.add(msg);
		}
	}
}
