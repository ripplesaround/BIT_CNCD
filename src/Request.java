import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.*;

/*
封装请求协议：获取method uri以及请求参数
封装请求参数
 */
public class Request {
    private String requestInfo;
    //请求方式
    private String request_method;
    //请求url
    private String url;
    //请求参数
    private String queryStr;
    private Map<String, List<String>> parameterMap;

    private final String CRLF = "\r\n";
    private Request() {
    }
    public String getRequest_method() {
        return request_method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }
    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }
    public Request(InputStream is) {
        parameterMap = new HashMap<String, List<String>>();
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

        if (request_method.equals("POST")){
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
//            System.out.println("qStr-->"+qStr);
            if(null==queryStr){
                queryStr = qStr;
            }
            else{
                queryStr += "&" + qStr;
            }
        }
        queryStr = null==queryStr?"":queryStr;
        System.out.println(request_method + " --> " + url + " --> " + queryStr);
        //转成Map
        //请求参数测试： fav=1&fav=2&uname=test&age=18&others=
        convert2Map();
    }
    //处理请求参数为Map
    private void convert2Map(){
        //分割字符串 &
        String[] keyValues = this.queryStr.split("&");
        for(String query:keyValues){
            //再次分割字符串 =
            String[] kv = query.split("=");
            kv = Arrays.copyOf(kv,2);   //填充
            //获取key和value
            String key = kv[0];
            String value = kv[1]==null?null:decode(kv[1],"utf-8");
            //存储到 map 中
            if(!parameterMap.containsKey(key)){
                parameterMap.put(key,new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
//            System.out.println("key-->"+key+"  value:-->"+value);
        }
    }
    //处理中文
    private String decode(String value,String enc){
        try {
            return java.net.URLDecoder.decode(value,enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    //通过name获取对应的多个值
    public String[] getParameterValues(String key){
        List<String> list = this.parameterMap.get(key);
        if(null==list||list.size()<1){
            return null;
        }
        return list.toArray(new String[0]);
    }
    //通过name获取对应的一个值
    public String getParameterValue(String key){
        String[] values = getParameterValues(key);
        return values == null?null:values[0];
    }



}
