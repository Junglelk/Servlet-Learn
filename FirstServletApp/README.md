## 带有Servlet的webapp

先开发后部署。  
新建一个文件夹，名称为FirstServletApp。内必须有一个名为**WEB-INF**文件夹，该文件夹内必须有一个**classes**文件夹、一个**lib**文件夹、一个web.xml文件。  
新建一个Java文件：

```java
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class HelloServlet implements Servlet
{
    public void init(ServletConfig config) throws ServletException{

    }
    public void service(ServletRequest request,ServletResponse response) throws IOException,ServletException{
        System.out.println("Hello Servlet!");
    }
    public void destroy(){

    }
    public String getServletInfo(){
        return null;
    }
    public ServletConfig getServletConfig(){
        return null;
    }

}
```

将编译生成的class文件放置于webapp内的WEB-INF文件夹内的classes文件夹内。
配置web.xml文件

```xml

<!--直接复制粘贴Tomcat的conf目录下的web.xml文件内相关数据即可，注意不用复制注释-->
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="4.0" 
xmlns="http://xmlns.jcp.org/xml/ns/javaee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
<servlet>
    <servlet-name>thisIsServletName</servlet-name>
    <servlet-class>HelloServlet</servlet-class>
</servlet>
    <!--上面是一个servlet配置，标志名为thisIsServletName；依托实现类为HelloServlet-->
    <!--下面是servlet池，包含所有配置的servlet，以标志名识别，以请求路径为别名，此别名是访问时的输入名-->
<servlet-mapping>
    <servlet-name>thisIsServletName</servlet-name>
    <!--路径随意编写但必须以"/"开始-->
    <!--此路径只是一个虚拟路径，只是代表一个资源的名称-->
    <!--所以此路径可任意，但必须以"/"开头-->
    <!--可设置多个路径，但必须以"/"开头-->
    <url-pattern>请求路径</url-pattern>
</servlet-mapping>
</web-app>
```

有如下注意点：

* 上面为一个合法的web.xml文件
* 一个webapp只能有一个web.xml文件
* web.xml文件主要配置请求路径和Servlet之间的绑定关系
* web.xml文件在Tomcat服务器启动阶段被解析
* web.xml文件解析失败，会导致webapp启动失败
* web.xml内的标签不能随意编写，要按照*.xsd中的规范编写，Tomcat服务器内置此规范
* web.xml内的标签也是SUN公司制定的Servlet规范

部署：复制粘贴该文件夹到tomcat根目录下的webapps文件夹内。
