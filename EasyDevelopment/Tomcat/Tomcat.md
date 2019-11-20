# main项目与web项目

main方法是项目的入口,通过main方法启动项目,而web项目是没有main方法,如何让web项目启动起来,这时候就需要tomcat了,tomcat是一个servlet容器,处理http请求,把开发好的类打包成war包,然后放在tomcat的webapps下面,tomcat会自己解压war包,并且去运行程序

# 手撕Tomcat

## 原理

http协议实际是使用的TCP协议,底层是socket实现的



## 基本思路

### 背景

1. tomcat--中间件
2. web无main方法
3. java--反射实现类动态加载
4. web项目是通过http协议 tcp--socket

### 步骤

tomcat需要main方法

tomcat需要监听本机上的某个端口

tomcat需要抓取此端口上来自客户端连接并且获取请求调用的方法与参数

tomcat需要根据请求调用方法,动态加载方法所在的类,完成类的实例化并通过该实例获取方法最终将请求传入方法

将结果返回给客户端(jsp/json/html/xml)

1. 提供服务类Server类

2. 处理请求信息Handler类

3. 封装Request Response

4. 实现Servlet

   HttpServlet抽象类，HttpServlet的实现类，web.xml文件的Servlet类，web.xml文件的ServletMapping类

5. 分发请求 doGet或doPost或404或500

6. 返回响应结果  response.write()

```java
package com.yy.tomcat;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server {
    private static ServerSocket serverSocket;
    //JAVA中对线程池定义的一个接口
    private static ExecutorService executorService;
    //线程池最大连接数
    private final static int POOL_SIZE=15;
    private static int port=8090;
    public static void start(){
    try {
        serverSocket=new ServerSocket(port);
        Socket socket;
        System.out.println("starting"+port);
        //通过看源码确定newFixedThreadPool方法返回的类ThreadPoolExecutor(实现了ExecutorService接口)
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
        while(true){
            socket = serverSocket.accept();
            //放进execute的线程会自动执行start方法
            executorService.execute(new Handler(socket));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static void main(String[] args) {
        start();
    }
}

```

```java
package com.yy.tomcat;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class Handler implements Runnable {
    private Socket socket;
    public Handler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        //处理请求信息
        PrintWriter pw= null;
        try {
            pw = new PrintWriter(socket.getOutputStream());
            //http请求固定格式
            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: text/html;charset=UTF-8");
            pw.println();
            //用于接收浏览器的请求
            Request request = new Request();
            //用于返回信息到浏览器
            Response response=new Response(pw);
            //读取请求信息
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (true){
                //浏览器发送的请求第一行是GET /index?name=zeb&pwd=pwd HTTP/1.1
                String msg=reader.readLine();
                //String.Trim()方法会去除字符串两端，不仅仅是空格字符，它总共能去除25种字符:
                //Trim删除的过程为从外到内，直到碰到一个非空白的字符为止，所以不管前后有多少个连续的空白字符都会被删除掉。
                if(null == msg || "".equals(msg.trim())){
                    break;
                }
                String[] msgs=msg.split(" ");
                //msg
                if(3 == msgs.length && "HTTP/1.1".equalsIgnoreCase(msgs[2])){
                    //get or post
                    request.setMethod(msgs[0]);
                    //获取参数name与pwd
                    //   /index?name=zhangsan&pwd=123456
                    //split方法工作原理是利用正则表达式,而在正则表达式中, "?"有特殊意思
                    //所以匹配"?"时要用转义字符"\",所以在正则表达式中匹配"?"的表达式是"\?", 而在Java中,\又是特殊字符, 所以还要进行转义, 所以最终变成"\\?"
                    //正则中匹配前面的子表达式零次或一次。
                    String[] attributesPath=msgs[1].split("\\?");
                    // /index
                    request.setPath(attributesPath[0]);
                    if(attributesPath.length>1) {
                        Map<String,String> attributeMap=new HashMap<>();
                        String[] params=attributesPath[1].split("&");// name=zhangsan&pwd=123456
                        for(int i=0;i<params.length;i++) {
                            attributeMap.put(params[i].split("=")[0], params[i].split("=")[1]);// name,zhangsan
                        }
                        //放入request的map里面,map的key就是name和pwd value就是zhangsan和123456
                        request.setMap(attributeMap);
                    }
                    break;
                }
            }
        //如果是图标请求,直接返回    
if(request.getPath().endsWith("ico")){
    return;
}
//加载servlet
            HttpServlet httpServlet = request.initServlet();
//请求分发
            dispatcher(httpServlet,request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            pw.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void dispatcher(HttpServlet httpServlet, Request request, Response response) {
        try {
            if (null == httpServlet){
            response.write("<h1>404</h1>");
        }else{
                if ("GET".equalsIgnoreCase(request.getMethod())){
                    httpServlet.doGet(request,response);
                }else if("POST".equalsIgnoreCase(request.getMethod())){
                    httpServlet.doPost(request,response);
                }
            }
    }catch (Exception e) {
            response.write("<h1>500</h1>"+ Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }
}
```

