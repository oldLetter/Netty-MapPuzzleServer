package com.netty.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableInterceptor.ORBInitInfoPackage.InvalidName;

import com.netty.controllers.UserInfo;
//import com.netty.controllers.UserInfo;
import com.netty.util.DbcpPool;

public class MysqlOperation {
    private Connection conn = null;
    private  ResultSet rs=null;
	private static Log log = LogFactory.getLog(MysqlOperation.class);  
    private static MysqlOperation instance=new MysqlOperation();
    public static MysqlOperation getInstance() {
		return instance;
	}
    public  boolean QueryOne(String name1,String name2) {
    	try {
    		conn=DbcpPool.getConn();
    		Statement stmt=conn.createStatement();
    		rs=null;
        	String sql = "select "+name1+" from userdata where "+name1+" = "+"'"+name2+"'";  
        	rs = stmt.executeQuery(sql);
			if(rs.next()){
				return true;
			}
    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
    public java.sql.Date QueryDate(String name1,String name2,String name3) {
    	java.sql.Date date=null;
    	try {
    		conn=DbcpPool.getConn();
    		Statement stmt=conn.createStatement();
    		rs=null;
        	String sql = "select "+name1+" from userdata where "+name2+" = "+"'"+name3+"'";  
        	rs = stmt.executeQuery(sql);
         	while (rs.next()) {
            	date=rs.getDate(1);
			}
    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return date;
	}
    public int QueryInt(String name1,String name2,String name3) {
		int result=0;
    	try {
    		conn=DbcpPool.getConn();
    		Statement stmt=conn.createStatement();
    		rs=null;
        	String sql = "select "+name1+" from userdata where "+name2+" = "+"'"+name3+"'";  
        	rs = stmt.executeQuery(sql);
        	while (rs.next()) {
            	result =rs.getInt(1);
			}
    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return result;
	}
   public  UserInfo QueryUserInfo(String name1,String name2){
	   UserInfo info=new UserInfo();
	   try {
   			conn=DbcpPool.getConn();
   			Statement stmt=conn.createStatement();
   			rs=null;
   			String sql = "select * from userdata where "+name1+" = "+"'"+name2+"'";  
   			rs = stmt.executeQuery(sql);
   			while(rs.next()){
   				info.acount=rs.getString(1);
   				info.id=rs.getString(3);
   				info.currentLevel=rs.getInt(4);
   				info.maxLevel=rs.getInt(5);
   				info.hintCount=rs.getInt(6);
   			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return info;
   }
   public  void UpdateOther(String data0,String data1,String data2){
       try {
    	   String sql = "update userdata set "+data0+"="+data1+" where acount=?";
    	   conn=DbcpPool.getConn();
           PreparedStatement pst = conn.prepareStatement(sql);
           //pst.setInt(1, data1);
           pst.setString(1, data2);
	       pst.executeUpdate();

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   
   public  void UpdateData(String data0,java.sql.Date data1,String data2){
       try {
    	   String sql = "update userdata set "+data0+"= ?where acount=?";
    	   conn=DbcpPool.getConn();
           PreparedStatement pst = conn.prepareStatement(sql);
           pst.setDate(1, data1);
           pst.setString(2, data2);
	       pst.executeUpdate();

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
    public  void  Insert(String data1,String data2,String data3) {
    	 String sql="insert into userdata (acount,pwd,id,currentLevel,maxLevel,hintcount) values (?,?,?,?,?,?)";  
         try {  
     		conn=DbcpPool.getConn();
            PreparedStatement preStmt=conn.prepareStatement(sql);  
            preStmt.setString(1, data1);  
            preStmt.setString(2, data2);  
            preStmt.setString(3, data3);
            preStmt.setInt(4, 1);
            preStmt.setInt(5, 3);
            preStmt.setInt(6, 10);
            preStmt.executeUpdate();  
               
         } catch (SQLException e) {  
             // TODO Auto-generated catch block  
             e.printStackTrace();  
         }  
	}
}
