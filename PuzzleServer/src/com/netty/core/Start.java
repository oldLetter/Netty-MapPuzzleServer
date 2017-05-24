package com.netty.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  class Start {

	private static Log log = LogFactory.getLog(HttpServer.class);  
	
	public static void main(String[] args) throws Exception {
		int port=8844;
		if(args.length>0&&args!=null){
			try {
				port=Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		HttpServer server = new HttpServer();  
		log.info("server start");
		server.start(port);  
  }  
    
}
