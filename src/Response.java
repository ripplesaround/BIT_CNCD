import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/*

 */
public class Response {
    private BufferedWriter bw;
    // 正文
    private StringBuilder content ;
    private int content_len;
    // 协议头信息
    private StringBuilder headInfo ;

    private final String BLANK = " ";
    private final String CRLF = "\r\n";
    private Response() {
        content = new StringBuilder();
        headInfo = new StringBuilder();
        content_len = 0;
    }
    public Response(Socket client) {
        //重载构造器
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            headInfo = null;
        }
    }
    public Response(OutputStream os) {
        //重载构造器
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    //动态添加内容
    public Response print(String info){
        content.append(info);
        content_len+=info.getBytes().length;
        return this;
    }
    public Response println(String info){
        content.append(info).append(CRLF);
        content_len+=(info+CRLF).getBytes().length;
        return this;
    }


    //构建头信息
    private void create_headInfo(int code){
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(code).append(BLANK);
        switch (code){
            case 200:
                headInfo.append("OK").append(CRLF);
                break;
            case 404:
                headInfo.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                headInfo.append("SERVER ERROR").append(CRLF);
                break;
            default:
                break;
        }
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("test Server/0.0.1;charset=gbk").append(CRLF);
        headInfo.append("Content-type:text/html").append(CRLF);
        headInfo.append("Content-length:").append(content_len).append(CRLF);
        headInfo.append(CRLF);
    }

    //推送响应头信息
    public void pushToBrowser(int code) throws IOException {
        if(null == headInfo){
            code = 505;
        }
        create_headInfo(code);
        bw.append(headInfo);
        bw.append(content);
        bw.flush();
    }
}
