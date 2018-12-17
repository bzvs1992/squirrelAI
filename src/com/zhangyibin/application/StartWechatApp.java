package com.zhangyibin.application;

import com.zhangyibin.application.speciauserslist.SpecialUsersList;
import com.zhangyibin.foundation.wechatapp.WechatApp;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 类：Start_WechatApp
 * 作用：启动程序；
 */

public class StartWechatApp {

    public static void GETStartWechatApp() {
        try {

            List<String> list = new ArrayList<String>();
            list.addAll(SpecialUsersList.getSpecialUsersList());// 将特殊账号传入list
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String strName = iterator.next();
                System.out.println("无需回复消息的用户列表:" + strName);
            }


            WechatApp wechatApp = new WechatApp(list);

            String uuid = wechatApp.getUUID();
            //如果没有获取到uuid，返回"uuid获取失败"
            if (null == uuid) {
                wechatApp.LOGGER.info("[*] uuid获取失败");
            } else {
                wechatApp.LOGGER.info("[*] 获取到uuid为 [%s]"+ wechatApp.Uuid);
                //获取到uuid，则显示微信的登陆二维码
                wechatApp.showQrCode();

                //waitForLogin() 扫描二维码并登陆，如果code 返回200 那么说明微信登陆成功
                while (!wechatApp.waitForLogin().equals("200")) {
                    // 每次循环一次登陆不成功，延迟2秒后在请求登陆，直到登陆成功
                    Thread.sleep(2000);
                }

                // 登陆成功后关闭二维码窗口
                wechatApp.closeQrWindow();

                // .login() 登陆微信
                // 如果微信登陆失败，return推出程序
                if (!wechatApp.login()) {
                    wechatApp.LOGGER.info("微信登录失败");
                    return;
                }

                // 登陆成功
                wechatApp.LOGGER.info("[*] 微信登录成功");

                // .wxInit() 初始化微信
                // 如果微信初始化失败，return推出程序
                if (!wechatApp.wxInit()) {
                    wechatApp.LOGGER.info("[*] 微信初始化失败");
                    return;
                }

                // 微信初始化成功
                wechatApp.LOGGER.info("[*] 微信初始化成功");

                //.wxStatusNotify()微信状态通知
                //如过通知失败，return推出程序
                if (!wechatApp.wxStatusNotify()) {
                    wechatApp.LOGGER.info("[*] 开启状态通知失败");
                    return;
                }

                // 状态通知成功
                wechatApp.LOGGER.info("[*] 开启状态通知成功");

                //.getContact() 获取联系人
                if (!wechatApp.getContact()) {
                    wechatApp.LOGGER.info("[*] 获取联系人失败");
                    return;
                }

                wechatApp.LOGGER.info("[*] 获取联系人成功");
                wechatApp.LOGGER.info("[*] 共有 %d 位联系人"+ wechatApp.ContactList.size());

                // 监听消息
                wechatApp.listenMsgMode();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


}

    /* 测试注销

    public static void main(String[] args){
        try {

            List<String> list = new ArrayList<String>();
            list.addAll(SpecialUsersList.getSpecialUsersList());// 将特殊账号传入list
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String strName = iterator.next();
                System.out.println("无需回复消息的用户列表:" + strName);
            }


            WechatApp wechatApp = new WechatApp(list);

            String uuid = wechatApp.getUUID();
            //如果没有获取到uuid，返回"uuid获取失败"
            if (null == uuid) {
                wechatApp.LOGGER.info("[*] uuid获取失败");
            } else {
                wechatApp.LOGGER.info("[*] 获取到uuid为 [%s]"+ wechatApp.Uuid);
                //获取到uuid，则显示微信的登陆二维码
                wechatApp.showQrCode();

                //waitForLogin() 扫描二维码并登陆，如果code 返回200 那么说明微信登陆成功
                while (!wechatApp.waitForLogin().equals("200")) {
                    // 每次循环一次登陆不成功，延迟2秒后在请求登陆，直到登陆成功
                    Thread.sleep(2000);
                }

                // 登陆成功后关闭二维码窗口
                wechatApp.closeQrWindow();

                // .login() 登陆微信
                // 如果微信登陆失败，return推出程序
                if (!wechatApp.login()) {
                    wechatApp.LOGGER.info("微信登录失败");
                    return;
                }

                // 登陆成功
                wechatApp.LOGGER.info("[*] 微信登录成功");

                // .wxInit() 初始化微信
                // 如果微信初始化失败，return推出程序
                if (!wechatApp.wxInit()) {
                    wechatApp.LOGGER.info("[*] 微信初始化失败");
                    return;
                }

                // 微信初始化成功
                wechatApp.LOGGER.info("[*] 微信初始化成功");

                //.wxStatusNotify()微信状态通知
                //如过通知失败，return推出程序
                if (!wechatApp.wxStatusNotify()) {
                    wechatApp.LOGGER.info("[*] 开启状态通知失败");
                    return;
                }

                // 状态通知成功
                wechatApp.LOGGER.info("[*] 开启状态通知成功");

                //.getContact() 获取联系人
                if (!wechatApp.getContact()) {
                    wechatApp.LOGGER.info("[*] 获取联系人失败");
                    return;
                }

                wechatApp.LOGGER.info("[*] 获取联系人成功");
                wechatApp.LOGGER.info("[*] 共有 %d 位联系人"+ wechatApp.ContactList.size());

                // 监听消息
                wechatApp.listenMsgMode();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
