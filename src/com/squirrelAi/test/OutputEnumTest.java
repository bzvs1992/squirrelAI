package com.squirrelAi.test;

import com.zhangyibin.application.specialusers.SpecialUsersEnum;
import com.zhangyibin.application.speciauserslist.SpecialUsersList;

/**
 * 类：OutputEnumTest
 * 作用：测试输出枚举值
 */

public class OutputEnumTest {

    public static void main(String[] args) throws Exception {

        for (SpecialUsersEnum str : SpecialUsersEnum.values()) {
            System.out.println(str);
        }

//        SpecialUsersList specialUsersList = new SpecialUsersList();
//        System.out.println(specialUsersList.getSpecialUsersList());


    }

}