```java
package com.yy.tomcat;
import java.util.Map;
//模拟HttpServletRequest
public class Request {
    private String path;
    private String method;
    private Map<String,String> map;
    //省去了get和set方法
public HttpServlet  initServlet(){
    return ServletContainer.getHttpServlet(path);
}
    public String getParameter(String name)
    {
        String parameter=map.get(name);
        return parameter;
    }
}
```

```java
package com.yy.tomcat;
import java.io.PrintWriter;
//模拟HttpServletResponse
public class Response {
    private PrintWriter writer;
    public Response(PrintWriter writer) {
        this.writer = writer;
    }
    public void write(String msg){
        writer.write(msg);
        writer.flush();
    }
}

```

```java
package com.yy.tomcat;
import java.io.IOException;
//模拟HttpServlet,自己的写的Servlet类都要继承这个抽象类
public abstract class HttpServlet {
    public void doGet(Request request,Response response) throws IOException {
this.service(request,response);
    }
    public void doPost(Request request,Response response) throws IOException {
        this.service(request,response);
    }
    public void service(Request request,Response response) throws IOException {
        if("GET".equalsIgnoreCase(request.getMethod())){
doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
```

```java
package com.yy.tomcat;
import java.io.IOException;
//自己写的servlet
public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws IOException {
response.write("<h1>Servlet GET response!</h1>"+
        "name: "+request.getParameter("name")+" , pwd: "+request.getParameter("pwd"));
    }
    @Override
    public void doPost(Request request, Response response) throws IOException {
response.write("<h1>Servlet POST response!</h1>"+
        "name: "+request.getParameter("name")+" , pwd: "+request.getParameter("pwd"));
    }
}
```

```java
package com.yy.tomcat.model;
//解析web.xml用到的实体
public class Servlet {
private String name;
private String clazz;
//省去了set和get方法,本来是class,因为class是关键字用clazz代替
}
```

```java
package com.yy.tomcat.model;
//解析web.xml用到的实体
public class ServletMapping {
    private String name;
    private String url;
}
```

