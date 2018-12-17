package com.zhangyibin.foundation.util;

/**
 * 类：Matchers(匹配器)
 * 作用：用于处理登陆微信过程的正则表达式的处理。
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matchers {

    public static String match(String p , String str) {
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

}
