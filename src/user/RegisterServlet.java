package user;

import server.core.Request;
import server.core.Response;
import server.core.Servlet;

public class RegisterServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {

        response.print("<html>");
        response.print("<head>");
        response.print("<meta http-equiv=\"content-type\" content=\"text/html\" charset=\"UTF-8\">");
        response.print("<title>");
        response.print("注册成功");
        response.print("</title>");
        response.print("</head>");
        response.print("<body>");

        String uname = request.getParameterValue("uname");
        String[] favs = request.getParameterValues("fav");
        response.println("注册信息-->"+ uname+" 喜欢：");
        for(String temp:favs){
            if(temp.equals("0")){
                response.print("吃饭");
            }
            else if(temp.equals("1")){
                response.print("看论文");
            }
            else if(temp.equals("2")){
                response.print("写程序");
            }
        }
//        response.print("测试 very hello " + request.getParameterValue("uname"));
        response.print("</body>");
        response.print("</html>");
    }
}
