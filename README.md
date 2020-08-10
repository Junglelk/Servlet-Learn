# Servlet学习记录

在学SSM框架时，其中的Spring MVC本质上是Servlet，所以打算跟着课程学习一下Servlet。

## 什么是Servlet

Servlet是Java EE的一个规范，用于连接web服务器与服务端小程序。本记录依赖于动力节点杜聚宾老师的Servlet教程。从开发Servlet程序过程中学习Servlet。

## Servlet应用 

### 第一个webapp

先开发后部署。  
开发：新建一个文件夹，此文件夹就是一个webapp，不妨命名为FirstWebApp，在里面新建一个网页，这个网页就是此webapp的内容。  
部署：将此文件夹复制粘贴到tomcat根目录下webapps文件夹内。  

1. 启动tomcat;
2. 打开浏览器
3. 输入localhost:8080:FirstWebApp/login.html
4. 回车

### 带有Servlet的webapp

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

### 输出到浏览器

上面虽可以输出到浏览器，但过于单调，采用如下方法以html格式自定义文字类型到浏览器。

```java
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

```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="4.0" 
xmlns="http://xmlns.jcp.org/xml/ns/javaee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
<servlet>
    <servlet-name>thisIsServletName</servlet-name>
    <servlet-class>WelcomeServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>thisIsServletName</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>
</web-app>
```

配置方式大同小异。

### 连接数据库

以下采用Intelj IDEA进行编写。Tomcat配置方法为：RUN→Edit Configurations→“**+**”→选择Tomcat填入Tomcat服务器所在文件夹即可。
新建一个module，选择Java web开发，在web目录下的WEB-INF文件夹下新建lib文件夹用于存放依赖库：mysql-connector-java-5.1.45.jar。

```java
package com.bjpowernode.javaweb.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ListEmpServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse)
            throws ServletException, IOException {
        //web服务主要就在这里
        servletResponse.setContentType("text/html;charset=UTF-8");
        PrintWriter out =servletResponse.getWriter();
        
        out.print("<!DOCTYPE html>");
        out.print("<html lang='en'>");
        out.print("<head>");
        out.print("<meta charset='UTF-8'>");
        out.print("<title>员工信息</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<h3 align='center'>员工列表</h3>");
        out.print("<hr width='60%'>");
        out.print("<table border='1' align='center' width='50%'>");
        out.print("<tr align='center'>");
        out.print("<th>员工姓名</th>");
        out.print("<th>员工地址</th>");
        out.print("<th>员工邮箱</th>");
        out.print("</tr>");

        //JDBC，此处连接数据库
        Connection conn = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/自己数据库名","用户名","密码");
            String sql = "SELECT realName,address,email FROM user_Info;";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String realName=resultSet.getString("realName");
                String address =resultSet.getString("address");
                String email = resultSet.getString("email");
                out.print("<tr align='center'>");
                out.print("<th>"+realName+"</th>");
                out.print("<th>"+address+"</th>");
                out.print("<th>"+email+"</th>");
                out.print("</tr>");
            }
    } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try{
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try{
                    conn.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        out.print("</table>");
        out.print("</body>");
        out.print("</html>");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    
    }
}
```



xml文件配置，大同小异。



```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>listEmp</servlet-name>
        <servlet-class>com.bjpowernode.javaweb.servlet.ListEmpServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>listEmp</servlet-name>
        <url-pattern>/system/list</url-pattern>
    </servlet-mapping>
</web-app>
```



## Servlet生命周期



1. 什么是生命周期？
   * 表示一个Java对象从最初被创建到最后被销毁，经历的所有过程。
2. Servlet对象的生命周期是谁来管理的，程序员是否能干涉？
   * Servlet对象的生命周期，javaweb程序员是无权干涉的，包括该Servlet对象的相关方法的调用，javaweb程序员无权干涉。
     Servlet对象从创建、方法调用以及最后的销毁，整个过程，是由web容器来管理的。
     Web Container管理Servlet对象的生命周期。
3. **默认情况**下，Servlet对象在Web服务器启动阶段不会被实例化。
   * 若要在启动阶段实例化对象，需要进行额外配置
4. Servlet对象生命周期
   1. 用户在浏览器上输入URL
   2. web容器截取请求路径
   3. web容器在容器上下文中找请求路径对应的Servlet对象
   4. 若未找到对应的Servlet
      1. 通过web.xml文件中的相关配置信息，得到请求路径对应的类名
      2. 通过反射机制，调用Servlet的无参构造方法完成Servlet对象的实例化
      3. web容器调用Servlet对象的init方法完成初始化操作
      4. web容器调用Servlet对象的service方法提供服务
   5. 若找到对应的Servlet对象
      1. web容器调用Servlet对象的service方法提供服务
   6. 若web容器关闭或webapp重新部署或该Servlet对象没有再次访问时，web容器会将Servlet对象销毁，在销毁对象之前会调用destroy方法进行销毁前的准备。
5. 总结
   * Servlet类的构造方法只执行一次
   * Servlet类的init方法只执行一次
   * Servlet类的service方法，只要用户请求一次，则执行一次
   * Servlet类的destroy方法只执行一次
6. 注意：
   * init方法执行时，Servlet对象已创建
   * destroy方法执行时，Servlet对象还没被销毁
7. Servlet是单例，但不符合单例模式，只能称为伪单例，真单例的构造方法是私有化的。Tomcat服务器是支持多线程的。所以Servlet对象对象在单实例多线程环境下运行的。那么Servlet对象中若有实例变量，并且实例变量涉及到修改操作，那么这个Servlet对象一定会存在线程安全问题，因此不建议在Servlet对象中使用实例变量，尽量使用局部变量。

```java
package com.javaweb.test.servlet;

import javax.servlet.*;
import java.io.IOException;

public class HelloServlet implements Servlet {
    /**
     * 一个无参构造方法
     * @param servletConfig
     * @throws ServletException
     */
    public HelloServlet(){
        System.out.println("Hello Servlet'Constructor execute!");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("Hello Servlet'init method execute!");
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        System.out.println("Hello Servlet'service method execute!");
    }
    @Override
    public void destroy() {
        System.out.println("Hello Servlet'destroy method execute!");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }
    @Override
    public String getServletInfo() {
        return null;
    }
}
//应用部署后Console上显示为
Hello Servlet'Constructor execute!
Hello Servlet'init method execute!
//多次调用service
Hello Servlet'service method execute!
Hello Servlet'service method execute!
    ...