```java
package com.yy.tomcat.util;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.yy.tomcat.model.Servlet;
import com.yy.tomcat.model.ServletMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//解析web.xml 了解即可
public class XMLUtil {
	public static Map<Integer,Map<String,Object>> parseWebXML() throws Exception{
		Map<Integer,Map<String,Object>> result=new HashMap<Integer,Map<String,Object>>();
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db=dbf.newDocumentBuilder();		
		
		InputStream in=XMLUtil.class.getClassLoader().getResourceAsStream("web.xml");
		Document document=db.parse(in);
		Element root=document.getDocumentElement();
		System.out.println("rootName: "+root.getTagName());//web-app
		
		NodeList xmlNodes=root.getChildNodes();		
		for(int i=0;i<xmlNodes.getLength();i++) {
			Node config=xmlNodes.item(i);			
			if(null!=config && config.getNodeType()== Node.ELEMENT_NODE) {
				String nodeName1=config.getNodeName();
				System.out.println("nodeName1: "+nodeName1);//servlet servlet-mapping				
				if("servlet".equals(nodeName1)) {
					Map<String,Object> servletMaps=null;
					if(result.containsKey(0)) {
						servletMaps=result.get(0);
					}else {
						servletMaps=new HashMap<String,Object>();
					}					
					NodeList childNodes=config.getChildNodes();
					Servlet servlet=new Servlet();
					for(int j=0;j<childNodes.getLength();j++) {						
						Node node =childNodes.item(j);						
						if(null!=node && node.getNodeType()== Node.ELEMENT_NODE) {//	???????????					
							String nodeName2=node.getNodeName();
							System.out.println("nodeName2: "+nodeName2);//servlet-name servlet-class
							String textContext=node.getTextContent();
							System.out.println("textContext: "+textContext);
							if("servlet-name".equals(nodeName2)) {
								servlet.setName(textContext);
							}else if("servlet-class".equals(nodeName2)) {
								servlet.setClazz(textContext);
							}
						}
					}
					servletMaps.put(servlet.getName(), servlet);
					result.put(0, servletMaps);					
					
				}else if("servlet-mapping".equals(nodeName1)) {
					Map<String,Object> servletMappingMaps=null;
					if(result.containsKey(1)) {
						servletMappingMaps=result.get(1);
					}else {
						servletMappingMaps=new HashMap<String,Object>();
					}
					
					NodeList childNodes=config.getChildNodes();
					ServletMapping servletMapping=new ServletMapping();
					
					for(int j=0;j<childNodes.getLength();j++) {
						Node node =childNodes.item(j);
						if(null!=node && node.getNodeType()==Node.ELEMENT_NODE) {
							String nodeName2=node.getNodeName();
							System.out.println("nodeName2: "+nodeName2);//servlet-name url-pattern
							String textContext=node.getTextContent();
							System.out.println("textContext: "+textContext);
							if("servlet-name".equals(nodeName2)) {
								servletMapping.setName(textContext);
							}else if("url-pattern".equals(nodeName2)) {
								servletMapping.setUrl(textContext);
							}
						}
					}
					servletMappingMaps.put(servletMapping.getUrl(), servletMapping);
					result.put(1, servletMappingMaps);
				}
			}
		}
		return result;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(parseWebXML());
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    <!--注册一个前端控制器,dispatchServlet-->
    <servlet>
        <servlet-name>myServlet</servlet-name>
        <servlet-class>com.yy.tomcat.MyServlet</servlet-class>
    </servlet>
    <!--servlet映射配置-->
    <servlet-mapping>
        <servlet-name>myServlet</servlet-name>
        <!--先统一写斜杠-->
        <url-pattern>/index</url-pattern>
    </servlet-mapping>
</web-app>
```

```java
package com.yy.tomcat;
import com.yy.tomcat.model.Servlet;
import com.yy.tomcat.model.ServletMapping;
import com.yy.tomcat.util.XMLUtil;
import java.util.HashMap;
import java.util.Map;
//Servlet容器,解析web.xml根据url信息拿到响应的servlet
public class ServletContainer {
    //3个servlet容器
    private static Map<String,Object> servletMaps=new HashMap<>();
    private static Map<String,Object> servletMappingMaps=new HashMap<>();
    private static Map<String,HttpServlet> servletContainer=new HashMap<>();
    //通过静态块去加载配置文件中映射对应的model实体
    static{
        try {
            Map<Integer, Map<String, Object>> maps = XMLUtil.parseWebXML();
            if(null != maps&&2 ==maps.size()){
                servletMaps=maps.get(0);
                servletMappingMaps=maps.get(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//通过工具类加载,从servlet容器中获取对应的servlet
    public static HttpServlet getHttpServlet(String path){
//访问跟路径/时修改为index路径
        if(null == path ||"".equals(path.trim()) || "/".equals(path)){
path="/index";
        }
        //如果servletContainer容器中有就直接取走
        if (servletContainer.containsKey(path)){
            return servletContainer.get(path);
        }
        if (!servletMappingMaps.containsKey(path)){
            return null;
        }
        ServletMapping servletMapping= (ServletMapping) servletMappingMaps.get(path);
        String name=servletMapping.getName();
        if(!servletMaps.containsKey(name)) {
            return null;
        }
        Servlet servlet=(Servlet) servletMaps.get(name);
        String clazz=servlet.getClazz();
        if(null==clazz || "".equals(clazz.trim())) {
            return null;
        }
        HttpServlet httpServlet=null;
        try {
            httpServlet=(HttpServlet) Class.forName(clazz).newInstance();//通过反射获取servlet实体
            servletContainer.put(path, httpServlet);//添加到servlet容器中
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpServlet;
    }
}
```



需要的对象 



![1569568453231](F:\Typora\EasyDevelopment\Tomcat\img\1569568453231.png)

