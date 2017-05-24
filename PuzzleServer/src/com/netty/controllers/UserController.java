package com.netty.controllers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.xml.crypto.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.BaseXSSFEvaluationWorkbook;

import com.dyuproject.protostuff.Tag;
import com.mysql.fabric.Server;
import com.netty.controllers.AnswerController.PapersScoreResp;
import com.netty.controllers.UserController.LoginReq;
import com.netty.core.MysqlOperation;
import com.netty.core.ServerHandler;
import com.netty.proto.RespDataID;
import com.netty.util.DbcpPool;
import com.netty.util.ProtoStuffSerializerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class UserController {
	public static class InitDataResp
	{
		@Tag (value =1)
	    public UserInfo userinfo;
		@Tag (value =2)
	    public PapersScoreResp screlist;
	    public InitDataResp()
	    {
	        userinfo = new UserInfo();
	        screlist = new PapersScoreResp();
	    }
	}
	public static class LoginReq{
		@Tag (value =1)
		public String usr;
		@Tag(value=2)
		public String pwd;
	}

	public static class LoginResp{
		public static int ACOUNT_INEXISTENCE=0;
		public static int PWD_ERROR=1;
		public static int LOGIN_SUCCESS=2;
		public  static int USER_ISLOGIN=3;
		@Tag (value =1)
		public int result;
		
		public LoginResp() {
			// TODO Auto-generated constructor stub
		}
	}
	
	
	public static class RegistReq{
		@Tag (value =1)
		public String acount;
		@Tag(value=2)
		public String pwd;
	}
	public static class RegistResp{
		public static int ACOUNT_EXIST=0;
		public static int REGIST_SUCCESS=1;

		@Tag (value =1)
		public int result;
		
		public RegistResp() {
			// TODO Auto-generated constructor stub
		}
	}
		 
	public static class UserInfoReq{
		@Tag(value=1)
		public String acount;
	}

    private static Log log = LogFactory.getLog(ServerHandler.class);  
    private static UserController userController=new UserController();
    
    public static UserController getInstance() {
		return userController;
	}
	public void Login(byte[] byt,ChannelHandlerContext ctx) {
		LoginReq req=ProtoStuffSerializerUtil.deserialize(byt, LoginReq.class);
		LoginResp resp=new LoginResp();
		//MysqlOperation.Connect();
		if(MysqlOperation.getInstance().QueryOne("acount", req.usr)){
			if(MysqlOperation.getInstance().QueryOne("pwd", req.pwd)){
				if(ServerHandler.usermap.containsValue(req.usr)){
					resp.result=LoginResp.USER_ISLOGIN;
				}
				else {
					resp.result=LoginResp.LOGIN_SUCCESS;
					ServerHandler.usermap.put(ctx.channel().id().toString(), req.usr);
				}
				presentHintCount(req.usr);
			}else {
				resp.result=LoginResp.PWD_ERROR;
			}
		}
		else{
			resp.result = LoginResp.ACOUNT_INEXISTENCE;
		}
//		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp, RespDataID.LoginResp);
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.write(buf);
		ctx.flush();
	}
	public void presentHintCount(String acount) {
		Date date1=new Date(new java.util.Date().getTime());
		Date current = java.sql.Date.valueOf(date1.toString());
		Date before=MysqlOperation.getInstance().QueryDate("date","acount",acount );
		if(before==null){
			before=current;
			MysqlOperation.getInstance().UpdateData("date",current, acount);	
		}
		if(current.after(before)){
			int hintcount=MysqlOperation.getInstance().QueryInt("hintcount","acount",acount);
			if(hintcount!=15){
				hintcount+=3;
				if(hintcount>15){
					hintcount=15;
				}
			}		
			MysqlOperation.getInstance().UpdateData("date",current, acount);	
			MysqlOperation.getInstance().UpdateOther("hintcount", Integer.toString(hintcount), acount);
		}
	}
	public void Regist(byte[] byt,ChannelHandlerContext ctx) {
		RegistReq req= ProtoStuffSerializerUtil.deserialize(byt, RegistReq.class);
		RegistResp resp=new RegistResp();
		if(!MysqlOperation.getInstance().QueryOne("acount", req.acount)){
			int temp = new Random().nextInt(999999);
			String randomid=String.format("%06d", temp);
			while(MysqlOperation.getInstance().QueryOne("id", randomid)){
				temp= new Random().nextInt(999999);
				randomid=String.format("%06d", temp);
			}
			MysqlOperation.getInstance().Insert(req.acount, req.pwd,randomid);
			resp.result=RegistResp.REGIST_SUCCESS;
			ServerHandler.usermap.put(ctx.channel().id().toString(), req.acount);
		}
		else {
			resp.result=RegistResp.ACOUNT_EXIST;
		}
		//ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp, RespDataID.RegistResp);
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.write(buf);
		ctx.flush();
	}
	public void returnUsreInfo(byte[] byt,ChannelHandlerContext ctx) {
		UserInfoReq req=ProtoStuffSerializerUtil.deserialize(byt, UserInfoReq.class);
		UserInfo resp=MysqlOperation.getInstance().QueryUserInfo("acount", req.acount);
		//ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp, RespDataID.UserInfoResp);
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.writeAndFlush(buf);
	}
	public void initData(byte[] byt,ChannelHandlerContext ctx) {
		InitDataResp resp=new InitDataResp();
		resp.userinfo=MysqlOperation.getInstance().QueryUserInfo("acount",ServerHandler.usermap.get(ctx.channel().id().toString()));
		resp.screlist=MysqlOperation.getInstance().QueryScorelist("acount",ServerHandler.usermap.get(ctx.channel().id().toString()));
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.writeAndFlush(buf);
	}
}

