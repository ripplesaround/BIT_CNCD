package user;

import server.core.Request;
import server.core.Response;
import server.core.Servlet;

public class OthersServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.print("其他测试页面");
    }
}
