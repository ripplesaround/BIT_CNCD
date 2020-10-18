/*
目标：
1. 使用ServerSocket建立与浏览器的链接，获取请求协议
2. 返回响应协议
3. 封装响应信息
 */

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
            Request request = new Request(client);

            Response response = new Response(client);
            //关注了内容
            //加入了Servlet，解耦了业务代码
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if(null!=servlet){
                servlet.service(request,response);
                //关注了状态码
                response.pushToBrowser(200);
            }
            else {
                //error
                response.pushToBrowser(404);
            }
//            if(request.getUrl().equals("login")){
//                servlet = new LoginServlet();
//            }
//            else if(request.getUrl().equals("reg")){
//                servlet = new RegisterServlet();
//            }
//            else{
//                //todo
//            }

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("客户端错误");
        }
    }
}