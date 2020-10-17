import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/*
封装请求协议：获取method uri以及请求参数
 */
public class Request {
    private String requestInfo;
    //请求方式
    private String request_method;
    //请求url
    private String url;
    //请求参数
    private String queryStr;


    private final String CRLF = "\r\n";
    private Request() {
    }
    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }
    public Request(InputStream is) {
        // 应该逐行读取，这里一次性读取了
        byte[] datas = new byte[1024*1024];
        int len;
//        String requestInfo;
        try {
            len = is.read(datas);
            this.requestInfo = new String(datas,0,len);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
       //分解字符串
        parseRequestInfo();
    }
    private void parseRequestInfo(){
        System.out.println("---分解---");
//        System.out.println(requestInfo);
        // 获取请求方式：开头到第一个/
        this.request_method = this.requestInfo.substring(0,this.requestInfo.indexOf("/"));
        this.request_method = this.request_method.trim();
//        System.out.println(request_method);
        //1 获取/
        int startindex = this.requestInfo.indexOf("/") + 1;
        //2 获取HTTP/的位置
        int endindex = this.requestInfo.indexOf("HTTP") ;
        //3 分割字符串
        this.url = this.requestInfo.substring(startindex,endindex); //会不会有个空格
//        System.out.println(url);
        //4 获取？的位置
        int queryidx = this.url.indexOf("?");
        if (queryidx>=0){
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }
//        System.out.println(url);
        // Get方法在这里就已经获取到了参数，post可能在请求体中

        if (request_method.equals("post")){
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            if(null==queryStr){
                queryStr = qStr;
            }
            else{
                queryStr += "&" + qStr;
            }
        }
        queryStr = null==queryStr?"":queryStr;
        System.out.println(request_method + " --> " + url + " --> " + queryStr);
    }
}
