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
    // �������ݿ����Ӷ��� ( ����Դ )  
    private static BasicDataSource dataSource=new BasicDataSource();  
    // ��������Դ  
    static   
    {  
        DataSourceConfig();  
    }  
    /** 
     * ���� dataSource������ֵ 
     */  
    private static void DataSourceConfig()  
    {     
        dataSource.setDriverClassName("com.mysql.jdbc.Driver"); //���ݿ�����  
            dataSource.setUsername("root");  //�û���  
            dataSource.setPassword("123");  //����  
            dataSource.setUrl("jdbc:mysql://localhost:3306/userinfo?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false");  //����url  
            dataSource.setInitialSize(10); // ��ʼ����������    
            dataSource.setMaxTotal(100);  //���������  
            dataSource.setMaxIdle(80);  // ��������������  
            dataSource.setMaxWaitMillis(6000);  // �������ȴ�ʱ��  
            dataSource.setMinIdle(10);  // ������С��������  
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
            // �����ӳ��л�����Ӷ���  
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
