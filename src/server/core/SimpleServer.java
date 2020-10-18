package server.core;/*
目标：
1. 使用ServerSocket建立与浏览器的链接，获取请求协议
2. 返回响应协议
3. 封装响应信息
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer{
    private ServerSocket serversocket;
    private boolean isRunning;
    public static void main(String[] args){
        SimpleServer server = new SimpleServer();
        server.start();
    }

    public SimpleServer() {
    }

    //启动服务
    public void start(){
        try {
            serversocket = new ServerSocket(8888);
            isRunning = true;
            receive();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
            stop();
        }
    }
    //停止服务
    public void stop(){
        isRunning = false;
        try {
            this.serversocket.close();
            System.out.println("服务器已停止");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //接受连接处理
    public void receive(){
        while (isRunning) {
            Socket client;
            try {
                client = serversocket.accept();
                System.out.println("一个客户端已建立了连接......");
                // 多线程
                new Thread(new Dispatcher(client)).start();
            } catch (java.lang.Exception e) {
                e.printStackTrace();
                System.out.println("客户端错误");
            }
        }
    }
}