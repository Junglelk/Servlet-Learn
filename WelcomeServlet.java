import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

public class WelcomeServlet implements Servlet{
    public void init(ServletConfig config) throws ServletException{
        
    }
    public void service(ServletRequest request,ServletResponse response)
        throws IOException,ServletException{
        //将信息输出到浏览器上
        //将html字符串输出到浏览器上，浏览器解释执行
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("<head>");
        out.print("<title>welcome servlet</title>");
        out.print("</head>");
        out.print("<h1 align=\"center\">welcome study servlet!</h1>");
        out.print("</body>");
        out.print("</html>");
    }
    public void destroy(){}
    public String getServletInfo(){return null;}
    public ServletConfig getServletConfig(){return null;}
}
