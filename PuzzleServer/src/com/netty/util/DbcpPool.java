package com.netty.util;

import java.io.IOException ;  
import java.io.InputStream ;  
import java.sql.Connection ;  
import java.sql.PreparedStatement ;  
import java.sql.ResultSet ;  
import java.sql.SQLException ;  
import java.util.Properties ;  
import org.apache.commons.dbcp2.BasicDataSource;  
  
public class DbcpPool {  
    protected static Connection conn=null ;  
    // 创建数据库连接对象 ( 数据源 )  
    private static BasicDataSource dataSource=new BasicDataSource();  
    // 配置数据源  
    static   
    {  
        DataSourceConfig();  
    }  
    /** 
     * 设置 dataSource各属性值 
     */  
    private static void DataSourceConfig()  
    {     
        dataSource.setDriverClassName("com.mysql.jdbc.Driver"); //数据库驱动  
            dataSource.setUsername("root");  //用户名  
            dataSource.setPassword("123");  //密码  
            dataSource.setUrl("jdbc:mysql://localhost:3306/userinfo?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false");  //连接url  
            dataSource.setInitialSize(10); // 初始的连接数；    
            dataSource.setMaxTotal(100);  //最大连接数  
            dataSource.setMaxIdle(80);  // 设置最大空闲连接  
            dataSource.setMaxWaitMillis(6000);  // 设置最大等待时间  
            dataSource.setMinIdle(10);  // 设置最小空闲连接  
            dataSource.setValidationQuery("select 1");
            dataSource.setTestWhileIdle(true);
            dataSource.setTimeBetweenEvictionRunsMillis(3600000);
            dataSource.setMinEvictableIdleTimeMillis(18000000);
            dataSource.setTestOnBorrow(true);
    }  
    
    protected static void shutdownDataSource() throws SQLException {  
        BasicDataSource bds = (BasicDataSource) dataSource;  
        bds.close();  
    }  
    
    /** 
     * @return 
     */  
    public static Connection getConn()  
    {  
        try   
        {  
            // 从连接池中获得连接对象  
            if(conn==null)  
            {  
                conn=dataSource.getConnection();  
            }  
        }  
        catch(SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return conn ;  
    }  
}  
