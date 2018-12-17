package com.squirrelAi.test;


import com.zhangyibin.foundation.databaseservice.InsertService;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * 类：InsertServiceTest
 * 作用：数据插入数据库
 *
 */
public class InsertServiceTest {

    private static File file = null;
    private static InputStream inputStream = null;
    private static InputStreamReader inputStreamReader = null;
    private static BufferedReader bufferedReader = null;

    public static void main(String[] args) {

        try {
            file = new File("/home/zhangyibin/下载/sql.txt");
            inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String strSql = "";
            while ((strSql = bufferedReader.readLine()) != null) {
                Thread.sleep(2000);
                InsertService.getInsertService(strSql);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
