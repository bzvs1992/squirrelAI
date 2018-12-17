package com.zhangyibin.foundation.wechatapp;


/**
 * 类：QRCodeFrame
 * 作用：用于展示微信的登陆二维码
 */

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class QRCodeFrame extends JFrame {

    private static final long serialVersionUID = 8550014433017811556L;
    private JPanel contentPane= new JPanel() ;
    private static final String FilePath="image.png";


    /**
     * 启动应用程序
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//                    new QRCodeFrame("a.png");
                    new QRCodeFrame(FilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建窗口
     * filePath为图片地址
     */
    @SuppressWarnings("serial")
    public QRCodeFrame(final String filePath) {
        setBackground(Color.WHITE);
        this.setResizable(false);
        //this.setUndecorated(true);//二维码窗口无边框
        this.setTitle("\u626b\u7801\u767b\u9646\u5fae\u4fe1");//扫码登陆微信
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 295, 315);
        this.contentPane.setBackground(new Color(102, 153, 255));
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.contentPane.setLayout(null);

        JPanel qrcodePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon(filePath);
                // 图片随窗体大小而变化
                g.drawImage(icon.getImage(), 0, 0, 301, 301, this);
            }
        };
        qrcodePanel.setBounds(0, 0, 295, 295);

        this.contentPane.add(qrcodePanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
