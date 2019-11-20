## 文件上传

### Apache方式

1. 添加依赖

   ```xml
   <!--apache文件上传的依赖-->
       <!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
       <dependency>
         <groupId>commons-fileupload</groupId>
         <artifactId>commons-fileupload</artifactId>
         <version>1.3.3</version>
       </dependency>
   
   ```

   

2. 在mvc.xml中配置文件上传解析器

   ```xml
   <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
   <!--定义总最大上传大小,单位是byte 1024byte=1kb-->
           <property name="maxUploadSize" value="10240000"/>
           <!--指定上传编码-->
           <property name="defaultEncoding" value="UTF-8"/>
           <!--单个文件最大上传大小-->
           <property name="maxUploadSizePerFile" value="2000000"/>
       </bean>
   ```

3. 写一个文件上传的页面

   ```jsp
   <%--添加一个enctype="multipart/form-data"--%>
   <form action="/<%=request.getContextPath()%>/file/upload" method="post" enctype="multipart/form-data">
       文件:<input type="file" name="file"><br>
       <input type="submit" value="提交">
   </form>
   ```

4. 写一个Controller

   ```java
   package com.sz.controller;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestParam;
   import org.springframework.web.multipart.MultipartFile;
   import java.io.File;
   import java.io.IOException;
   import java.util.Date;
   @Controller
   @RequestMapping("/file")
   public class FileController {
       String uploadPath="E:"+ File.separator;
       //入参代表文件上传
       @RequestMapping("/upload")
       public String upload(@RequestParam("file")MultipartFile multipartFile, Model model){
   //1传到哪里去 2传什么东西 3传的细节
           //如果有多个name为file,可以写成@RequestParam("file")MultipartFile[] multipartFile
           if (multipartFile!=null && !multipartFile.isEmpty()){
               //不空才传
               //构建新的文件
               String originalFilename = multipartFile.getOriginalFilename();
               System.out.println(originalFilename);
               //先截取原文件的文件名前缀,不带后缀
               String fileNamePrefix=originalFilename.substring(0,originalFilename.lastIndexOf("."));
               //加工处理文件名,加上时间戳
               String newFileNamePrefix=fileNamePrefix+new Date().getTime();
               //得到新的文件名
               String newFileName=newFileNamePrefix+originalFilename.substring(originalFilename.lastIndexOf("."));
               //构建对象
   File file=new File(uploadPath+newFileName);
               //上传
               try {
                   multipartFile.transferTo(file);
                   model.addAttribute("fileName",newFileName);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           return "uploadSuc";
       }
   }
   
   ```

   

### servlet3.0的方式

在mvc.xml中配置文件上传解析器改成

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver">
</bean>
```

在web.xml中的DispatcherServlet中配置

```xml
<multipart-config>
  <max-file-size>2000000</max-file-size>
</multipart-config>
```



## 文件下载

```java
@Controller
@RequestMapping("/download")
public class DownloadController {
//定义一个文件下载的目录
    private static String parentPath="E:"+ File.separator;
    @RequestMapping("/down")
    public String down(HttpServletResponse response){
        //通过输出流写到客户端(浏览器)
        //1获取文件名
        String fileName="互联网项目.docx";
        //2构建一个文件对象通过paths工具类获取一个path对象
       Path path= Paths.get(parentPath,fileName);
//判断它是否存在
        if (Files.exists(path)){
            //存在就下载
            //通过response设定它的响应的类型
            //4获取文件的后缀
            String fileSuffix=fileName.substring(fileName.lastIndexOf(".")+1);
            //5设置contentType,只有指定它才能去下载
 response.setContentType("application/"+fileSuffix);
            //6添加头信息 默认编码是ISO8859-1 先以utf-8的形式打散成字节,再转以ISO8859-1
            try {
                response.addHeader("Content-Disposition","attachment; filename="+new String(fileName.getBytes("UTF-8"),"ISO8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //通过Path写出去
            try {
                Files.copy(path,response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "upload";
    }
}
```

## springmvc配置log4j

1. 添加依赖

   ```xml
   <dependency>
           <groupId>log4j</groupId>
           <artifactId>log4j</artifactId>
           <version>1.2.17</version>
       </dependency>
   ```

2. 在resources在添加文件log4j.properties,内容是:

   ```properties
   log4j.rootCategory=INFO, stdout
   log4j.appender.stdout=org.apache.log4j.ConsoleAppender
   log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
   log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %t %c{2}:%L - %m%n
   log4j.category.org.springframework.beans.factory=DEBUG
   ```

   

## 拦截器

springmvc提供了拦截器,类似于过滤器,可以设计多个

1. 它在我们的请求处理之前先做检查,有权决定,接下来是否继续
2. 对我们的请求进行加工

通过实现HandlerInterceptor(接口)

- 前置处理
- 后置处理
- 完成处理

### 案例一

拦截器实现方法耗时统计与警告

1. 先添加log4j

2. 定义一个interceptors的包,然后在包中添加类MethodTimerInterceptor

   ```java
   package com.sz.interceptors;
   import org.apache.log4j.Logger;
   import org.springframework.web.servlet.HandlerInterceptor;
   import org.springframework.web.servlet.ModelAndView;
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;
   //方法耗时统计的拦截器
   public class MethodTimerInterceptor implements HandlerInterceptor {
       private static final Logger LOGGER=Logger.getLogger(MethodTimerInterceptor.class);
       //前置处理
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           //1定义开始时间
           long start=System.currentTimeMillis();
           //将其存在请求域中
           request.setAttribute("start",start);
           //返回true,才会找下一个拦截器 如果没有下一个拦截器,则去Controller
           //记录请求日志
           LOGGER.info(request.getRequestURI()+",请求到达");
           return true;
       }
   //后置处理
       @Override
       public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
   //取出start
           long start=(long)request.getAttribute("start");
           //得到end
           long end=System.currentTimeMillis();
           //记录一下耗时
           long spendTime=end-start;
           if (spendTime>=1000){
               LOGGER.warn("方法耗时严重"+spendTime+"毫秒");
           }else {
               LOGGER.info("方法耗时"+spendTime+"毫秒");
           }
       }
   //完成处理
       @Override
       public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
   
       }
   }
   
   ```

   3. 在mvc.xml中添加

      ```xml
      <mvc:interceptors>
              <mvc:interceptor>
                  <!--/*的写法只能拦截/name的方法,只能由一层,不是多层拦截-->
                  <mvc:mapping path="/**/*"/>
                  <bean class="com.sz.interceptors.MethodTimerInterceptor">
                  </bean>
              </mvc:interceptor>
      </mvc:interceptors>
      ```

   ### 案例二

   检查Session是否存在,登录样例


### 拦截器执行顺序

按照注册顺序执行

注册顺序:i1 i2 i3

前置处理i1 i2 i3

后置处理i3 i2 i1

## 其他知识点

```java
@Controller
@RequestMapping(value="/goods")
public class NewController {
    @RequestMapping(value="/good",produces="application/json;charset=utf-8")
    @ResponseBody
    public String good(){
        return "登录成功";
    }
}
```

