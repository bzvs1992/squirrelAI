package com.zhangyibin.aifunction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 类：SquirrelAiRobot
 * 1.名称为松鼠AI机器人
 * 2.通过AI自动回复用户消息
 * 3.对接图灵机器人
 */

public class SquirrelAiRobot {

    private static String TuLing_API_URL = "http://openapi.tuling123.com/openapi/api/v2";

    private static String ReqType = "0";//输入类型:0-文本(默认)、1-图片、2-音频
    private static String TuLingapiKey = "b7f5a870a15b4958852166350741b6b7";
    private static String TuLingUserId = "268996";

    private static String StrJson1 = "{\"reqType\":";
    private static String StrJson2 = ",\"perception\": {\"inputText\": {\"text\": \"";
    private static String StrJson3 = "\"},\"inputImage\": {\"url\": \"imageUrl\"";
    private static String StrJson4 = "},\"selfInfo\": {\"location\": {\"city\": \"杭州\",\"province\": \"浙江\",\"street\": \"网商路\"}}},\"userInfo\": {\"apiKey\": \"";
    private static String StrJson5 = "\",";
    private static String StrJson6 = " \"userId\":  \"";
    private static String StrJson7 = "\"}}";

    private static Document document = null;
    private static Connection connection = null;

    public static String SquirrelRobot(String msg) {
        String result = "";
        try {

            connection = Jsoup.connect(TuLing_API_URL)
                    .requestBody(StrJson1 + ReqType + StrJson2 + msg + StrJson3 + StrJson4 + TuLingapiKey + StrJson5 + StrJson6 + TuLingUserId + StrJson7)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                    .ignoreContentType(true)
                    .timeout(3000);
            document = connection.post();
            String Strdouc = document.text();

            JSONObject Str_Json = JSONObject.fromObject(Strdouc);
            JSONArray Results_JsonArray = Str_Json.getJSONArray("results");
            for (int i = 0; i < Results_JsonArray.size(); i++) {
                JSONObject data_jsonObject = Results_JsonArray.getJSONObject(i);
                String receiveAddress = data_jsonObject.getString("values");

                JSONObject Txt_Json = JSONObject.fromObject(receiveAddress);
                result = "松鼠AI：" + Txt_Json.get("text").toString();
//                result = "" + Txt_Json.get("text").toString();
            }


        } catch (Exception e) {
            result = "松鼠AI：" + "对不起，我还不能理解到您说的话。";
//            result = "" + "对不起，我还不能理解到您说的话。";
        }

        return result;
    }


}
