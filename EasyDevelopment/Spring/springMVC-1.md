## 入门体验

pom.xml

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>5.0.8.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
```

1. 创建web项目

   ![1549812967603](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1549812967603.png)

2. 编写web.xml，在其中注册一个servlet，前端控制器。

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
            version="3.1">
       <!--注册一个前端控制器,dispatchServlet-->
     <servlet>
       <servlet-name>springmvc</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
     </servlet>
       <!--servlet映射配置-->
     <servlet-mapping>
       <servlet-name>springmvc</servlet-name>
         <!--先统一写斜杠-->
       <url-pattern>/</url-pattern>
     </servlet-mapping>
   </web-app>
   ```

   组件分析

   注册前端控制器,目的在于,我们希望springmvc处理所有的请求

   通过

   ```xml
   <servlet-mapping>
       <servlet-name>springmvc</servlet-name>
         <!--先统一写斜杠-->
       <url-pattern>/</url-pattern>
     </servlet-mapping>
   ```

   URLPattern写法

   - /处理所有请求
   - /*(永远不要这么写)
   - *.do(do结尾的请求 eg:http://localhost:8080/helloController.do)

3. 编写一个springmvc的配置文件，注册一个视图解析器

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   <!--配置一个视图解析器
   常用内部资源视图解析器
   -->
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
           <!--前缀-->
           <property name="prefix" value="/jsp/" />
           <!--后缀-->
           <property name="suffix" value=".jsp" />
       </bean>
       <bean class="com.sz.controller.HelloController" name="/helloController">
       </bean>
   </beans>
   ```

4. 编写一个控制器

   ```java
   package com.sz.controller;
   import org.springframework.web.servlet.ModelAndView;
   import org.springframework.web.servlet.mvc.Controller;
   //实现一个controller接口的方式
   public class HelloController implements Controller {
       @Override
       public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) {
           ModelAndView mav=new ModelAndView();
           mav.addObject("girl","ff");
           mav.setViewName("girl");
           return mav;
       }
   }
   ```

5. 编写一个结果页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
my girl:${girl}
</body>
</html>
```

6. 配置Tomcat

   ![1549813940435](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1549813940435.png)

![1549813985144](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1549813985144.png)

## 前端控制器dispatchServlet

控制视图页面的选择

## springmvc配置文件的文件名问题

默认情况下是用web.xml里面配置的dispatchServlet的名字作为servletName

[servletName]-servlet.xml(WEB-INF)之下寻找

[servletName]-servlet.xml(WEB-INF)之下寻找

[servletName]-servlet=namespace

如果非要使用另外一个名字

![1549852274194](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1549852274194.png)

默认规则是在WEB-INF下,但是maven默认资源路径是resources,如何解决这个问题?

```xml
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:mvc.xml</param-value>
    </init-param>
  </servlet>
```

在类路径下寻找mvc.xml,推荐使用这种

## 视图解析器

springmvc支持多种视图技术

- jsp
- freemaker

内部的资源视图解析器

视图前缀

+ /jsp/ 它是我们的请求响应的资源的路径的配置  viewName: girl

视图后缀

+ .jsp 此时我们的前缀+视图名称+后缀=/jsp/girl.jsp

逻辑视图

View=prefix+logicViewName+suffix;

## 控制器

1. 实现接口的方式完成

   ```java
   //实现一个controller接口的方式
   public class HelloController implements Controller {
       @Override
       public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) {
           ModelAndView mav=new ModelAndView();
           mav.addObject("girl","ff");
           mav.setViewName("girl");
           return mav;
       }
   }
   ```

设计为ModelAndView

在model中填充数据,然后在具体的视图进行展示

还需要在配置文件中配置一下这个bean,要取个名字,就用来充当这个请求URI

2. 基于注解

   - @Controller
   - @RequestMapping

   开发步骤

   1. 记得配置一下基础扫描的包,这样配置的注解才会生效
   2. 在指定类上面添加@Controller
   3. 添加@RequestMapping 类似于前面的controller的那个名字

当我们写上@Controller之后改类就被标记为spring的一个组件,并且是控制器的组件,此时我们的handlemapping会去扫描寻找这个controller是否与之匹配,如果匹配就把工作交给它

匹配的规则是什么?

通过请求的路径来匹配

@RequestMapping(URI)

此时就通过这个URI匹配



@RequestMapping

可以写在方法上

或类上(推荐使用二者结合)

```java
package com.sz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//访问的路径是http://localhost:8080/goods/good
@Controller
@RequestMapping("/goods")
public class NewController {
    @RequestMapping("/good")
    public String good(Model model){
        model.addAttribute("girl","qwer");
        //这里return就是那个viewName
        //此时是jsp/girl.jsp
        return "girl";
    }
}
```

## 转发与重定向

- 转发到页面 默认
- 重定向到页面 redirect:path
- 转发到另外一个控制器:forward:path

重定向:

```java
@Controller
@RequestMapping("/goods")
public class NewController {
    @RequestMapping("/redirect")
    public String redirect(Model model){
        model.addAttribute("girl","redirect");
        //如果是重定向就与视图解析规则无关,而且model.addAttribute("girl","redirect");的数据也传递不过来
        return "redirect:/jsp/girl.jsp";
    }
}
```

访问路径:http://localhost:8080/goods/redirect会变成http://localhost:8080/s/jsp/girl.jsp?girl=redirect

重要补充:重定向之后session里面的数据不会丢失,重定向是两次请求一次回话,请求域数据丢失会话域数据保存

比session范围大的对象里面的数据也不会丢失 eg:application

## 关于springmvc访问web元素

- request
- session  HttpSession
- application 

可以通过模拟的对象完成操作,也可以使用原生的servletAPI,直接在方法里面入参即可完成

```java
//模拟请求,推荐使用
@RequestMapping("/request")
public String request(WebRequest request){
    System.out.println(request.getParameter("x"));
    return "girl";
}
//真正的request
@RequestMapping("/request2")
public String request2(HttpServletRequest request){
    System.out.println(request.getParameter("x"));
    return "girl";
}
```

## 注解详细介绍

@RequestMapping

- value写的是路径,是一个数组的形式,可以匹配多个路径 eg:@RequestMapping(value={"/m1","/m2"})
- path value的别名,二者任选其一
- method 限定可以访问的请求的类型   eg:method=RequestMethod.POST
- params 可以去指定参数,你还可以去限定这个参数的特征,必须等于某个值,不等于某个值 eg:params = {"girl","boy"}  params = {"girl=wangfei","boy!=gg"}  
- headers 能够影响浏览器的行为
- consumers 消费者 媒体类型 可以限定为application/json;charset=UTF-8
- produces 产生的响应的类型

@GetMapping

相对于@RequestMapping里面method指定为RequestMethod.GET

@PostMapping

相对于@RequestMapping里面method指定为RequestMethod.POST

@RestController

@RestController=@Controller+@Responsebody

@PathVariable

restful风格编程

![1549960914176](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1549960914176.png)

@Requestbody

处理Ajax请求

@Responsebody

返回数据的,一般情况下返回JSON格式

eg:

```java
@RequestMapping("/pa")
@ResponseBody
public String demo(){
    //没有ok.jsp,就是显示数据ok
    return "ok";
}
```

![1550057201391](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550057201391.png)

@ModelAttribute

方法上面:

加上这个注解,在controller里面的任意一个处理具体的方法之前都会执行

![1550060834856](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550060834856.png)

结果:false true

参数列表上:

![1550063257062](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550063257062.png)

当路径为pa的请求是form表单时:

如果有user字段,该sys字段里面的user属性就是页面传过来的

如果没有user字段,该sys字段里面的user属性就是上方设置的"user"

这样如果返回的是一个页面,那么在页面上可以直接用systems.user来获取

@SessionAttributes

这个用在类上面,它会将模型自动填充到会话里面去

@SessionAttribute

要求当前这次访问当中的会话里面必须要有某个对象

@CookieValue

## @RequestMapping等注解请求路径的问题

springmvc支持ant风格

- ?任意的一个字符,斜杠除外 eg:@RequestMapping(value={"/m1?"})
- *任意个字符,斜杠除外
- /** 支持任意层路径  eg:@RequestMapping(value={"/m3/**})

## 静态资源访问问题

springmvc默认拦截静态资源

由于我们的servlet设置了URL匹配方式为/(在文件mvc.xml里面设置的)所以,它将静态资源(css,js等)也当做一个后台的请求

比如http://localhost:8080/s/static/css/index.css                   

它尝试去匹配一个static/css/index.css里面的Controller里面的RequestMapping的组合,因为没有所以404

处理方法1

在mvc.xml中加入:

```xml
<!--默认的servlet处理者,不会处理静态资源-->
<!--在springMVC-servlet.xml中配置<mvc:default-servlet-handler />后，会在Spring MVC上下文中定义一个org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler，它会像一个检查员，对进入DispatcherServlet的URL进行筛查，如果发现是静态资源的请求，就将该请求转由Web应用服务器默认的Servlet处理，如果不是静态资源的请求，才由DispatcherServlet继续处理。但是如何判断是不是静态资源请求呢,需要用到<mvc:annotation-driven />-->
<mvc:default-servlet-handler />
```

处理方法2

在mvc.xml中加入

```xml
<mvc:resources mapping="/static/css/*" location="static/css/" />
```

注意上面两种可以加载静态资源但是再也无法访问到controller

还需在mvc.xml中加上

```xml
<!--如果在web.xml中servlet-mapping的url-pattern设置的是/，而不是如.do。表示将所有的文件，包含静态资源文件都交给spring mvc处理。就需要用到<mvc:annotation-driven />了。如果不加，DispatcherServlet则无法区分请求是静态资源文件还是mvc的注解，而导致controller的请求报404错误。-->
<!--当配置了mvc:annotation-driven/后，Spring就知道了我们启用注解驱动。然后Spring通过context:component-scan/标签的配置，会自动为我们将扫描到的@Component，@Controller，@Service，@Repository等注解标记的组件注册到工厂中，来处理我们的请求。-->
<mvc:annotation-driven />
```

## 关于POST请求中文乱码问题

添加一个过滤器即可,springmvc提供了非常好的字符编码过滤器,所以我们注册即可

web.xml中:

```xml
<filter>
  <filter-name>characterEncodingFilter</filter-name>
  <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  <init-param>
    <param-name>encoding</param-name>
    <param-value>UTF-8</param-value>
  </init-param>
    <init-param>
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
  <filter-name>characterEncodingFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 关于form表单提交数据的方式

### 方式一通过属性名字绑定

通过属性名称进行绑定,可以完成数据传递

页面当中表单元素的name值和后台的形参的名字保持一致

eg:

![1550059196418](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550059196418.png)

![1550059144160](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550059144160.png)

### 方式二利用@RequestParam

```java
@RequestMapping("/good")
public String good(Model model,@RequestParam("user") String user){
    model.addAttribute("girl",user);
    return "girl";
}
```

### 方式三直接使用pojo形式传递

```java
@Controller
@RequestMapping("/goods")
public class NewController {
    @RequestMapping("/good")
    public String good(Model model, Systemss sys){
        model.addAttribute("girl",sys.getUser());
        return "girl";
    }
    }
```

```java
public class Systemss {
    private String user;
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}
```

## 关于form表单提交日期格式数据问题的处理

在属性上添加额外的注解

```java
//符合yyyy-MM-dd HH:mm:ss格式的都可以
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private Date birth;
```

## JSON数据交互

![1550126523338](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1550126523338.png)

### 额外依赖

```xml
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.3</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.9.3</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.9.3</version>
</dependency>

<!-- https://mvnrepository.com/artifact/net.sf.json-lib/json-lib -->
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
    <version>2.4</version>
</dependency>
<!--添加处理json为javabean-->
<!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-core-asl -->
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-core-asl</artifactId>
    <version>1.9.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl -->
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.2</version>
</dependency>
```

## Ajax特别注意:

Ajax的dataType是从后台返回的数据的数据类型,如果指定了datatype:"json",那么后台传来的数据是String不可以到达success方法,去掉datatype:"json",String可以到达

传过去是json:data:JSON.stringify(obj),

传过去是普通值:data: "username=" + name,

### 后台pojo->json

1. 返回pojo

```java
@RequestMapping("/first")
    @ResponseBody
    public User m1(){
        User user = new User();
        user.setName("名字");
        user.setPassword("121");
        return user;
    }
```

2. 返回Map

```java
@RequestMapping("/second")
@ResponseBody
public Map<String,Object> m2(){
    Map<String, Object> object = new HashMap<>();
    object.put("name","信息");
    object.put("password",123);
    return object;
}
```

3. 返回数组

   ```java
   @RequestMapping("/third")
   //@ResponseBody
   public User[] m3(){
       User user = new User();
       user.setName("名字");
       user.setPassword("121");
       User u = new User();
       u.setName("名字");
       u.setPassword("121");
       return new User[]{user,u};
   }
   ```

4. 返回List

```java
@RequestMapping("/fourth")
//@ResponseBody
public List<User> m4(){
    List<User> list=new ArrayList<>();
    User user = new User();
    user.setName("名字");
    user.setPassword("121");
    User u = new User();
    u.setName("名字");
    u.setPassword("121");
    list.add(u);
    list.add(user);
    return list;
}
```

### 前台json->pojo

```js
$(function () {
        $("#btn1").click(function () {
                    $.ajax({
                        type: "POST",
                        url: "<%=request.getContextPath() %>/json/first",
                        success: function(msg){
                            console.log("congratulation"+msg.name);
                        }
                    });
            }
        )
    }
)
```

### 后台json->pojo

1. 一个pojo

```java
@RequestMapping("/fifth")
    //一定要记得添加@RequestBody
    public User m5(@RequestBody User user){
        System.out.println(user.getName()+user.getPassword());
        return user;
    }
```

Ajax传输json数据的代码

```js
$("#btn2").click(function () {
       var x="命名"
        var obj={
            "name":"叶问",
            "password":x
        };
            $.ajax({
                type: "POST",
                url: "<%=request.getContextPath() %>/json/fifth",
                data:JSON.stringify(obj),
                contentType:"application/json",
                success: function(msg){
                    console.log("congratulation"+msg);
                }
            });
        }
        )
```

layui模板中使用Ajax:

```js
layui.use(['element','jquery'], function(){
        var element = layui.element;
var jquery=layui.jquery;//or var jquery=$=layui.jquery;
    $(
    function () {
    $("#btn2").click(function () {
        var x="命名";
        var obj={
            "name":"叶问",
            "password":x
        };
            $.ajax({
                type: "POST",
                url: "<%=request.getContextPath() %>/json/fifth",
                data:JSON.stringify(obj),
                contentType:"application/json",
                success: function(msg){
                    console.log("congratulation"+msg.name);
                }
            });
        }
        );
        $("#btn1").click(function () {
                $.ajax({
                    type: "POST",
                    url: "<%=request.getContextPath() %>/json/first",
                    success: function(msg){
                        console.log("congratulation"+msg.name);
                    }
                });
            }
        )
    }
)
    });
```

2. 多个pojo

```js
$("#btn2").click(function () {
       var x="命名";
        var obj={
            "name":"叶问",
            "password":x
        };
    var obj2={
            "name":x,
            "password":"密码"
        };
    var arr=new Array();
    arr.push(obj);
    arr.push(obj2);
            $.ajax({
                type: "POST",
                url: "<%=request.getContextPath() %>/json/fifth",
                data:JSON.stringify(arr),
                contentType:"application/json",
                success: function(msg){
                    console.log("congratulation"+msg);
                }
            });
        }
        )
```



## form提交数据与Ajax提交数据的区别

form提交的数据的contentType:application/x-www-form-urlencoded

Ajax提交的数据的contentType:application/json

