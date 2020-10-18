import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable{
    private Socket client;
    private Request request;
    private Response response;

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            request = new Request(client);
            response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            this.release();
        }
    }

    @Override
    public void run() {
        //关注了内容
        //加入了Servlet，解耦了业务代码

        try {
//            System.out.println("url-->"+request.getUrl());
            if(null == request.getUrl()  || request.getUrl().equals("")){
                InputStream is = Thread.currentThread().
                        getContextClassLoader().getResourceAsStream("index.html");
                if(is!=null){
                    response.print((new String(is.readAllBytes())));
                    is.close();
                }
                response.pushToBrowser(200);
                release();  //不要忘记释放资源
                return;
            }

            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (null != servlet) {
                servlet.service(request, response);
                //关注了状态码
                response.pushToBrowser(200);
            } else {
                //error
                InputStream is = Thread.currentThread().
                        getContextClassLoader().getResourceAsStream("error.html");
                if(is!=null){
                    response.print((new String(is.readAllBytes())));
                    is.close();
                }
                response.pushToBrowser(404);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.println("马上好");
                response.pushToBrowser(500);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        release();
    }

    private void release(){
        try {
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
