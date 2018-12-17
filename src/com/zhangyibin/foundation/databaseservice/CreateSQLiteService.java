package com.zhangyibin.foundation.databaseservice;


import java.lang.Class;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;

// 连接(创建)数据库

/**
 * 类：CreateSQLiteService
 * 作用：
 * 1.提供数据库服务
 * 2.创建数据库SquirrelAI.db
 * 3.使用sqlite数据库
 *
 */
public class CreateSQLiteService {

    private static Connection connection = null;
    private static Statement statement = null;

    public static void main(String[] args) {
        try {

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:SquirrelAI.db");
            statement = connection.createStatement();
//            String sql = "CREATE TABLE message(date varchar(255),name varchar(255),message varchar(512));";
            //UserName、NickName、RemarkName、Province、City
            String sql = "CREATE TABLE user(username varchar(255),nickname varchar(255),remarkname varchar(512),province varchar(512),city varchar(512));";

            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            System.out.println("数据库创建成功!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
