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
//            //获取请求协议
//            InputStream is = client.getInputStream();
//            // 应该逐行读取，这里一次性读取了
//            byte[] datas = new byte[1024*1024];
//            int len = is.read(datas);
//            String requestInfo = new String(datas,0, len);
            Request request = new Request(client);

            Response response = new Response(client);
            //关注了内容
            response.print("<html>");
            response.print("<head>");
            response.print("<title>");
            response.print("服务器响应成功");
            response.print("</title>");
            response.print("</head>");
            response.print("<body>");
            response.print("测试 very good " + request.getParameterValue("uname"));
            response.print("</body>");
            response.print("</html>");
            //关注了状态码
            response.pushToBrowser(200);

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("客户端错误");
        }
    }
}