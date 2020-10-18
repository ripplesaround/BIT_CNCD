package user;

import server.core.Request;
import server.core.Response;
import server.core.Servlet;

public class LoginServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        response.print("<html>");
        response.print("<head>");
        response.print("<meta http-equiv=\"content-type\" content=\"text/html\" charset=\"UTF-8\">");
        response.print("<title>");
        response.print("登录");
        response.print("</title>");
        response.print("</head>");
        response.print("<body>");
        response.print("we finally meet again, " + request.getParameterValue("uname"));
        response.print("</body>");
        response.print("</html>");

    }
}