服务器关闭后
Hello Servlet'destroy method execute!
```

8. 若要在Tomcat服务器启动时，实例化Servlet对象，可以修改web.xml文件，添加\<load-on-startup>自然数\</load-on-startup>。自然数越小启动优先级越高。

9. Servlet对象实例化后实例化后这个Servlet对象存储到哪里了？

   * 大多数的web容器都是讲Servlet对象以及对应的url-parttern存储到Map集合中了：
     在web容器中有这样一个Map集合，Map<String,Servlet>。

10. 服务器在启动时，就会解析各个webapp的web.xml文件，然后做了什么?

    * 将web.xml文件中的url-pattern对应的Servlet完整类名存储到Map集合中了
    * 在WEB容器中有这样一个集合Map<String,String>，key是路径名，value是完整的类名。

11. Servlet接口中编写什么代码？什么时候使用这些方法？

    * 无参构造方法
    * init方法

    上述两方法执行时间几乎同时执行，且均仅执行一次。构造方法执行时，对象正在创建，init执行时，对象已经创建。

    若系统要求在对象创建时执行某一段特殊的程序，则应写在init方法中。

    * service方法
      * 必然重写
    * destroy方法
      * 若希望在销毁时执行某一段程序，写在这里

    回顾：类加载时刻执行程序，代码写在哪里？静态代码块中。



## ServletConfig接口

新建一个JavaWeb项目，添加两个类：AServlet、BServlet。

配置如下：



```java
package com.javaweb.config.servlet;

import javax.servlet.*;
import java.io.IOException;

public class AServlet implements Servlet {
//仅列出A，B于此同理。
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("BServlet's ServletConfig = "+config.toString());
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws IOException,ServletException {

    }

    @Override
    public void destroy(){

    }

    @Override
    public ServletConfig getServletConfig(){
        return null;
    }

    @Override
    public String getServletInfo(){
        return null;
    }
}

```



启动Tomcat，得到输出结果为：

```java
AServlet's ServletConfig = org.apache.catalina.core.StandardWrapperFacade@45928713
BServlet's ServletConfig = org.apache.catalina.core.StandardWrapperFacade@12fd89f9
```

由Object类的toString方法知，“@”前为类名，后为哈希地址，而ServletConfig是Servlet内的对象，却是由Tomcat中的类实现的，所以，ServletConfig为一接口，此接口由Tomcat实现。

1. javax.servlet.ServletConfig是接口

2. Apache Tomcat服务器实现了Servlet规范，Tomcat服务器专门写了一个ServletConfig接口的实现类：org.apache.catalina.core.StandardWrapperFacade。准确说是由服务器软件实现Servlet的规范。

3. JavaWeb程序员编程时，一直面向ServletConfig接口去完成调用，不需要关心具体的实现类。

4. Tomcat是实现了Servlet规范和JSP规范的容器。

5. ServletConfig接口中有哪些方法？

    * String getServletName();

      * 获取Servlet的name

    * ServletContext getServletContext();

      * 获取“Servlet上下文”上下文对象

    * String getInitParameter(String name);

      * 通过初始化参数的name，获取在web.xml文件中设置好的参数

      * ```xml
        <servlet>
                <servlet-name>a</servlet-name>
                <servlet-class>com.javaweb.config.servlet.AServlet</servlet-class>
            <!--参数各有用途，这里描述的是连接数据库所需的参数-->
                <init-param>
                    <param-name>url</param-name>
                    <param-value>jdbc:mysql://localhost:3306:eshop</param-value>
                </init-param>
                <init-param>
                    <param-name>user</param-name>
                    <param-value>root</param-value>
                </init-param>
                <init-param>
                    <param-name>password</param-name>
                    <param-value>123</param-value>
                </init-param>
            </servlet>
            <servlet-mapping>
                <servlet-name>a</servlet-name>
                <url-pattern>/a</url-pattern>
            </servlet-mapping>
        ```

      * ```java
        public void service(ServletRequest request, ServletResponse response)
                    throws IOException,ServletException {
                ServletConfig config =getServletConfig();
                String user = config.getInitParameter("user");
                String driver = config.getInitParameter("driver");
                String url = config.getInitParameter("url");
                String password = config.getInitParameter("password");
                
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();        
                out.print(driver);
                out.print("<br>");
                out.print(url);
                out.print("<br>");
                out.print(user);
                out.print("<br>");
                out.print(password);
        }
        ```

    * Enumeration\<String> getInitParameterNames();

      * 获取所有参数的name

      * ```java
        response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
        
                Enumeration<String> names = getServletConfig().getInitParameterNames();
                while(names.hasMoreElements()){
                    String name = names.nextElement();
                    String value = config.getInitParameter(name);
                    out.print(name+"="+value);
                    out.print("<br>");
                }
        //主方法与上文一致
        ```


6. ServletConfig到底是什么？

   * ServletConfig是一个Servlet对象的配置信息对象，ServletConfig对象中封装了一个Servlet对象的配置信息。Servlet对象的配置信息在web.xml中。(突然意识到，在上面Servlet对象的生命周期内容中，会先读取web.xml，并将类路径、servlet对象存储在Map中，应该就是为什么上面打印的是一个哈希地址了。)
   * 一个Servlet对象对应一个ServletConfig对象。

7. 将init方法上的ServletConfig参数移植到service中，因为service中可能会使用SevrletConfig

   * 初始化一个ServletConfig实例变量(private ServletConfig config)，在init方法中将局部变量congfig赋值给实例变量config
   * 实现getServletConfig方法，提供公开的get方法，供子类使用。

## ServletContext接口

新建一项目，按照上文的方法打印输出A、BServlet的ServletContext内容

```java
AServlet的ServletContext：org.apache.catalina.core.ApplicationContextFacade@4e29c0f
BServlet的ServletContext：org.apache.catalina.core.ApplicationContextFacade@4e29c0f
//同一个内存地址
```

1. javax.servlet.ServletContext接口，servlet规范

2. Tomcat服务器实现ServletContext接口，Java程序员不需要关心具体实现，仅使用即可

3. ServletContex到底是什么？什么时候创建？什么时候销毁?

   * ServletContext被翻译为Servlet上下文
   * 一个webapp只有一个ServletContext对象
   * 一个webapp只有一个web.xml文件
   * web.xml在服务器启动阶段被解析
   * Servlet在服务器启动阶段被实例化
   * ServletContext在服务器关闭时被销毁
   * ServletContext对应的是web.xml文件，是web.xml文件的代表
   * 用户若想共享同一个数据，可以将这个数据放在ServletContext对象中
   * 一般放置于ServletContext对象中的数据是不建议修改的，因为ServletContext是多线程共享一个对象，修改会涉及“线程安全”

4. ServlertContext接口中有哪些常用方法？

   * void setAttribute(String name,Object object)
     * 向ServletContext中绑定数据
     * 显然是map集合的put方法，下同
     * 如果在编写程序阶段向AServlet对象中存入了数据，则部署后如果不调用AServlet，以生成对象，则此时BServlet不能取出数据。

   * Object getAttribute(String name)
     
     * 从ServletContext中获取数据
   * void removeAttribute(String name)
     
   * 移除ServletContext中的数据
     
   * String getInitParameterName(String name)

   * Enumeration getInitParameterNames()

     * 在XML文件中做如下配置

     * ```xml
       	<context-param>
               <param-name>username</param-name>
               <param-value>admin</param-value>
           </context-param>
       ```

     * 上述两个方法就是返回name使用的，这样这个数据就可以在当前项目下所有Servlet中使用。

   * String getRealPath(String name)

     * 获取文件的绝对路径

     * ```java
       		//获取ServletConfig
               ServletConfig config = getServletConfig();
               //获取ServletContext
               ServletContext application = config.getServletContext();
         		String realPath = application.getRealPath("/index.html");
               System.out.println(realPath);
       输出结果为C:\Users\****\Documents\Servlet-learn-IDEA\out\artifacts\servlet_05_war_exploded3\index.html
       ```

     * 

5. Servlet、ServletContext、ServletConfig之间的关系？

   * 一个Servlet对应一个ServletConfig
   * **所有Servlet共享同一个ServletContext对象**

6. ServletContext可以完成跨用户传递数据

目前为止所有编写的路径:

* 超链接
  * \<a href="路径名">\</a>
* web.xml中的url-pattern
  * \<url-pattern>/路径\</url-pattern>
* form表单的action属性
  * \<form action="/路径">\</form>
* String realPath = application.getRealPath("/WEB-INF/文件名")

## webapp的欢迎页面的设置



### 欢迎页面怎么设置



1. 怎么设置欢迎页面？  
   * 在webapp根目录下创建login.html，想让login.html作为整个webapp的欢迎页面需要做如下设置：
   * 编写XML文件，添加如下行

		```xml
		<welcome-file-list>
    	<welcome-file>login.html</welcome-file>
		</welcome-file-list>
		```

2. 为什么设置欢迎页面？

   * 为了访问方便

3. 欢迎页面可设置多个，越往上，优先级越高

   * ```xml
     <welcome-file-list>
      	<welcome-file>login.html</welcome-file>
      	<welcome-file>css/welcome.html</welcome-file>
     </welcome-file-list>
     ```

4. 欢迎页面设置时，路径不需要以"/"开头

5. 欢迎页面不一定是html文件，也可以是servlet或任意webapp对象

6. 欢迎页面有全局配置和局部配置：

   * 全局配置：Tomcat根目录下conf文件夹内的web.xml
   * 局部配置：webapp内的web.xml
   * 就近原则，先检索局部配置后检索全局配置



## HTTP状态码404、500

在webapp中常见的错误代码：

	* 404-NOT FOUND-资源未找到：请求路径写错了
	* 500-Server Inner Error-服务器内部错位，一般是Java程序出现异常

404和500是HTTP状态码，由W3C制定的，所有浏览器和服务器都必须遵守。

正常响应状态码：200.

可以配置错误提示页面，在web.xml中配置出错后跳转页面

```xml
<error-page>
	<error-code>错误码</error-code>
    <location>/跳转地址</location>
