import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
    private static Webcontext web_context;
    static {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parse = factory.newSAXParser();
            WebHandler handler = new WebHandler();

            parse.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"),
                    handler);

            web_context = new Webcontext(handler.getEntities(),handler.getMappings());

        }catch (Exception e){
            System.out.println("解析配置文件失败");
        }
    }
    /*
    通过url获取配置文件对应的servlet
     */
    public static Servlet getServletFromUrl(String url) {
        try {

            String classname = web_context.getClz("/"+url);
            Class clz = null;
            clz = Class.forName(classname);
            Servlet servlet = (Servlet)clz.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
