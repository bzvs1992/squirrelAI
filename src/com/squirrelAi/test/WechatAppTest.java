package com.squirrelAi.test;

import com.zhangyibin.application.speciauserslist.SpecialUsersList;

import java.util.List;
import java.util.ArrayList;

public class WechatAppTest {

    private static List<String> list = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        list.addAll(SpecialUsersList.getSpecialUsersList());
        System.out.println(list);

    }

}