</error-page>
```

## 路径总结



三类：

1. 第一类
   * 以“/”开始，加webapp名
2. 第二类
   * 以“/”开始，不加webapp名
3. 第三类
   * 欢迎页面比较特殊，不以“/”开始，不加webapp名



## 设计模式

### 适配器

项目中不使用缺省适配器模式，有何缺点？

接口Common中有10个方法，A类、B类、C类中均仅使用Common接口中的三个方法，如果直接实现这个接口，需要实现更多非必要方法，代码丑陋。

项目中使用缺省适配器模式：

同上情况，新建一个抽象类Adapter，将A类、B类、C类使用的方法设置为抽象方法，再由A类、B类、C类继承该类。即制造一个中介类，使代码更好看。

### 设计模式分类

1. 创建型：解决对象创建问题
2. 行为型：该模式与方法、行为、算法有关的设计模式
3. 结构型：更多类，更多的对象组合成更大的结构解决某个特定的问题

## GenericServlet

创建一个GenericServlet，用作适配器：

```java
/**
 * 这是一个Servlet，同时也是一个适配器
 * 以后编写webapp无需实现Servlet接口，继承此适配器即可
 */
import javax.servlet.*;
import java.io.IOException;

public abstract class GenericServlet  implements Servlet {
    private ServletConfig config;
    @Override
    public final void init(ServletConfig config) throws ServletException { this.config = config;this.init();}
    public void init(){}
    //上面带参的init方法不可重写，因为一旦重写就会使后面的方法出错
    //若要实现“在servlet对象创建时输出日志，这类行为”，则可以设置一个无参的init方法，并且在带参init创建时调用此方法
    //这个无参方法可以在子类中重写
    @Override
    public ServletConfig getServletConfig() { return config; }
    @Override
    public abstract void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException;
    @Override
    public String getServletInfo() { return null; }
    @Override
    public void destroy() { }

