package com.zhangyibin.application.specialusers;

/**
 * 枚举类：SpecialUsersEnum
 * 作用：记录消息不回复的特殊账号
 */

public enum SpecialUsersEnum {

    微信群消息("微信群消息"),
    贝店精选("贝店精选"),
    渤海银行("渤海银行"),
    渤海银行社区之家("渤海银行社区之家"),
    Booking福利("Booking福利"),
    CSDN("CSDN"),
    二维火吃喝购("二维火吃喝购"),
    丰巢快递柜("丰巢快递柜"),
    公众平台安全助手("公众平台安全助手"),
    汉堡王中国("汉堡王中国"),
    杭州本地宝("杭州本地宝"),
    杭州公安("杭州公安"),
    好邻居HiMart("好邻居HiMart"),
    环球黑卡("环球黑卡"),
    会员生活圈("会员生活圈"),
    京东("京东"),
    美团福利社("美团福利社"),
    Motorola("Motorola"),
    平安银行("平安银行"),
    QuestMobile("QuestMobile"),
    思瑞健康服务号("思瑞健康服务号"),
    上汽大众大众品牌("上汽大众大众品牌"),
    腾讯理财通("腾讯理财通"),
    腾讯企业邮箱("腾讯企业邮箱"),
    网易四季("网易四季"),
    网易味央("网易味央"),
    微信公众平台("微信公众平台"),
    微信卡包("微信卡包"),
    微信支付("微信支付"),
    西贝莜面村("西贝莜面村"),
    小e微店("小e微店"),
    星巴克江浙沪("星巴克江浙沪"),
    招商银行信用卡("招商银行信用卡"),
    浙江联通("浙江联通"),
    中国建设银行("中国建设银行"),
    中国银行浙江分行("中国银行浙江分行"),
    中邮速递易("中邮速递易"),
    蘑菇街服务中心("蘑菇街服务中心"),
    捞王锅物料理("捞王锅物料理"),
    龙湖杭州滨江天街("龙湖杭州滨江天街"),
    Name_51CTO("51CTO");

    public String NameList = "";

    private SpecialUsersEnum(String NameList) {
        this.NameList = NameList;

    }

    public String getNameList() {
        return this.NameList;

    }

}
