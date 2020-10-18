import java.io.IOException;
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
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (null != servlet) {
                servlet.service(request, response);
                //关注了状态码
                response.pushToBrowser(200);
            } else {
                //error
                response.pushToBrowser(404);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
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
