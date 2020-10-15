

/*
目标：使用ServerSocket建立与浏览器的链接，获取请求协议

 */

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer{
    private ServerSocket serversocket;
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
            receive();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
    //停止服务
    public void stop(){

    }
    //接受连接处理
    public void receive(){
        Socket client;
        try {
            client = serversocket.accept();
            System.out.println("一个客户端已建立了连接......");
            //获取请求协议
            InputStream is = client.getInputStream();
            // 应该逐行读取，这里一次性读取了
            byte[] datas = new byte[1024*1024];
            int len = is.read(datas);
            String requestInfo = new String(datas,0, len);
            System.out.println(requestInfo);

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("客户端错误");
        }
    }
}