    //以下为拓展方法，方便子类使用
    public ServletContext getServetContext(){ return getServletConfig().getServletContext();}
    //.....
}
```

实际上，SUN公司在Java EE库里已经实现GenericServlet适配器。



## HTTP协议-GET和POST



### HTTP协议的详细内容

1. 什么是HTTP协议？
   * 超文本传输协议
   * 浏览器与服务器之间的一种通讯协议
   * 该协议是由W3C负责指定，本质上就是一种数据传输格式。浏览器和服务器必须按照这种数据格式进行接收和发送
2. 使用的HTTP协议版本号是多少？
   * HTTP1.1
3. HTTP协议包括几部分？
   * 请求协议：从Browser发送到Server的时候采用的数据传输格式
   * 相应协议：从Server发送到Browser的时候采用的数据传输格式
4. 请求协议
   * 请求协议包括四部分
     * 请求行
     * 消息报头
     * 空白行
     * 请求体
   * 请求行包括
     * 请求方式
     * URI
     * 协议版本号
   * 空白行
     * 专门用来分离消息报头和请求体
5. 响应协议
   * 响应协议包括四部分
     * 状态行
     * 响应报头
     * 空白行
     * 响应体
   * 状态行有三部分
     * 协议版本号
     * 状态码
     * 状态描述信息
   * 空白行
     * 用于分离响应报头和响应体
   * 响应协议重点掌握状态码
     * 200-响应成功
     * 404-资源未找到
     * 500-服务器内部错位

### GET请求和POST请求的区别

1. 什么时候是GET请求？什么时候是POST请求？
   * 只有使用表单form ，并且将form标签的method属性设置为method="post"，才是POST请求方式，其余所有的请求方式都是基于GET方式。
2. GET请求和POST请求有何区别？
   * GET请求在请求行上提交数据，格式：uri?name=value&name=value...
     * 这种提交方式最终提交的数据会显示在浏览器地址栏上
   * POST请求在请求体中提交数据，相对安全，提交格式:name=value&name=value&...
     * 这种提交方式不在浏览器地址栏上显示
   * POST请求在请求体上提交数据，所以POST请求提交的数据没有长度限制(POST可以提交大数据)
   * GET请求在请求行上提交数据，所以GET请求提交的数据有长度限制
   * GET请求只能提交字符串数据，POST请求可以提交任意类型数据
   * GET请求的最终结果会被浏览器缓存收纳，而POST不会
3. GET请求和POST请求应当如何选择使用？
   * 敏感数据，必须使用POST
   * 传送数据不是普通字符串，使用POST
   * 传送的数据非常多，使用POST
   * 这个请求是为了修改服务器端资源，使用POST
   * GET请求多数是从服务器中读取资源，这个资源在短期内不会发生变化，所以GET的请求的结果被浏览器缓存起来了
   * POST请求是为了修改服务器端资源，而每一次修改都是不同的，所以没必要缓存下来

## 安装httpwatch工具，进行HTTP协议的监测

用于获取网页发往服务器的数据包。软件名为httpwatch，作为IE浏览器的加载项使用。



## HTTPServlet

1. 前端的页面发送的请求方式应当和服务器端需要的请求方式一致
   * 服务器端需要前端发送POST请求，那前端就应当发送POST请求，否则报错
   * 服务器端需要前端发送GET请求，那前端就应当发送GET请求，否则报错

2. 如何达成上述需求？

   * 在JavaWeb程序中想办法获取请求的类型

   * 获取请求方式后，使用if条件句进行分支返回

   * ```java
     if("POST".equals(method)){
         
     }else if("GET".equals(method)){
         
     }
     ```

3. 怎么在javaweb程序中获取请求方式？

   * 重点：HTTP的请求协议全部信息被自动封装到java.servlet.http.HttpServletRequest对象中

   * 在HttpServletRequest接口类型中有一个方法叫做：String getMethod();可以获取请求方式

   * ```java
     public interface javax.servlet.http.HttpServletRequest extends ServletRequest{
         
     }
     ```

4. 编写上述代码时，因为某一个Servlet对应一个service，如果对不同的功能都进行相关的POST和GET判断，代码复制太高，不优雅。使用HttpServlet继承GenericServlet

   * ```java
     import javax.servlet.GenericServlet;
     import javax.servlet.ServletException;
     import javax.servlet.ServletRequest;
     import javax.servlet.ServletResponse;
     import javax.servlet.http.HttpServletRequest;
     import javax.servlet.http.HttpServletResponse;
     import java.io.IOException;
     import java.io.PrintWriter;
     
     public class HttpServlet extends GenericServlet {
         @Override
         public void service(ServletRequest servletRequest, ServletResponse servletResponse)
                 throws ServletException, IOException {
             HttpServletResponse response = (HttpServletResponse) servletResponse;
             HttpServletRequest request = (HttpServletRequest) servletRequest;
             service(request,response);
         }
         public void service(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException,IOException{
             String method = request.getMethod();
             if("GET".equals(method)){
                 //此时无法继续，因为不知道子类的正确请求方式是GET还是POST，解决方法如下
                 doGet(request,response);
             }else if("POST".equals(method)){
                 doPost(request,response);
             }
         }
         /** 继承此类的子类知道自己
          * 如果实现的是GET请求，就重写doGet方法
          * 如果实现的是POST请求，就重写doPost方法
          * 因为如果是GET请求，则不需要抛出异常，于是重写doGet方法，重写的doGet方法内正常运行即可
          * 而如果是POST请求，则直接传入doPost方法内，输出"405-你应该发送GET请求"
          */
         public void doPost(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException,IOException{
             response.setContentType("text/html;charset=UTF-8");
             PrintWriter out = response.getWriter();
             out.print("405-你应该发送GET请求");
             throw new RuntimeException("405-你应该发送GET请求");
         }
     
         public void doGet(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException,IOException{
             response.setContentType("text/html;charset=UTF-8");
             PrintWriter out = response.getWriter();
             out.print("405-你应该发送POST请求");
             throw new RuntimeException("405-你应该发送POST请求");
         }
     }
     ```

   * **编写一个Servlet类应当继承HttpServlet，get请求重写doGet，post请求重写doPost**

   * **doPost和doGet方法可以等同于main方法**

   * **SUN公司已经实现HttpServlet。**

5. 错误代码405-浏览器发送的请求方式和后台处理方式不同(POST/GET请求)。

### HttpServlet的设计模式



#### 模板方法设计模式



多个类中，有相同的算法(代码段)，如果不使用模板方法设计模式，代码重复率过高，易出错。使用模板方法可以避免此问题。

* 模板方法设计模式可以在不改变算法的前提下可以重新定义算法步骤的实现

* ```java
  public abstract class Template{
      //核心算法框架
      public final void templeMethod(){
      method1();
      method2();
      ...
      }
      //具体实现步骤，由子类完成
      public abstract void do1();
      public abstract void do2();
      public abstract void do3();
  }
  ```

* HttpServlet体现了典型的模板方法设计模式

  * HttpServlet是一个典型的模板方法设计模式
  * HttpServlet是一个模板类
  * 其中service(HttpServletRequest ,HttpServletResponse)方法是典型的模板方法
  * 该方法定义了核心算法框架，doGet、doPost等具体实现类去子类中完成

* 模板方法设计模式的特点

  * doXXX()
  * doYYY()
  * doZZZ()

* 模板方法设计模式属于：行为型设计模式

* 模板方法设计模式主要作用

  * 核心算法得到保护
  * 核心代码得到复用
  * 不改变算法的前提下，可以重新定义算法步骤的具体实现

## HttpServletRequest接口

* HttpServletRequest是一个接口，Servlet规范中重要的接口之一

* 继承关系

  * ```java
    public interface HttpServletRequest extends ServletRequest{}
    ```

* HttpServletRequest由web容器负责实现，Tomcat服务器已经实现该接口，程序员只需调用即可

* HttpServletRequest封装了哪些信息？

  * 封装了HTTP请求的所有内容

    * 请求方式

    * URI

    * 协议版本号

    * 表单提交的数据

      ....

* 表单数据提交的格式

  * POST请求，在请求体中提交，数据格式
    * username=admin&password=123&sex=m&interest=sport&interest=music&grade=dz&introduce=ok
  * 将数据存储在Map中，key是一个String，value是一个数组

* HttpServletRequest一般变量的名字为：request，表示请求，HttpServletRequest对象代表一次请求，一次请求对应一个request对象，多少次请求，多少次对象。所以request对象的生命周期是短暂的。



### HttpServletRequest常用的方法

* 表单提交的数据会自动封装到request中，request对象由上文提到的Map存储这些数据
* String getParameter(String name)
  * 通过key获取value这个一维数组中的首元素(用得最多)
* String[] getParameterValues(String name)
  * 通过Map集合key获取value
* Map getParameterMap()
  * 获取整个Map集合
* Enumeration getParameterNames()
  * 获取整个Map集合的所有key



* void setAttribute(String name,Object o)
  * 向request范围中添加数据
* Object getAttribute(String name)
  * 从request范围中读取数据
* void removeAttribute(String name)
  * 移除request范围中的数据
* HttpServletRequest是一个什么样的范围？
  * HttpServletRequest类型的变量通常命名为：request，代表本次请求。一次请求对应一个request对象，100个请求对应100个对象。请求范围是极小的，request只能完成在**同一次**请求中传递数据。
* 一次请求只能调用一个servlet，如果想在同一次请求中调用两个servlet，则必须使用转发技术*forward*
  * 获取请求转发器对象
  * 调用请求转发器的forward方法



* RequestDispatcher getRequestDispatcher(String path)

  * 这个就是获取请求转发器

  * ```java
    //获取请求转发器对象
    RequestDispatcher dispatcher = request.getRequestDispatcher("/要跳转的目标路径");
    
    //调用亲求转发器完成转发
    dispatcher.forward(request,response);
    ```
    

* void setCharacterEncoding(String env)

  * 解决乱码



* String getRemoteAdder()
  * 获取客户端IP地址
* String getContextPath()
  * 获取上下文路径【webapp的根路径】
* String getMethod()
  * 获取浏览器请求方式
* String getRequestURI()
  * 获取请求的URI
* String getServletPath()
  * 获取Servlet Path
* StringBuffer getRequestURL()
  * 获取请求的URL



* Cookie[] getCookies()
* HttpSession getSession()



#### 关于范围对象的选择

* ServletContext
  * 应用范围，可以跨用户传递数据
* ServletRequest
  * 请求范围，只能在同一次请求中传递数据
* get/set/removeAttribute方法
  * 对同一webapp内的A、B两个servlet。ServletContext执行一次该方法，两个servlet都受影响；ServletRequest执行一次，如果没有转发机制，则只会有当前请求的servlet受影响。
* 优先选择request范围。 



## 程序中的乱码问题

### 乱码会出现的位置：

* 数据传递过程中
* 数据展示过程中
* 数据保存过程中

### 数据保存过程中的乱码

* 最终保存到数据库表中时，数据出现乱码
* 导致这种乱码包括以下两种情况
  1.  保存前，数据本身就是乱码
  2. 保存前，数据本身不是乱码，但由于数据库本身不支持简体中文，导致乱码


### 数据展示过程中的乱码

* 最终显示到网页上的数据出现乱码

* 经过Java程序后，Java程序负责向浏览器响应的时候，中文出现乱码

  * 设置响应的内容类型和字符编码方式

     ```java
      response.setContenttype("text/html;charset=UTF-8");
     ```

* 没经过Java程序，直接访问html页面，出现乱码

  * 使用\<meta content="text/html;charset=UTF-8">或者\<meta charset="UTF-8">

### 数据传递过程中的乱码(重)

将数据从浏览器发送给服务器时，服务器接受的数据是乱码。浏览器采用ISO-8859-1编码发送数据，又被称为latin1。任何语言在浏览器中传输时，都采用ISO-8859-1编码。浏览器直接将数据传送给web服务器(Tomcat)时，web服务器不知道这些数据之前是什么类型的文字，所以出现乱码。

* 万能解决方式，即可解决POST请求乱码也可解决GET请求乱码：

  * 先将服务器接收到的数据采用ISO-8859-1的方式解码，回归原始状态，再采用一种支持简体中文的编码方式重新编码组装。此编码方式需要与浏览器编码方式一致。

  * ```java
    //request对象的getBytes方法
    String dname = request.getParameter("dname");
    //此dname是html文件内表单标签的识别码
    byte[] bytes = dname.getBytes("IOS-8859-1");//解码
    dname = new String(bytes,"UTF-8");
    //此处与原始的html文件的编码方式一致
    System.out.println(dname);
    ```
  
* 第二种解决方案，仅适用于POST请求

  * 调用request的setCharacterEncoding方法，但这种方式只适合POST请求，只对请求体编码，告诉Tomcat服务器，请求体中的数据采用UTF-8的方式进行编码。

* 第三种解决方案，仅适用于GET请求

  * 修改CATALINA_HOME/conf/server.xml文件

  * ```xml
    <Connector port="8080" protocol="HTTP/1.1"
    			connectionTimeout="20000"
    			redirectPort="8443"
    			URIEncoding="UTF-8"/>
    <!--URIEncoding行为修改行-->
    ```

  * Connector标签可以编写哪些属性？

    * 在帮助文档下：CATALINA_HOME\webapps\docs\config\http.html
    * 端口
    * 编码方式
    * 并行线程数
    * ....



## Servlet线程安全问题

1. Servlet是单实例多线程环境下运行的。

2. 什么时候会有线程安全问题

   * 多线程并发
   * 有共享的数据
   * 共享数据有修改操作

3. 在JVM中，哪些数据会存在线程安全问题？

   * 局部变量内部空间不共享，一个线程一个栈，局部变量在栈中存储，局部变量不会存在线程安全问题
   * 常量不会被修改，所以常量不会存在线程安全问题
   * 所有线程共享一个堆
     * 堆内存中new出来的对象在其中存储，对象内部有“实例变量”，所以“实例变量”的内存多线程是共享的。
       实例变量多线程共同访问，并且涉及到修改操作时就会出现线程安全问题。

   * 所有线程共享一个方法区
     * 方法区中有静态变量，静态变量的内存也是共享的，若涉及到修改操作，静态变量也存在线程安全问题。

4. 线程安全问题并不止是体现在JVM中，还有可能发生在是数据库中，例如多个线程共享同一张表，并且同时去修改表中一些记录，这些记录就会存在线程安全问题，如何解决？

   * 第一种方案：在Java程序中使用**synchronized**关键字，线程排队执行，自然不会在数据库中并发，解决线程安全问题。
   * 第二种方案：行级锁【悲观锁】
   * 第三种方案：事务隔离级别，例如：串行化
   * 第四种方案：乐观锁
   * ...

5. 怎么解决线程安全问题？

   * 不使用实例变量，尽量使用局部变量
   * 若必须使用实例变量，那么可以考虑将该对象变成多例对象，一个线程一个Java对象，实例变量的内存也不会共享。
   * 若必须使用单例，那就只能使用synchronized线程同步机制，线程一旦排队执行，则吞吐量降低，降低用户体验。

6. Servlet怎么解决线程安全问题？

   * 不使用实例变量，尽量使用局部变量
   * Servlet必须是单例，所以只能使用synchronized线程同步机制。

## 转发和重定向

关于web系统中资源跳转。

1. 资源跳转包括两种：
   * 转发-forward
   * 重定向-redirect
   
2. 转发和重定向代码如何完成？

   * 转发

     ```java
     request.getRequestDispatcher("/b").forward(request,response);
     ```

   * 重定向

     ```java
     response.sendRedirect(request.getContextPath()+"/b");
     ```

3. 转发和重定向的相同点和不同点

   * 相同点
     * 都可以完成资源跳转
   * 不同点
     * 转发是request对象触发的；重定向是response对触发
     * 转发是一次请求，浏览器地址栏上地址不会发生变化；重定向是两次请求，浏览器地址栏会发生变化
     * 重定向的路径需要加webapp的根路径
     * 转发是在本项目内部完成资源跳转；重定向可以跨app跳转资源。

4. 跳转的下一个资源可以是什么？

   * 跳转的下一个资源可能是web服务器中任何一个资源：可以是Servlet，也可以是HTML...
   * 即地址指向的任意 web资源

5. **什么时候采用转发，什么时候使用重定向？**(大多数情况下使用重定向)

   * 若想完成跨app跳转，必须使用重定向
   * 若在上一个资源中向request范围中存储了数据，希望在下一个资源中从request中范围中将数据取出，必须使用转发
   * 重定向可以解决浏览器的刷新问题

6. 重定向原理是什么？

   * ```java
     response.sendRedirect("/jd/login");
     ```

     程序执行到以上代码，将请求路径/jd/login反馈给浏览器，浏览器自动又向web服务器发送了一次全新的请求：/jd/login  

     浏览器地址栏最终显示的地址为：/jd/login

7. 这句话：在浏览器上点击一个超链接，到网页最终停下来是一次请求。

   * 此时由于有了重定向，这句话已经是错误的了。

### 重定向解决浏览器刷新问题

如果使用转发机制，会使网页在跳转后仍然与原网页有联系，此时如果刷新页面，会导致之前提交的数据再次提交。如果没有此类需求，则会造成相同数据多次提交。此时应采用重定向的模式解决此问题。需结合模块servlet-17。

该模块设置了一个servlet用以更新数据库，servlet的数据来源于一个网页save.html，更新数据成功后跳转至success.html。

```java 
//转发机制
//会存在刷新问题
request.getRequestDispatcher("/success.html").forward(request,response);
//重定向
response.sendRedirect(request.getContextPath()+"/success.html");
```

### 用户登录

设置字符集，获取用户名和密码

```java
/**
         * 设置字符编码方式
         * 获取用户名和密码
         * 连接数据库验证用户名和密码
         * 登录成功和失败页面
         */
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

```



连接数据库、数据查询

```java
//JDBC操作
        Connection conn = null;
        PreparedStatement preparedStatement=null;
        ResultSet rs = null;
        boolean logSuccess =false;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/数据库名","用户名","密码");
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM t_user WHERE username = ? AND password = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                logSuccess = true;
            }
            conn.commit();
        } catch (Exception e){
            if(conn!=null){
                try{
                    conn.rollback();
                }catch(SQLException e1){
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {

            if(rs!=null){
                try{
                    rs.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try{
                    conn.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
```

链接跳转

```java
if(logSuccess){
            response.sendRedirect(request.getContextPath()+"/welcome.html");
        }
        else {
            response.sendRedirect(request.getContextPath()+"/loginError.html");
```

另有登录页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
    <meta charset="UTF-8">
</head>
<body>
<form action="/servlet_18_war_exploded/login" method="post">
    username<input type="text" name="username"><br>
    password<input type="password" name="password"><br>
    <input type="submit" value="login">
</form>
</body>
</html>
```



登录成功页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WELCOME</title>
    <meta charset="UTF-8">
</head>
<body>
欢迎登录
</body>
</html>
```



登录失败页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登陆失败</title>
</head>
<body>
登陆失败，请确认用户名或密码后重新登录
<a href="login.html">登录</a>
</body>
</html>
```

配置web.xml文件

```xml
	<welcome-file-list>
        <welcome-file>login.html</welcome-file>
    </welcome-file-list>
        <servlet>
                <servlet-name>login</servlet-name>
                <servlet-class>com.javaweb.test.servlet.LoginServlet</servlet-class>
            </servlet>
            <servlet-mapping>
                <servlet-name>login</servlet-name>
                <url-pattern>/login</url-pattern>
            </servlet-mapping>
```

## Cookie

在客户端上保留会话状态。

### Cookie概述

1. Cookie是什么？有什么作用？保存在哪？

   * 直译：曲奇饼干
   * 作用：在浏览器客户端上保留会话状态
   * 只要Cookie是保存在浏览器客户端上的
   * Cookie可以保存在浏览器的缓存中，浏览器关闭Cookie消失
   * Cookie也可以保存在客户端的硬盘文件中，浏览器关闭Cookie还在，除非Cookie失效

2. Cookie只在JavaWeb中有吗？

   * 不是
   * 只要是web开发，只要是B/S架构的系统，只要是基于HTTP协议，就有Cookie的存在
   * Cookie这种机制是HTTP协议规定的
   * (JavaScript也可以创建Cookie)

3. Cookie实现的功能，常见的有哪些？

   * 保留购物车商品的状态在客户端上
   * 十天内免登录
   * ...

   

### 在Java中处理Cookie

4. 在Java中如何处理Cookie？

   * 在Java中，Cookie被当作类来处理，使用new运算符可以创建Cookie对象，而且Cookie由两部分组成

     分别是Cookie的name和value，name和value都是字符串类型String。

   * 创建一个Javaweb模块，用以测试Java程序创建Cookie

     ```java
     //Java主程序的主方法
     //只有使用表单form ，并且将form标签的method属性设置为method="post"，才是POST请求方式，其余所有的请求方式都是基于GET方式。
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
             //创建Cookie对象
             Cookie cookie1 = new Cookie("username","zhangsan");
             Cookie cookie2 = new Cookie("password","123");
   
             //将Cookie对象发送给浏览器客户端
             response.addCookie(cookie1);
             response.addCookie(cookie2);
         
         }
     //HttpWatch获取结果
     HTTP/1.1 200 
     Set-Cookie: username=zhangsan
     Set-Cookie: password=123
     Content-Length: 0
     Date: Fri, 07 Aug 2020 02:17:46 GMT
     Keep-Alive: timeout=20
     Connection: keep-alive
     ```

     

5. Java程序中如何创建Cookie？

   * ```java
     Cookie cookie1 = new Cookie(String cookieName,String cookieValue);
     ```

### Cookie与浏览器路径

4. 服务器可以一次向浏览器发送多个Cookie

5. 默认情况下，服务器发送Cookie给浏览器后，浏览器将Cookie保存在缓存当中，只要不关闭浏览器，Cookie永远存在，并且有效。
   当浏览器关闭后，缓存中的Cookie被清除。

6. 在浏览器客户端无论是硬盘中还是缓存中保存的Cookie，什么时候会再次发送给服务器呢？

   * 浏览器会不会提交发送这些Cookie给服务器，和请求路径有关系
   * 请求路径和Cookie是紧密关联的
   * 不同的请求路径会发送不同的Cookie

7. 默认情况下，Cooki会和哪些路径绑定在一起？

   * 此路径请求服务器，服务器生成Cookie，并将Cookie发送给浏览器客户端
     这个浏览器中的Cookie会默认和test/这个路径绑定到一起。也就是说，以后只要发送test/请求，Cookie一定会提交给服务器。

     ```html
     /servlet_19_war_exploded/test/createAndSendCookieToBrowser
     <!--实际上路径默认绑定的是URI倒数第二个地址-->
     <!--以下路径绑定到servlet_19_war_exploded/-->
     /servlet_19_war_exploded/a
     ```

8. 其实路径是可以指定的，可以通过Java程序进行设置，保证Cookie与某个特定的路径绑定在一起

   * 如果执行了这样的语句

     ```java
     cookie1.setPath(request.getContextPath()+"/king");
     cookie2.setPath(request.getContextPath()+"/king");
     ```

   * HttpWatch将会有如下结果
     Set-Cookie: username=zhangsan; Path=/servlet_19_war_exploded/king
     Set-Cookie: password=123; Path=/servlet_19_war_exploded/king
     即Cookie与/servlet_19_war_exploded/king绑定在一起。只有发送”/servlet_19_war_exploded/king“请求路径，浏览器才会提交Cookie给服务器。

### 设置Cookie有效时长

11. 默认情况下，没有设置Cookie的有效时长，该Cookie被保存在浏览器的缓存当中，只要浏览器不关闭，Cookie一直存在，只要浏览器关闭，Cookie消失。可以设置Cookie的有效时长，以保证Cookie保存在硬盘文件当中。但这个有效时长必须大于0。有效时长过后，硬盘中的Cookie失效。
   
    * Cookie时长=0 直接被删除
    
    * Cookie时长<0 不会被存储 
    
    * Cookie时长>0 存储在硬盘文件中
    
    * ```java
      //设置Cookie有效时长
      cookie1.setMaxAge(60*60);
      cookie2.setMaxAge(60*60*24);
      ```
    
    * ```java
      //执行后的请求
      Set-Cookie: username=zhangsan; Max-Age=3600; Expires=Fri, 07-Aug-2020 08:56:27 GMT; Path=/servlet_19_war_exploded/king
      Set-Cookie: password=123; Max-Age=86400; Expires=Sat, 08-Aug-2020 07:56:27 GMT; Path=/servlet_19_war_exploded/king
      ```
    

### 服务器和浏览器间的Cookie交换

12. 浏览器提交Cookie给服务器，服务器如何接收？

    * 此处若发现取不到cookie，是因为之前设置了cookie的关联路径，将设置语句注释掉即可

      ```java
      //在生成cookie的servlet中
      //服务器将cookie对象发送给浏览器客户端
      response.addCookie(cookie1);
      response.addCookie(cookie2);
      
      ----------------------------------
      //新建一个servlet用以接收浏览器发送给服务器的cookie
      //注：此和分割线上面不是对应关系。上面是服务器传输cookie给浏览器
      //下面是服务器从浏览器处接收cookie
      public class ReceiveCookiesServlet extends HttpServlet {
          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
              //从request对象中获取Cookie
              Cookie[] cookies = request.getCookies();
      
              if(cookies != null){
                  for (Cookie cookie:cookies) {
                      String cookieName = cookie.getName();
                      String cookeValue = cookie.getValue();
                      System.out.println(cookieName+"="+cookeValue);
                  }
              }
          }
      }
      ```

### 总结

* Cookie的有效时间
   * 有效时间决定存储位置
* Cookie路径
* Cookie发送给浏览器
  * response.addcookie(cookie对象)
* 接收浏览器发送的cookie
  * request.getCookies();
* 浏览器禁用Cookie
  * 服务器发过来的cookie浏览器不接收
  * 服务器依然会发送 

### 练习：十天内免登录

1. 十天内免登录
   1. 页面元素
      1. 用户名
      2. 密码框
      3. 提交
      4. 十天免登录勾选框
   2. 逻辑
      1. 使用Servlet进行登录和免登录
         1. 一个专职用于登录，LoginServlet
            * 同时从浏览器接收数据用于创建cookie
            * 将创建的cookie发送给浏览器
         2. 另一个用于验证是否已经登录过，CheckLoginStatusServlet
            * 用于接收浏览器发来的cookie请求，并验证该cookie是否为数据库内已有用户
            * 使用到此servlet本身就代表浏览器存在cookie，即已经调用过上文的cookie
            * 根据验证结果跳转至不同的方向
              * 若无cookie或cookie数据不匹配数据库，则跳转至登录页面
              * 若cookie匹配，则跳转至登录成功页面
         3. 所以登录页面不能作为欢迎页面
         4. 以上均包含数据库验证的代码，可以单独使用一个类去完成
      2. cookie具体实现的关键
         1. 浏览器向服务器发送cookie：Cookie[] cookies = request.getCookies()
         2. 服务器向浏览器发送cookie：response.addCookie(cookie名)

## 路径总结和url-pattern的编写

### 路径的编写方式

1. 超链接

   * ```html
     <a href="/项目名/资源路径"></a>
     ```

2. form表单

   * ```html
     <form action="/项目名/资源路径"></form>
     ```

3. 重定向

   * ```java
     response.sendRedirect("/项目名/资源路径");
     ```

4. 转发

   * ```java
     request.getRequestDispatcher("/资源路径").forward(request,response);
     ```

5. 欢迎页面

   * ```xml
     <welcome-file-list>
     	<welcome-file>webapp资源名</welcome-file>
     </welcome-file-list>
     ```

6. servlet

   * ```java
     <servlet>
     	<servlet-name>标志名</servlet-name>
     	<servlet-class>类路径及类名</servlet-class>
     </servlet>
     <servlet-mapping>
     	<servlet-name>标记名</servlet-name>
     	<url-pattern>别名"/"开头</url-pattern>
     </servlet-mapping>
     ```

7. Cookie设置Path

   * ```java
     cookie.setPath("/项目名/资源路径")
     ```

8. ServletContext

   * ```java
     ServletContext application = config.getServletContext();
     application.getRealPath("资源路径");
     ```



### url-pattern的编写方式

#### url-pattern是否可以编写多个？

* 可以，有以下多种写法

  * 精确匹配

    ```xml
    <url-pattern>/hello</url-pattern>
    ```

  * 扩展匹配

    ```xml
    <!--此处以/abc开头，后面任意字符均可匹配路径-->
    <url-pattern>/abc/*</url-pattern>
    ```

  * 后缀匹配

    ```xml
    <!--此处以.action结尾，前面任意字符均可匹配路径-->
    <url-pattern>*.action</url-pattern>
    ```

  * 全部匹配

    ```xml
    <!--作用显然-->
    <url-pattern>/*</url-pattern>
    ```



## HttpSession

关于web编程中的Session

1. Session表示会话，不止在javaweb中存在，只要是web开发，都有会话这种机制；

2. 在Java中会话对应的类型是：javax.servlet.http.HttpSession，简称session/会话；

3. Cookie可以将会话状态保存在客户端，HttpSession可以将会话状态保存在服务器端；

```java
public class AccessMySelfSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        HttpSession httpSession = request.getSession();
        System.out.println(ip+" 's HttpSession = "+httpSession);
    }
}
//访问此Servlet后输出
0:0:0:0:0:0:0:1 's HttpSession = org.apache.catalina.session.StandardSessionFacade@2b897711
//不关闭浏览器重新刷新
0:0:0:0:0:0:0:1 's HttpSession = org.apache.catalina.session.StandardSessionFacade@2b897711
//关闭后刷新
0:0:0:0:0:0:0:1 's HttpSession = org.apache.catalina.session.StandardSessionFacade@7da852b3
```

### 会话/HttpSession

4. HttpSession对象是一个会话级别的对象，一次会话对应一个HttpSession对象

5. 什么是一次会话？
   * 当前学习进度的前提下，可以理解为：用户打开浏览器，在浏览器上发送多次请求，直到最后浏览器关闭，表示一次完整会话

6. 在会话进行过程中，web服务器一直为当前这个用户维护着一个对话对象/HttpSession

7. 在WEB容器中，web容器维护了大量的HTTPSession，或者说，在WEB容器中应该有一个“Session列表”

> 为什么当前会话中每一次请求可以获取到属于自己的会话对象？session的实现原理？
>
> * 打开浏览器，在浏览器上发送首次请求
> * 服务器会创建一个HttpSession对象，该对象代表一次会话
> * 同时生成HttpSession对象对应的Cookie对象，并且Cookie对象的name为JSESSIONID，Cookie的value是长度为32的字符串。
> * 服务器将Cookie的value和HttpSession对象绑定到session列表中
> * 服务器将Cookie完整发送给浏览器客户端
> * 浏览器客户端将Cookie保存到缓存中
> * 只要浏览器不关闭，Cookie不会消失
> * 当再次发送请求时，自动提交当前缓存中的Cookie
> * 服务器接收到Cookie，验证该Cookie的name确实是：JSESSIONID。然后获取该Cookie的Value
> * 通过Cookie的value去session列表中检索对应的HttpSession对象。

![](C:\Users\Jungle\Desktop\Session.png)

8. 和HttpSession对象关联的这个Cookie的name是比较特殊的。在Java中就叫做：jsessionid

9. 浏览器禁用cookie会出现什么问题？如何解决？

   * 浏览器缓存中不再保存cookie；
   * 会导致在同一个会话中，无法获取到对应的会话对象
   * 禁用Cookie之后，每一次获取的会话对象都是新的
   * 若禁用Cookie后，若仍想拿到对应的Session对象，必须使用URL重写机制
     * 如何重写URL？
       * 在URL后添加“;jsessionid=32位字符串”
       * 重写URL会给编程带来难度/复杂度，所以一般web站点不建议禁用cookie
     * 重写URL方式可以跨浏览器，甚至跨电脑访问同一个session

10. 浏览器关闭后，服务器对应的session对象会被销毁吗？为什么？

    * 服务器不会销毁session对象
    * 因为B/S架构的系统基于HTTP协议，而HTTP协议是一种无连接/无状态的协议
      * 无连接/无状态是指请求瞬间，浏览器和服务器之间的通道是打开的，请求相应结束后。通道关闭
      * 目的是降低服务器压力

11. session对象在什么时候被销毁？

    * web系统中引入了session超时的概念

    * 当很长一段时间(可指定)，没有用户再访问该session对象，此时session对象超时，web服务器自动回收session对象

    * 默认是30分钟，下为配置为两小时失效

      ```java
      <session-config>
              <session-timeout>120</session-timeout>
      </session-config>
      ```

12. 什么是一次会话？

    * 一般多数情况下，是这样描述：用户打开浏览器，在浏览器上执行一系列操作，然后将浏览器关闭，表示一次会话结束
    * 本质上讲：从session对象的创建，到最终session对象超时之后销毁，这个才是真正意义上的一次完整会话。

### 常用方法

13. 关于javax.servlet.http.HttpSession接口中的常用方法：
    * void setAttribute(String name,Object value)
      * 绑定一个Object到该session，由name作为标识符取出
    * Object getAttribute(String name)
      * 使用set方法的name作为标识符，取出set方法存入的Object
    * void removeAttribute(String name)
      * 显然的作用
    * void invalidate()
      * 销毁session
14. ServletContext、HttpSession、HttpServletRequest接口对比：
    * 以上三个都是范围对象
      * ServletContext application；是应用范围
      * HttpSession session；是会话范围
      * HttpServletRequest request；是请求范围
    * 三个范围的排序：
      * application  >  session  >  request
    * application完成跨会话共享数据、session完成跨请求共享数据，但请求必须在同一会话当中、request完成跨Servlet共享数据，但Servlet必须在同一请求中(转发)
    * 使用原则：由小到大，优先使用小范围。
      * 例如登录成功之后，保留成功状态，可以将此状态保存到session对象中。
      * 不能保存到request中，因为一次请求对应一个新的request对象。
      * 也不能保存到application范围，因为登录成功状态属于会话级别的，不能所有用户共享。
15. 补充HttpServletRequest中的方法
    * HttpSession session = request.getSession();
      * 获取当前session对象，若没有获取到session对象，则新建session对象
    * HttpSession session = request.getSession(true);
      * 获取当前session对象，若没有获取到session对象，则新建session对象
    * HttpSession session = request.getSession(false);
      * 获取当前session对象，若没有获取到session对象，则返回null

## 练习：保存登录成功状态

完成如下功能：

* 登录
* 登录成功后将此“已登录”状态保存
* 实现“显示员工列表”功能
* 当用户点击“显示员工列表”的时候，验证用户是否已登录，已登录则继续显示员工列表，否则跳转至登录页面
* 退出系统

四个servlet

* CheckLoginStatusServlet
  * 检查是否已登录
    * 验证session
  * 作为欢迎页面
    * 已登录跳转至员工列表
    * 否则跳转至登录页面
* ListEmpServlet
  * 显示员工列表
  * 员工列表页有退出登录的链接
* LoginServlet
  * 处理登录请求
  * 登录成功后设置session
* LogoutServlet
  * 处理退出请求
  * 删除session

### 练习总结

1. 数据库操作不便
   * 连接可只使用一个类来实现
   * 但对数据库操作很繁杂，如果在多处都需要数据，则在很多地方都需要连接数据库、执行SQL语句
2. 视觉表现差
   * 实际应用中不可能会如此简陋
   * 多样的页面需要多样的html标签和CSS样式，写输出语句会很繁杂
3. servlet配置信息复杂
   * 每一个功能对应一个servlet
   * 每一个servlet都需要在web.xml配置文件中编写配置信息
   * 小项目尚可，但大项目会很繁重