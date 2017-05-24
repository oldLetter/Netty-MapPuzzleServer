package com.netty.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dyuproject.protostuff.Tag;
import com.mysql.fabric.xmlrpc.base.Value;
import com.netty.core.MysqlOperation;
import com.netty.core.ServerHandler;
import com.netty.util.CoderUtil;
import com.netty.util.ExcelOperation;
import com.netty.util.ProtoStuffSerializerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;


public class AnswerController {
    private static Log log = LogFactory.getLog(AnswerController.class);  
    public static class PaperScoreReq
    {
    	@Tag(value=1)
    	public String id;
    	@Tag(value=2)
    	public int score;
    	@Tag(value=3)
    	public boolean type;
    }
    public static class PaperScoreResp
    {
    	@Tag(value=1)
    	public int score;
    }
    public static class PapersScoreResp
    {
    	@Tag(value=1)
        public List<Integer> scoreList;
        public PapersScoreResp()
        {
            scoreList=new ArrayList<>();
        }
    }
	public static class PaperInfo
	{
		@Tag(value=1)
		public String id;
		@Tag(value=2)
	    public String name;
		@Tag(value=3)
	    public String paperName;
		@Tag(value=4)
	    public String count;
	    public PaperInfo()
	    {

	    }
	}
	public static class PaperListResp {
		@Tag(value=1)
		public List<PaperInfo> paperList;
		public  PaperListResp() {
			paperList=new ArrayList<>();
		}
	}
	public static class QuestionDetail
	{
		@Tag(value=1)
	    public String id;
		@Tag(value=2)
	    public String title;
		@Tag(value=3)
	    public String answerA;
		@Tag(value=4)
	    public String answerB;
		@Tag(value=5)
	    public String answerC;
		@Tag(value=6)
	    public String answerD;
		@Tag(value=7)
	    public String rightAnswer;
		
	    public QuestionDetail() {
			// TODO Auto-generated constructor stub
		}	
	}
	public static class QuestionListReq {
		@Tag(value=1)
	    public String paperName;
		@Tag(value=2)
		public String id;
	}
	public static class QuestionListResp {
		@Tag(value=1)
		public List<QuestionDetail> list;
		@Tag(value=2)
		public int maxscore;
		public  QuestionListResp() {
			list=new ArrayList<>();
		}
	}
	public void GetPaperList(byte[] byt,ChannelHandlerContext ctx){
		ArrayList<ArrayList<Map<String, String>>> result;
		try {
			result = ExcelOperation.readExcelWithTitle("src/doc/PaperList.xlsx");
			PaperListResp resp=new PaperListResp();
			for(int i=0;i<result.size();i++){
				ArrayList<Map<String, String>> sheet=result.get(i);
				for(int j=0;j<sheet.size();j++){
					Map<String , String> rowmap=sheet.get(j);
					PaperInfo info=new PaperInfo();
					info.id=rowmap.get("ID");
					info.name=rowmap.get("name");
					info.paperName=rowmap.get("paperName");
					info.count=rowmap.get("count");
					resp.paperList.add(info);
				}
			}
			ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
			ctx.write(buf);
			ctx.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void GetQuestionList(byte[] byt,ChannelHandlerContext ctx) {
		QuestionListReq req=ProtoStuffSerializerUtil.deserialize(byt, QuestionListReq.class);
		String excelpath="src/doc/"+req.paperName+".xlsx";
		ArrayList<ArrayList<Map<String, String>>> result;
		try {
			result = ExcelOperation.readExcelWithTitle(excelpath);
			QuestionListResp resp=new QuestionListResp();
			for(int i=0;i<result.size();i++){
				ArrayList<Map<String, String>> sheet=result.get(i);
				for(int j=0;j<sheet.size();j++){
					Map<String , String> rowmap=sheet.get(j);
					QuestionDetail info=new QuestionDetail();
					info.id=rowmap.get("id");
					info.title=rowmap.get("title");
					info.answerA=rowmap.get("A");
					info.answerB=rowmap.get("B");
					info.answerC=rowmap.get("C");
					info.answerD=rowmap.get("D");
					info.rightAnswer=rowmap.get("right");
					resp.list.add(info);
				}
			}
			resp.maxscore=MysqlOperation.getInstance().QueryInt("netpaper"+req.id, "acount",  ServerHandler.usermap.get(ctx.channel().id().toString()));
			log.info(resp.maxscore);
			ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
			ctx.write(buf);
			ctx.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void updateScore(byte[] byt,ChannelHandlerContext ctx) {
		PaperScoreReq req=ProtoStuffSerializerUtil.deserialize(byt, PaperScoreReq.class);
		PaperScoreResp resp=new PaperScoreResp();
		if(req.type){
			MysqlOperation.getInstance().UpdateInt("locpaper"+req.id, req.score, ServerHandler.usermap.get(ctx.channel().id().toString()));			
		}else {
			MysqlOperation.getInstance().UpdateInt("netpaper"+req.id, req.score, ServerHandler.usermap.get(ctx.channel().id().toString()));			
		}
		resp.score=req.score;
		ByteBuf buf=ProtoStuffSerializerUtil.serialize(resp);
		ctx.write(buf);
		ctx.flush();
	}
}
