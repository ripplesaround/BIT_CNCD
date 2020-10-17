/*
目标：
1. 使用ServerSocket建立与浏览器的链接，获取请求协议
2. 返回响应协议

 */

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

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
//            System.out.println(requestInfo);

            StringBuilder content = new StringBuilder();
            content.append("<html>");
            content.append("<head>");
            content.append("<title>");
            content.append("服务器响应成功");
            content.append("</title>");
            content.append("</head>");
            content.append("<body>");
            content.append("测试 test");
            content.append("</body>");
            content.append("</html>");
            int content_len = content.toString().getBytes().length; //字节数
            // 返回
            StringBuilder responseInfo = new StringBuilder();
            String blank = " ";
            String CRLF = "\r\n";
            //1、 响应行： HTTP/1.1 200 OK  注意有空格
            responseInfo.append("HTTP/1.1").append(blank);
            responseInfo.append(200).append(blank);
            responseInfo.append("OK").append(CRLF);
            //2、 响应头：最后有空行
            /*
            Data: Sat,17Oct200904:25:57GMT
            Server:test Server/0.0.1;charset=GBK
            Content-type:text/html
            Content-length:39725426
             */
            responseInfo.append("Date:").append(new Date()).append(CRLF);
            responseInfo.append("Server:").append("test Server/0.0.1;charset=gbk").append(CRLF);
            responseInfo.append("Content-type:text/html").append(CRLF);
            responseInfo.append("Content-length:").append(content_len).append(CRLF);
            responseInfo.append(CRLF);
            //3、 正文
            responseInfo.append(content.toString());
            //写出到客户端
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bw.write(responseInfo.toString());
            bw.flush();
            System.out.println(responseInfo);

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("客户端错误");
        }
    }
}