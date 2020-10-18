public class LoginServlet implements Servlet{

    @Override
    public void service(Request request,Response response) {
        response.print("<html>");
        response.print("<head>");
        response.print("<title>");
        response.print("第一个脚本");
        response.print("</title>");
        response.print("</head>");
        response.print("<body>");
        response.print("测试 very hello " + request.getParameterValue("uname"));
        response.print("</body>");
        response.print("</html>");

    }
}
