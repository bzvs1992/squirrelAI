package com.zhangyibin.foundation.databaseservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 类：SelectService
 * 作用：执行Sql查询服务
 */
public class SelectService {

    public static void getSelectService(String sql) {
        try {

            Class.forName("org.sqlite.JDBC");
            // 连接到数据库Dxy.db
            Connection connection = DriverManager.getConnection("jdbc:sqlite:SquirrelAI.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String strUserName = resultSet.getString("username");
                String strNickName = resultSet.getString("nickname");
                String strRemarkname = resultSet.getString("remarkname");
                String strProvince = resultSet.getString("province");
                String strCity = resultSet.getString("city");

                System.out.println(strUserName + "," + strNickName + "," + strRemarkname + "," + strProvince + "," + strCity);

            }

            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 用于查询数据库中没有记录的好友
    public static List<String> getSelectService() {
        List<String> list = new ArrayList<String>();
        String strSelectResult = "";
        try {
            Class.forName("org.sqlite.JDBC");
            // 连接到数据库Dxy.db
            Connection connection = DriverManager.getConnection("jdbc:sqlite:SquirrelAI.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select username,nickname,remarkname,province,city from user");
            while (resultSet.next()) {
                String strUserName = resultSet.getString("username");
                String strNickName = resultSet.getString("nickname");
                String strRemarkname = resultSet.getString("remarkname");
                String strProvince = resultSet.getString("province");
                String strCity = resultSet.getString("city");

//                System.out.println(strUserName + "," + strNickName + "," + strRemarkname + "," + strProvince + "," + strCity);
                strSelectResult = strNickName;
                list.add(strSelectResult);

            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static void main(String[] args) throws Exception {
//        SelectService.getSelectService("select username,nickname,remarkname,province,city from user");
        System.out.println(SelectService.getSelectService());

    }

}
