package com.learn.client.view;

import com.learn.client.service.ClientService;

import java.util.Scanner;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class View {
    private boolean loop = true;
    private Scanner scanner = new Scanner(System.in);
    private String key = "";
    private ClientService clientService = new ClientService();  //用于登录/注册

    public static void main(String[] args) {
        new View().showMenu();
    }

    public void showMenu() {
        while (loop) {
            System.out.println("==========欢迎登录网络通信系统==========");
            System.out.println("\t\t\t1 登录系统");
            System.out.println("\t\t\t9 退出系统");
            System.out.print("请输入你的选择：");
            key = scanner.next();
            if (key.equals("1")) {
                System.out.print("请输入用户名：");
                String uid = scanner.next();
                System.out.print("请输入密  码：");
                String pwd = scanner.next();
                if (clientService.checkUser(uid, pwd)) {
                    while (loop) {
                        System.out.println("==========用户" + uid + "，欢迎==========");
                        System.out.println("==========网络通信系统二级菜单（用户" + uid + "）==========");
                        System.out.println("\t\t\t1 显示在线用户列表");
                        System.out.println("\t\t\t2 群发消息");
                        System.out.println("\t\t\t3 私聊消息");
                        System.out.println("\t\t\t4 发送文件");
                        System.out.println("\t\t\t9 退出系统");
                        System.out.print("请输入你的选择：");
                        key = scanner.next();
                        switch (key) {
                            case "1":
                                clientService.onlineFriendList();
                                break;
                            case "2":
                                break;
                            case "3":
                                break;
                            case "4":
                                break;
                            case "9":
                                loop = false;
                                clientService.logout();
                                break;
                        }
                    }
                } else {
                    System.out.println("==========登录失败==========");
                }
            } else if (key.equals("9")) {
                loop = false;
            }
        }
    }
}
