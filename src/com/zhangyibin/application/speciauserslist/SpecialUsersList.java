package com.zhangyibin.application.speciauserslist;

import com.zhangyibin.application.specialusers.SpecialUsersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：SpecialUsersList
 * 作用：将特殊账号的枚举值转化成List
 */

public final class SpecialUsersList {
    public static List<String> getSpecialUsersList() {
        List<String> list = new ArrayList<String>();
        try {
            for (SpecialUsersEnum str : SpecialUsersEnum.values()) {
                list.add(String.valueOf(str));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

}
