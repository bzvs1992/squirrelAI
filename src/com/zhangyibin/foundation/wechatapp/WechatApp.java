package com.zhangyibin.foundation.wechatapp;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.UIManager;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSON;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;

import com.zhangyibin.aifunction.SquirrelAiRobot;
import com.zhangyibin.foundation.databaseservice.InsertService;
import com.zhangyibin.foundation.util.AddressBook;
import com.zhangyibin.foundation.util.CookieUtil;
import com.zhangyibin.foundation.util.Matchers;
import com.zhangyibin.foundation.wechatinterface.WechatInterface;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class WechatApp {

    // Ios地址: https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin
    // Android 地址：https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin

    public String BASE_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";
    public String REDIRECT_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";
    public String WEBPUSH_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";

//    public String BASE_URL = "https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin";
//    public String REDIRECT_URL = "https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin";
//    public String WEBPUSH_URL = "https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin";

    public String SKEY = "e" + DateKit.getCurrentUnixTime();
    public String SYNCKEY = "e" + DateKit.getCurrentUnixTime();
    public String WXSID = "e" + DateKit.getCurrentUnixTime();
    public String WXUIN = "e" + DateKit.getCurrentUnixTime();
    public String PASS_TICKET = "e" + DateKit.getCurrentUnixTime();
    public String DEVICEID = "e" + DateKit.getCurrentUnixTime();

    public static final Logger LOGGER = Logger.getLogger("com.zhangyibin.foundation.wechatapp");


    public String Uuid = null;
    private int Tip = 0;
    private String Cookie = null;
    private QRCodeFrame qrCodeFrame = null;
    private JSONObject SyncKey = null;
    private JSONObject User = null;
    private JSONObject BaseRequest = null;
    private JSONArray MemberList = null;// 微信联系人列表
    public JSONArray ContactList = null;// 可聊天的联系人列表

    private List<String> specialUsersList = new ArrayList<String>(); // 存储不回复消息的特殊账号

    public WechatApp(List specialUsersList) {
        /*
         * 1.设置指定键对值的系统属性
         * 2.System.setProperty 相当于一个静态变量存在内存里面
         * 3.System.setProperty("Property1", "abc");
         * 4.这样就把第一个參数设置成为系统的全局变量！能够在项目的不论什么一个地方 通过System.getProperty("变量")来获得
         */
        System.setProperty("jsse.enableSNIExtension", "false");

        // 特殊账号List的引用传递
        this.specialUsersList = specialUsersList;
    }

    /**
     * 获取UUID:uuid是唯一的识别码
     */
    public String getUUID() {

        HttpRequest request =
                HttpRequest.get(WechatInterface.UUID_URL, true, "appid", "wx782c26e4c19acffb", "fun", "new", "lang", "zh_CN", "_", DateKit.getCurrentUnixTime());
        // LOGGER.info 记录信息
        LOGGER.info("[*] " + request);

        String res = request.body();
        //res 为UUID
        request.disconnect();

        //如果返回的uuid信息不等于空
        if (StringKit.isNotBlank(res)) {
            //将登陆信息赋值给code
            String code = Matchers.match("window.QRLogin.code = (\\d+);", res);
            //如果登陆信息不等于空，那么微信登陆成功，否则反馈错误状态码
            if (null != code) {
                if (code.equals("200")) {
                    this.Uuid = Matchers.match("window.QRLogin.uuid = \"(.*)\";", res);
                    return this.Uuid;
                } else {
                    LOGGER.info("[*] 错误的状态码: %s"+ code);
                }
            }
        }
        return null;
    }

    /**
     * 显示微信登陆二维码
     */
    public void showQrCode() {

        // post请求"https://login.weixin.qq.com/qrcode/" + this.uuid; 可以获取到微信的登陆二维码
        String url = WechatInterface.SHOWQRCODEURL + this.Uuid;

        //将二维码存储在根目录下面
        final File output = new File("temp.jpg");

        //通过post 获取到二维码信息，并且将二维码存储在output(根目录下面temp.jpg)文件中
        HttpRequest.post(url, true, "t", "webwx", "_", DateKit.getCurrentUnixTime()).receive(output);

        //判断：output不等于空，
        //isFile()测试此抽象路径名表示的文件是否是一个标准文件
        //exists()测试此抽象路径名表示的文件或目录是否存在
        if (null != output && output.exists() && output.isFile()) {
            //调用Runnable线程
            EventQueue.invokeLater(new Runnable() {
                @Override
                //覆写Runnable线程类中的run()方法
                public void run() {
                    try {
                        //设置窗口外观
                        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                        //将二维码传到qrCodeFrame 窗体中，并显示出来
                        qrCodeFrame = new QRCodeFrame(output.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 等待登录
     * 扫描二维码并登陆
     */
    public String waitForLogin() {
        this.Tip = 1;
        HttpRequest request = HttpRequest.get(WechatInterface.LOGIN_URL, true, "tip", this.Tip, "uuid", this.Uuid, "_", DateKit.getCurrentUnixTime());
        LOGGER.info("[*] " + request.toString());
        String Res = request.body();
        request.disconnect();

        if (null == Res) {
            LOGGER.info("[*] 扫描二维码验证失败");
            return "";
        }

        String Code = Matchers.match("window.code=(\\d+);", Res);

        if (null == Code) {
            LOGGER.info("[*] 扫描二维码验证失败");
            return "";
        } else {
            if (Code.equals("201")) {
                LOGGER.info("[*] 成功扫描,请在手机上点击确认以登录");
                Tip = 0;
            } else if (Code.equals("200")) {
                LOGGER.info("[*] 正在登录...");
                String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", Res);
                this.REDIRECT_URL = pm + "&fun=new";
                LOGGER.info("[*] redirect_uri=%s"+this.REDIRECT_URL);
                this.BASE_URL = this.REDIRECT_URL.substring(0, this.REDIRECT_URL.lastIndexOf("/"));
                LOGGER.info("[*] base_uri=%s"+ this.BASE_URL);
            } else if (Code.equals("408")) {
                LOGGER.info("[*] 登录超时");
            } else {
                LOGGER.info("[*] 扫描code=%s"+ Code);
            }
        }
        return Code;
    }

    public void closeQrWindow() {
        //关闭二维码窗口
        qrCodeFrame.dispose();
    }


    /**
     * 登陆微信
     */
    public boolean login() {

        HttpRequest request = HttpRequest.get(this.REDIRECT_URL);
        LOGGER.info("[*] " + request);
        String res = request.body();
        this.Cookie = CookieUtil.getCookie(request);
        request.disconnect();

        if (StringKit.isBlank(res)) {
            return false;
        }

        this.SKEY = Matchers.match("<skey>(\\S+)</skey>", res);
        this.WXSID = Matchers.match("<wxsid>(\\S+)</wxsid>", res);
        this.WXUIN = Matchers.match("<wxuin>(\\S+)</wxuin>", res);
        this.PASS_TICKET = Matchers.match("<pass_ticket>(\\S+)</pass_ticket>", res);

        LOGGER.info("[*] skey[%s]"+ this.SKEY);
        LOGGER.info("[*] wxsid[%s]"+ this.WXSID);
        LOGGER.info("[*] wxuin[%s]"+ this.WXUIN);
        LOGGER.info("[*] pass_ticket[%s]"+ this.PASS_TICKET);

        this.BaseRequest = new JSONObject();
        BaseRequest.put("Uin", this.WXUIN);
        BaseRequest.put("Sid", this.WXSID);
        BaseRequest.put("Skey", this.SKEY);
        BaseRequest.put("DeviceID", this.DEVICEID);

        return true;
    }

    /**
     * 微信初始化
     */
    public boolean wxInit() {

        String url = this.BASE_URL + "/webwxinit?r=" + DateKit.getCurrentUnixTime() + "&pass_ticket=" + this.PASS_TICKET + "&skey=" + this.SKEY;

        JSONObject body = new JSONObject();
        body.put("BaseRequest", this.BaseRequest);

        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.Cookie)
                .send(body.toString());

        LOGGER.info("[*] " + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            return false;
        }

        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(res);

            if (null != jsonObject) {
                JSONObject BaseResponse = (JSONObject) jsonObject.get("BaseResponse");
                if (null != BaseResponse) {
                    int ret = BaseResponse.getInt("Ret", -1);
                    if (ret == 0) {
                        this.SyncKey = (JSONObject) jsonObject.get("SyncKey");
                        this.User = (JSONObject) jsonObject.get("User");
                        StringBuffer synckey = new StringBuffer();
                        JSONArray list = (JSONArray) SyncKey.get("List");
                        for (int i = 0, len = list.size(); i < len; i++) {
                            JSONObject item = (JSONObject) list.get(i);
                            synckey.append("|" + item.getInt("Key", 0) + "_" + item.getInt("Val", 0));
                        }

                        this.SYNCKEY = synckey.substring(1);

                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 微信状态通知
     */
    public boolean wxStatusNotify() {

        String Url = this.BASE_URL + WechatInterface.WXSTATUS_CODE + this.PASS_TICKET;

        JSONObject body = new JSONObject();
        body.put("BaseRequest", BaseRequest);
        body.put("Code", 3);
        body.put("FromUserName", this.User.getString("UserName"));
        body.put("ToUserName", this.User.getString("UserName"));
        body.put("ClientMsgId", DateKit.getCurrentUnixTime());

        HttpRequest request = HttpRequest.post(Url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.Cookie)
                .send(body.toString());

        LOGGER.info("[*] " + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            return false;
        }

        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(res);
            JSONObject BaseResponse = (JSONObject) jsonObject.get("BaseResponse");
            if (null != BaseResponse) {
                int ret = BaseResponse.getInt("Ret", -1);
                return ret == 0;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取联系人
     */
    public boolean getContact() {

        String url = this.BASE_URL + "/webwxgetcontact?pass_ticket=" + this.PASS_TICKET + "&skey=" + this.SKEY + "&r=" + DateKit.getCurrentUnixTime();

        JSONObject body = new JSONObject();
        body.put("BaseRequest", BaseRequest);

        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.Cookie)
                .send(body.toString());

        LOGGER.info("[*] " + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            return false;
        }

        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(res);
            //AddressBook类的作用：
            //1.导出好友列表
            //2.判断好友是否在数据库中存在，如果是数据库中不存在的好友，那么会将最新的好友存储在数据库中
            AddressBook.getAddressBookList(jsonObject);
            JSONObject BaseResponse = (JSONObject) jsonObject.get("BaseResponse");
            if (null != BaseResponse) {
                int ret = BaseResponse.getInt("Ret", -1);
                if (ret == 0) {
                    this.MemberList = (JSONArray) jsonObject.get("MemberList");
                    this.ContactList = new JSONArray();
                    if (null != MemberList) {
                        for (int i = 0, len = MemberList.size(); i < len; i++) {
                            JSONObject contact = (JSONObject) this.MemberList.get(i);
                            //公众号/服务号
                            if (contact.getInt("VerifyFlag", 0) == 8) {
                                continue;
                            }
                            //特殊联系人
//                            if (SpecialUsers.contains(contact.getString("UserName"))) {
//                                continue;
//                            }
                            //群聊
                            if (contact.getString("UserName").indexOf("@@") != -1) {
                                continue;
                            }
                            //自己
                            if (contact.getString("UserName").equals(this.User.getString("UserName"))) {
                                continue;
                            }
                            ContactList.add(contact);
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 消息检查
     */
    public int[] syncCheck() {

        int[] arr = new int[2];
        String Url = this.WEBPUSH_URL + "/synccheck";

        JSONObject body = new JSONObject();
        body.put("BaseRequest", BaseRequest);
        HttpRequest request = null;
        String res = null;


        request = HttpRequest.get(Url, true,
                "r", DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5),
                "skey", this.SKEY,
                "uin", this.WXUIN,
                "sid", this.WXSID,
                "deviceid", this.DEVICEID,
                "synckey", this.SYNCKEY,
                "_", System.currentTimeMillis())
                .header("Cookie", this.Cookie);


        LOGGER.info("[*] " + request);
        // 当res为空的时候，程序就开始报错了
        //res = request.body();
        /**
         *   循环的作用是排错
         *   原理：当res 为空的情况下，会一直循环获取内容，直到有内容的才跳出循环
         */
        int i = 1;
        while (i > 0) {
            // 当 res 出错的时候，异常不处理，res 字符串为空；然后继续执行循环提，直到拿到内容。
            try {
                res = request.body();
            } catch (Exception e) {
                res = "";
            }

            if (res != null || "".equals(res) || res.length() != 0) {
                break;
            }
        }

        request.disconnect();

        if (StringKit.isBlank(res)) {
            return arr;
        }

        String retcode = Matchers.match("retcode:\"(\\d+)\",", res);
        String selector = Matchers.match("selector:\"(\\d+)\"}", res);
        if (null != retcode && null != selector) {
            arr[0] = Integer.parseInt(retcode);
            arr[1] = Integer.parseInt(selector);
            return arr;
        }

        return arr;
    }

    /**
     * 发送消息
     */
    private void webwxsendmsg(String content, String to) {

        String url = this.BASE_URL + "/webwxsendmsg?lang=zh_CN&pass_ticket=" + this.PASS_TICKET;

        JSONObject body = new JSONObject();

        String clientMsgId = DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5);
        JSONObject Msg = new JSONObject();
        Msg.put("Type", 1);
        Msg.put("Content", content);
        Msg.put("FromUserName", User.getString("UserName"));
        Msg.put("ToUserName", to);
        Msg.put("LocalID", clientMsgId);
        Msg.put("ClientMsgId", clientMsgId);

        body.put("BaseRequest", this.BaseRequest);
        body.put("Msg", Msg);

        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.Cookie)
                .send(body.toString());

        LOGGER.info("[*] " + request);
        request.body();
        request.disconnect();
    }

    /**
     * 获取最新消息
     */
    public JSONObject webwxsync() {

        String url = this.BASE_URL + "/webwxsync?lang=zh_CN&pass_ticket=" + this.PASS_TICKET
                + "&skey=" + this.SKEY + "&sid=" + this.WXSID + "&r=" + DateKit.getCurrentUnixTime();

        JSONObject body = new JSONObject();
        body.put("BaseRequest", BaseRequest);
        body.put("SyncKey", this.SyncKey);
        body.put("rr", DateKit.getCurrentUnixTime());

        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.Cookie)
                .send(body.toString());

        LOGGER.info("[*] " + request);
        String res = request.body();
        request.disconnect();

        if (StringKit.isBlank(res)) {
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.parse(res);
        JSONObject BaseResponse = (JSONObject) jsonObject.get("BaseResponse");
        if (null != BaseResponse) {
            int ret = BaseResponse.getInt("Ret", -1);
            if (ret == 0) {
                this.SyncKey = (JSONObject) jsonObject.get("SyncKey");

                StringBuffer synckey = new StringBuffer();
                JSONArray list = (JSONArray) SyncKey.get("List");
                for (int i = 0, len = list.size(); i < len; i++) {
                    JSONObject item = (JSONObject) list.get(i);
                    synckey.append("|" + item.getInt("Key", 0) + "_" + item.getInt("Val", 0));
                }
                this.SYNCKEY = synckey.substring(1);
            }
        }
        return jsonObject;
    }

    /**
     * 回复消息
     */
    private String name = "";//好友名称

    public void handleMsg(JSONObject data) {

        if (null == data) {
            return;
        }

        JSONArray AddMsgList = (JSONArray) data.get("AddMsgList");

        for (int i = 0, len = AddMsgList.size(); i < len; i++) {
            LOGGER.info("[*] 你有新的消息，请注意查收");
            JSONObject msg = (JSONObject) AddMsgList.get(i);

            System.out.println(msg);//消息内容json

            int msgType = msg.getInt("MsgType", 0);
            name = this.getUserRemarkName(msg.getString("FromUserName"));

            String receiveMessages = msg.getString("Content");// receiveMessages 接收好友消息

            /*
             * 1.name记录消息来自哪位好友
             * 2.对不回复消息的name 做个判断
             * 3.specialUsersList 列表中存储为不作消息回复的特殊账号
             * 4.将list转化成字符串，在通过indexOf()方法判断列表中是否包含特殊账号
             * 5.!=-1 表示本次消息的发送者为特殊账号列表中的用户
             */

            if (specialUsersList.toString().indexOf(name)!=-1) {
                System.out.println("微信群消息和公众号消息不回复！");
            } else {
                if (msgType == 51) {
                    LOGGER.info("[*] 成功截获微信初始化消息");
                } else if (msgType == 1) {
                    // contains()方法，作用：是否包含
//                    if (SpecialUsers.contains(msg.getString("ToUserName"))) {
//                        continue;
//                    } else
                    if (msg.getString("FromUserName").equals(User.getString("UserName"))) {
                        continue;
                    } else if (msg.getString("ToUserName").indexOf("@@") != -1) {
                        //split() 方法用于把一个字符串分割成字符串数组
                        String[] peopleContent = receiveMessages.split(":<br/>");
                        LOGGER.info("|" + name + "| " + peopleContent[0] + ":\n" + peopleContent[1].replace("<br/>", "\n"));
                    } else {
                        LOGGER.info(name + ": " + receiveMessages);
                        // 存储好友的发送消息到数据库
                        InsertService.getInsertService(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),name,receiveMessages);

                        // 机器人处理消息
                        //String ReplyMessage = Robot.MoLiRobot(receiveMessages);//茉莉机器人接口
                        //String ReplyMessage = Robot.TuLingRobot(receiveMessages);//图灵机器人接口
                        //String ReplyMessage= PersonalReply.getReplyMessage(); //人工回复消息接口
                        //String ReplyMessage = PersonalReply.getReplyMessage(); //人工回复消息接口

                        /*
                         * 判断是否为白名单用户
                         * 如果判断为白名单列表，那么该用户的消息回复则通过人工；否则通过机器人回复。
                         */
                        String ReplyMessage = "";// ReplyMessage 回复好友消息
//                        if(name.equals(WhitelistEnum.胡雯.getNameList()) || name.equals(WhitelistEnum.张益斌.getNameList())){
//                        if (name.equals(WhitelistEnum.胡雯.getNameList())) {
//                            ReplyMessage = PersonalReply.getReplyMessage(); //人工回复消息接口
//                        } else {
//                            ReplyMessage = Robot.TuLingRobot(receiveMessages);//图灵机器人接口
//                        }

//                        ReplyMessage = "手机不在身边，请稍后联系。谢谢";
                        ReplyMessage= SquirrelAiRobot.SquirrelRobot(receiveMessages);//通过机器人自动回复消息
                        webwxsendmsg(ReplyMessage, msg.getString("FromUserName")); // 程序完成会话消息回复
                        LOGGER.info("自动回复 " + ReplyMessage);
                        // 存储当前给好友回复的消息到数据库
                        InsertService.getInsertService(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"自动回复",ReplyMessage);

                    }
                } else if (msgType == 3) {
                    webwxsendmsg("松鼠AI还不支持图片呢", msg.getString("FromUserName"));
                } else if (msgType == 34) {
                    webwxsendmsg("松鼠AI还不支持语音呢", msg.getString("FromUserName"));
                } else if (msgType == 42) {
                    LOGGER.info(name + " 给你发送了一张名片:");
                }
            }

        }

    }

    /**
     * 获取用户的备注名称
     *
     * @param id
     * @return
     */
    public String getUserRemarkName(String id) {
        String name = "微信群消息";//一般值的是群聊消息
        for (int i = 0, len = this.MemberList.size(); i < len; i++) {
            JSONObject member = (JSONObject) this.MemberList.get(i);
            if (member.getString("UserName").equals(id)) {
                if (StringKit.isNotBlank(member.getString("RemarkName"))) {
                    name = member.getString("RemarkName");
                } else {
                    name = member.getString("NickName");
                }
                return name;
            }
        }
        return name;
    }

    /**
     * 监听微信消息
     */
    public void listenMsgMode() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 线程延时1000毫秒
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    LOGGER.info("[*] 进入消息监听模式 ...");
                    int playWeChat = 0;
                    while (true) {

                        int[] arr = syncCheck();

                        LOGGER.info("[*] retcode=%s,selector=%s"+arr[0]+ arr[1]);

                        if (arr[0] == 1100) {
                            LOGGER.info("[*] 你在手机上登出了微信，再见！");
                            break;
                        }

                        if (arr[0] == 0) {
                            if (arr[1] == 2) {
                                JSONObject data = webwxsync();
                                handleMsg(data);
                            } else if (arr[1] == 6) {
                                JSONObject data = webwxsync();
                                handleMsg(data);
                            } else if (arr[1] == 7) {
                                playWeChat += 1;
                                LOGGER.info("[*] 你在手机上玩微信被我发现了 %d 次"+ playWeChat);
                                webwxsync();
                            } else if (arr[1] == 3) {
                            } else if (arr[1] == 0) {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
//                                e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
//                            e.printStackTrace();
                            }
                        }
                    }


                }
            }, "listenMsgMode").start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

