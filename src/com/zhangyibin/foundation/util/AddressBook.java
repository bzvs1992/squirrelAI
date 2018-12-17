package com.zhangyibin.foundation.util;

import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;
import com.zhangyibin.foundation.databaseservice.InsertService;
import com.zhangyibin.foundation.databaseservice.SelectService;

import java.util.List;
import java.util.ArrayList;

/**
 * 类：AddressBook
 * 作用：好友通讯录
 * 1.导出好友列表
 * 2.判断好友是否在数据库中存在，如果是数据库中不存在的好友，那么会将最新的好友存储在数据库中
 * 通讯录字段：UserName、NickName、RemarkName、Province、City
 */
public class AddressBook {

    private static List<String> list = new ArrayList<String>();

    public static void getAddressBookList(JSONObject jsonObject) {
        try {
            list.addAll(SelectService.getSelectService());//将数据库的好友列表放到List中
            System.out.println("数据库中的好友列表:"+list);//当前数据库中的好友列表

            JSONArray jsonArray = (JSONArray) jsonObject.get("MemberList");
            System.out.println("好友名单如下：");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String userName = jsonObject1.getString("UserName");
                String nickName = jsonObject1.getString("NickName");
                String remarkName = jsonObject1.getString("RemarkName");
                String province = jsonObject1.getString("Province");
                String city = jsonObject1.getString("City");
                System.out.println(userName + "','" + nickName + "','" + remarkName + "','" + province + "','" + city);

                System.out.println("---------分隔符---------");

                // 以下代码的作用：如果在数据库中没有找到的好友名称，将该好友插入到数据库中
                String regexp = "'";//单引号
                String reNickName = nickName.replaceAll(regexp, "");//为了防止SQL语句出错，将微信好友的名称中包含单引号的nickName进行处理

                if (list.toString().indexOf(reNickName) == -1) {
                    System.out.println("不在数据库中的好友名称：" + reNickName);
                    String strSql = "INSERT INTO user VALUES ('" + userName + "','" + nickName + "','" + remarkName + "','" + province + "','" + city + "');";
                    InsertService.getInsertService(strSql);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
