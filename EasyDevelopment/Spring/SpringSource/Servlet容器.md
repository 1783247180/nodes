# Servlet容器

## 背景

浏览器发给服务端的是一个HTTP格式的请求,HTTP服务器收到这个请求后,需要调用服务端程序来处理,所谓的服务端程序就是你写的service层类,一般来说不同的请求需要不同的service层类来处理.

HTTP服务器怎么知道要带哦用哪个java类的哪个方法呢?最直接的作法就是在HTTP服务器里写一堆if else逻辑判断:如果是A请求就调用X类的M1方法,如果是B请求就调用Y类的M2方法.但这样做明显由问题,因为HTTP服务器的代码跟业务逻辑耦合在一起了 

## 什么是Web服务器

Web服务器使用HTTP协议来传输数据。最简单的一种情况是，用户在浏览器（客户端，client）中输入一个URL，然后就能获取网页进行阅览。因此，服务器完成的工作就是发送网页至客户端。传输过程遵循HTTP协议，它指明了请求（request）消息和响应（response）消息的格式。

## 什么是servlet容器

 在这里，我们发现，用户/客户端只能向服务器请求静态网页。如果用户想要根据自己的输入来读取网页，这种方式就不能满足用户的要求。Servlet容器的基本思想是在服务器端使用Java来动态生成网页。因此，Servlet容器是Web服务器和servlet进行交互的必不可少的组件。

Servlet容器就是用来装Servlet的。

Servlet容器的作用：

负责处理客户请求，当客户请求来到时，Servlet容器获取请求，然后调用某个Servlet，并把Servlet的执行结果返回给客户。

## 什么是Servlet

```java
public interface Servlet {
    //你可以看到接口中还有两个跟声明周期有关的方法init和destory,Servlet容器在加载Servlet类的时候会调用init方法,在卸载的时候会调用destory方法,我们可能会在init方法里初始化一些资源,并在destory方法里释放这些资源,比如Spring MVC中的DispatcherServlet,就是在init方法里创建了自己的Spring容器.
    void init(ServletConfig config) throws ServletException;
    void destroy();
    //ServletConfig这个类,ServletConfig的作用就是封装Servlet的初始化参数,你可以在web.xml给Servlet参数,并在程序里通过getServletConfig方法拿到这些参数.
    ServletConfig getServletConfig();
    //service方法,具体业务类在这个方法里实现处理逻辑.
    void service(ServletRequest req, ServletResponse res）throws ServletException, IOException;
    String getServletInfo();
}
```

## 拓展机制

### listener

Listener是监听器,这是另一种扩展机制 ,当web应用在Servlet容器中运行时,Servlet容器内部会不断的发生各种事件,如web应用的启动和停止,用户请求到达等,Servlet容器提供了一些默认的监听器来监听这些事件,当事件发生时,Servlet容器会负责调用监听器的方法.当然,你可以定义自己的监听器去监听你感兴趣的事件,将监听器配置在web.xml中,比如Spring就实现了自己的监听器,来监听ServletContext的启动事件,目的是当Servlet容器启动时,创建并初始化全局的Spring容器.

题外话:

Servlet容器和ServletContext的关系：

ServletContext是servlet与servlet容器之间的直接通信的接口。Servlet容器在启动一个Web应用时，会为它创建一个servletContext对象。每个web应用有唯一的servletContext对象。同一个web应用的所有servlet对象共享一个serveltContext,servlet对象可以通过它来访问容器中的各种资源。 

### Filter

Filter是过滤器,这个接口允许你对请求和响应做一些统一的定制化处理,比如你可以根据请求的频率来限制访问,或者根据国家地区的不同来修改响应内容,过滤器的工作原理是这样的:Web应用部署完成后,Servlet容器需要实例化Filter并把Filter链接成一个FilterChain,当请求进来时,获取第一个Filter调用doFilter方法,doFilter方法负责调用这个FilterChain中的下一个Filter.

(https://www.cnblogs.com/jieerma666/p/10805966.html)

# Servlet3.0

@WebServlet

@WebListener

@WebFilter

# ServletContainerInitializer

Servlet3通过SPI的机制允许我们自定义一个ServletContainerInitializer的实现类，Servlet容器会在启动的时候自动调用实现类的`onStartup`方法，我们可以在该方法中进行一些Servlet对象的注册。

ServletContainerInitializer接口的定义如下：

```java
public interface ServletContainerInitializer {
    public void onStartup(Set<Class<?>> c, ServletContext ctx)
        throws ServletException; 
}
```

在下面的代码中自定义了一个ServletContainerInitializer的实现类，叫MyServletContainerInitialier。

```java
package com.atguigu.servlet;
import java.util.EnumSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import com.atguigu.service.HelloService;
//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来；
//传入感兴趣的类型；
@HandlesTypes(value={HelloService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {
	/**
	 * 应用启动的时候，会运行onStartup方法；
	 * 
	 * Set<Class<?>> arg0：感兴趣的类型的所有子类型；
	 * ServletContext arg1:代表当前Web应用的ServletContext；一个Web应用一个ServletContext；
	 * 
	 * 1）、使用ServletContext注册Web组件（Servlet、Filter、Listener）
	 * 2）、使用编码的方式，在项目启动的时候给ServletContext里面添加组件；
	 * 		必须在项目启动的时候来添加；
	 * 		1）、ServletContainerInitializer得到的ServletContext；
	 * 		2）、ServletContextListener得到的ServletContext；
	 */
	@Override
	public void onStartup(Set<Class<?>> arg0, ServletContext sc) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("感兴趣的类型：");
		for (Class<?> claz : arg0) {
			System.out.println(claz);
		}
		//注册组件  ServletRegistration  
		ServletRegistration.Dynamic servlet = sc.addServlet("userServlet", new UserServlet());
		//配置servlet的映射信息
		servlet.addMapping("/user");
		//注册Listener
		sc.addListener(UserListener.class);
		//注册Filter  FilterRegistration
		FilterRegistration.Dynamic filter = sc.addFilter("userFilter", UserFilter.class);
		//配置Filter的映射信息
		filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
	}
}
```

它是基于SPI发现的，这需要我们在classpath下面的META-INF/services下新建一个名为`javax.servlet.ServletContainerInitializer`的文件(我们在resources下建META-INF/services/javax.servlet.ServletContainerInitializer)，然后在里面加上我们自定义的ServletContainerIntializer的全路径名称。如果有多个实现类，每一个实现类写一行。

总结:

servlet容器在启动应用的时候，会扫描当前应用每一个jar包里面
META-INF/services/javax.servlet.ServletContainerInitializer
指定的实现类，启动并运行这个实现类的方法；传入感兴趣的类型；