package com.zhangyibin.foundation.databaseservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 类：InsertService
 * 作用：执行Sql插入数据库服务
 * 将数据记录插入SquirrelAI中的message表
 * 插入字段：date(日期)|name(姓名)|message(消息)
 */

public class InsertService {

    // getInsertService() 方法名相同，参数个数/参数类型不同 称为方法重载
    // 将消息按照字段分隔插入数据库
    public static void getInsertService(String date,String name,String message){
        try{

            Class.forName("org.sqlite.JDBC");
            // 连接到数据库Dxy.db
            Connection connection = DriverManager.getConnection("jdbc:sqlite:SquirrelAI.db");
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO message VALUES ('"+date+"','"+name+"','"+message+"');";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();

            System.out.println(sql);

        }catch (Exception e){

        }
    }

    // 通过完整的sql插入数据库
    public static void getInsertService(String strSql){
        try{

            Class.forName("org.sqlite.JDBC");
            // 连接到数据库Dxy.db
            Connection connection = DriverManager.getConnection("jdbc:sqlite:SquirrelAI.db");
            Statement statement = connection.createStatement();
            String sql = strSql;
            statement.executeUpdate(sql);
            statement.close();
            connection.close();

            System.out.println(sql);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
//        InsertService.getInsertService("2018-08-02 20:21:25","自动回复","杭州:周五,大雨转阴 南风微风,最低气温26度，最高气温31度");

    }

}
