package com.netty.core;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;

import com.netty.controllers.MessagePush;
import com.netty.util.CoderUtil;
import java.lang.reflect.Method;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;  


public class ServerHandler extends ChannelInboundHandlerAdapter{  
     private static Log log = LogFactory.getLog(ServerHandler.class);  
     //public static HashMap<String, String> usermap =new HashMap<>();
     public static BidiMap usermap = new TreeBidiMap();
     public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
     
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelActive(ctx);
    	channelGroup.add(ctx.channel());
//		new MessagePush().start();
        System.out.println(ctx.channel().id()+" In");  
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelInactive(ctx);
    	channelGroup.remove(ctx.channel());
    	usermap.remove(ctx.channel().id());
        System.out.println(ctx.channel().id()+" Out");  
    }
    
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception {
        ByteBuf buf = (ByteBuf)msg;  
        byte[] temp = new byte[buf.readableBytes()]; 
        buf.readBytes(temp);
		byte[] urllenthbyt=new byte[4];
		System.arraycopy(temp, 0, urllenthbyt, 0, 4);
		int urllenth=CoderUtil.bytesToInt(urllenthbyt, 0);
		byte[] urlbyt=new  byte[urllenth];
		byte[] data=new byte[temp.length-4-urllenth];
		System.arraycopy(temp, 4+urllenth, data, 0, data.length);
		System.arraycopy(temp, 4, urlbyt, 0, urlbyt.length);
        String body = new String(urlbyt,"UTF-8"); 
        callControlMethod(body, data,ctx);
	}  
	public void callControlMethod(String methodname,byte[] req,ChannelHandlerContext ctx)
    {
    	try {
    		String className="com.netty.controllers."+StringUtils.substringBefore(methodname, ",");
        	Class cla= Class.forName(className); 
			Method m1=cla.getMethod(StringUtils.substringAfter(methodname, ","),byte[].class,ChannelHandlerContext.class);
			Object obj=cla.newInstance();
			Object objs[]=new Object[]{methodname,ctx};
			m1.invoke(obj, req,ctx);
			//m1.invoke(obj,(Object)new Object[]{req,ctx});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    public void callControlMethod(Object msg)
    {
    	Class cla=ControllerBase.class;
    }
      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  
            throws Exception {  
        // TODO Auto-generated method stub  
        ctx.close();  
    }  
}  