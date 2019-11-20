## SSM整合

1. 添加依赖

   修改pom.xml:

   ```xml
   <properties>
       <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
       <maven.compiler.source>1.8</maven.compiler.source>
       <maven.compiler.target>1.8</maven.compiler.target>
         <spring.version>5.0.8.RELEASE</spring.version>
     </properties>
   ```

​        pom.xml中添加:

```xml
<!--spring依赖start================-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context-support</artifactId>
      <version>${spring.version}</version>
    </dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-beans</artifactId>
  <version>${spring.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-web</artifactId>
  <version>${spring.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>${spring.version}</version>
</dependency>

<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aop</artifactId>
  <version>${spring.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aspects</artifactId>
  <version>${spring.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>${spring.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-tx</artifactId>
  <version>${spring.version}</version>
</dependency>
<!--spring依赖end================-->
<!--json -->
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.3</version>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.9.3</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-annotations</artifactId>
  <version>2.9.3</version>
</dependency>

<dependency>
  <groupId>net.sf.json-lib</groupId>
  <artifactId>json-lib</artifactId>
  <version>2.4</version>
  <classifier>jdk15</classifier>
</dependency>
<!--新添加处理json为javabean -->
<dependency>
  <groupId>org.codehaus.jackson</groupId>
  <artifactId>jackson-core-asl</artifactId>
  <version>1.9.2</version>
</dependency>
<dependency>
  <groupId>org.codehaus.jackson</groupId>
  <artifactId>jackson-mapper-asl</artifactId>
  <version>1.9.2</version>
</dependency>
<!--新添加处理json为javabean ==end=== -->
<!-- 文件上传依赖start============ -->
<dependency>
  <groupId>commons-fileupload</groupId>
  <artifactId>commons-fileupload</artifactId>
  <version>1.3.1</version>
</dependency>
<!-- 文件上传依赖end============ -->
<!-- 持久层依赖 -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.4.1</version>
</dependency>
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>5.1.38</version>
</dependency>
<!-- 日志依赖 start============ -->
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.7.12</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.7.12</version>
</dependency>
<dependency>
  <groupId>log4j</groupId>
  <artifactId>log4j</artifactId>
  <version>1.2.17</version>
</dependency>
<!-- 日志依赖 end============ -->
<!-- 数据源的引入 -->
<dependency>
  <groupId>com.mchange</groupId>
  <artifactId>c3p0</artifactId>
  <version>0.9.2.1</version>
</dependency>
<!-- mybatis与spring整合所需要的依赖 -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>1.3.0</version>
</dependency>
<!-- servlet jsp jstL等依赖start================ -->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.servlet.jsp</groupId>
  <artifactId>javax.servlet.jsp-api</artifactId>
  <version>2.3.1</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>jstl</artifactId>
  <version>1.2</version>
</dependency>
<!-- servlet jsp jstL 等依赖end ================ -->
<!-- 处理时间日期格式 -->
<dependency>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <version>2.9.9</version>
</dependency>
<!-- mybatis分页依赖start============= -->
<dependency>
  <groupId>com.github.pagehelper</groupId>
  <artifactId>pagehelper</artifactId>
  <version>5.1.2</version>
</dependency>
<!-- mybatis分页依赖end============= -->
<!--apache用于MD5加密 -->
<dependency>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.10</version>
</dependency>
```

1. web.xml的配置

   注册spring的上下文文件

   ```xml
   <!-- web.xml中以下配置的加载顺序：ServletContext >> context-param >> listener >> filter >> 
           servlet >> spring -->
   <context-param>
           <!--指定上下文位置,spring里面声明对象都需要通过上下文对象获取-->
           <param-name>contextConfigLocation</param-name>
           <param-value>
               classpath:spring/applicationContext.xml
           </param-value>
   </context-param>
   ```

   字符编码以及全HTTP请求支持过滤器添加

   ```xml
   <!--字符编码设置-->
   <filter>
       <filter-name>encodingFilter</filter-name>
       <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
       <init-param>
           <param-name>encoding</param-name>
           <param-value>UTF-8</param-value>
       </init-param>
       <init-param>
           <param-name>forceEncoding</param-name>
           <param-value>true</param-value>
       </init-param>
   </filter>
   <filter-mapping>
       <filter-name>encodingFilter</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
   <!--html中form表单只支持GET与POST请求，而DELETE、PUT等method并不支持，spring3.0添加了一个过滤器，可以将这些请求转换为标准的http方法，使得支持GET、POST、PUT与DELETE请求。-->
   <filter>
       <filter-name>hiddenHttpMethodFilter</filter-name>
       <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
   </filter>
   <filter-mapping>
       <filter-name>hiddenHttpMethodFilter</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
   ```

   DispatcherServlet注册

   ```xml
   <!--注册DispatcherServlet-->
   <servlet>
       <servlet-name>springMVC</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <!--指定SpringMVC的核心配置文件-->
       <init-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath:spring/spring-servlet.xml</param-value>
       </init-param>
       <load-on-startup>1</load-on-startup>
   </servlet>
   <servlet-mapping>
       <servlet-name>springMVC</servlet-name>
       <url-pattern>/</url-pattern>
   </servlet-mapping>
   ```

   spring启动监听器配置

   ```xml
   <!--启动Web容器时，读取在contextConfigLocation中定义的xml文件，自动装配-->
   <listener>
       <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
   </listener>
   ```

   ## spring核心配置文件编写

   核心配置文件用于引入其它配置文件。

   applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
       <!--    引入spring和其它整合的配置文件 -->
       <import resource="classpath:spring/spring-*.xml"/>
   </beans>
   ```

   spring-servlet.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xmlns:c="http://www.springframework.org/schema/c"
          xmlns:cache="http://www.springframework.org/schema/cache"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:jdbc="http://www.springframework.org/schema/jdbc"
          xmlns:jee="http://www.springframework.org/schema/jee"
          xmlns:lang="http://www.springframework.org/schema/lang"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:p="http://www.springframework.org/schema/p"
          xmlns:task="http://www.springframework.org/schema/task"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xmlns:util="http://www.springframework.org/schema/util"
          xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd
           http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
           http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
           http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd
           http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd ">
       <!-- 启动注解 -->
   <context:component-scan base-package="com.sz">
       <!--排除service注解,专注于Controller-->
       <context:exclude-filter type="annotation"
                               expression="org.springframework.stereotype.Service" />
   </context:component-scan>
   <!-- 配置一个视图解析器 -->
   <bean
           class="org.springframework.web.servlet.view.InternalResourceViewResolver">
       <property name="prefix" value="/jsp/" />
       <property name="suffix" value=".jsp" />
   </bean>
   
   <!-- 加上MVC驱动 -->
   <mvc:annotation-driven>
       <!--处理请求返回json字符串的中文乱码问题-->
       <mvc:message-converters>
           <bean class="org.springframework.http.converter.StringHttpMessageConverter">
               <property name="supportedMediaTypes">
                   <list>
                       <value>text/html;charset=UTF-8</value>
                       <value>application/json;charset=UTF-8</value>
                   </list>
               </property>
           </bean>
       </mvc:message-converters>
   </mvc:annotation-driven>
       <!--json配置-->
       <bean
       class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
       <property name="messageConverters">
           <list>
               <!--使用json-->
               <bean
                       class="org.springframework.http.converter.StringHttpMessageConverter">
                   <property name="supportedMediaTypes">
                       <list>
                           <value>text/html; charset=UTF-8</value>
                           <value>application/json;charset=UTF-8</value>
                       </list>
                   </property>
               </bean>
               <!--json视图解析-->
               <bean   class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                   <property name="supportedMediaTypes">
                       <list>
                           <value>text/html; charset=UTF-8</value>
                           <value>application/json;charset=UTF-8</value>
                       </list>
                   </property>
               </bean>
           </list>
       </property>
   </bean>
       <!-- 文件上传 id 必须取名multipartResolver，注册我们的文件上传解析器 -->
   <bean id="multipartResolver"
         class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
       <!-- 最大上传的文件的大小 单位 byte -->
       <property name="maxUploadSize" value="54000000" />
       <property name="defaultEncoding" value="UTF-8" />
   </bean>
       <!-- 静态资源处理 -->
   <mvc:default-servlet-handler />
   </beans>
   
   ```

   spring-mybatis.xml
   整合ORM功能

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xmlns:c="http://www.springframework.org/schema/c"
          xmlns:cache="http://www.springframework.org/schema/cache"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:jdbc="http://www.springframework.org/schema/jdbc"
          xmlns:jee="http://www.springframework.org/schema/jee"
          xmlns:lang="http://www.springframework.org/schema/lang"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:p="http://www.springframework.org/schema/p"
          xmlns:task="http://www.springframework.org/schema/task"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xmlns:util="http://www.springframework.org/schema/util"
          xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd
           http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
           http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
           http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd
           http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd ">
       <context:component-scan base-package="com.sz.mapper">
           <context:exclude-filter type="annotation"
                                   expression="org.springframework.stereotype.Controller" />
       </context:component-scan>
       <!-- 引入数据库相关信息的配置文件 -->
       <context:property-placeholder location="classpath:db.properties" />
       <!--连接池-->
       <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
           <property name="driverClass" value="${jdbc.driver}" />
           <property name="jdbcUrl" value="${jdbc.url}" />
           <property name="user" value="${jdbc.username}" />
           <property name="password" value="${jdbc.password}" />
           <!-- 如果有需要，请把所有的属性全部写到properties文件当中去 -->
           <!-- c3p0连接池的私有属性 -->
           <property name="maxPoolSize" value="30" />
           <property name="minPoolSize" value="10" />
           <!-- 关闭连接后不自动commit -->
           <property name="autoCommitOnClose" value="false" />
           <!-- 获取连接超时时间 -->
           <property name="checkoutTimeout" value="100000" />
           <!-- 当获取连接失败重试次数 -->
           <property name="acquireRetryAttempts" value="2" />
       </bean>
       <!-- 最后关键一步，如何整合mybatis -->
   <!-- 1 注入一股mybatis的sqlsessionFactory这就是我们所要做的关键步骤 2 声明式的事务管理 -->
   <bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">
       <property name="dataSource" ref="dataSource" />
       <!-- 引入mappers文件 -->
       <property name="mapperLocations" value="classpath:com/sz/mapper/**/*.xml" />
       <property name="configuration">
           <bean class="org.apache.ibatis.session.Configuration">
               <!-- 可以加入驼峰命名，其它mybatis的配置也就是mybatis.cfg.xml的相关配置都会转移到这里来 -->
               <property name="mapUnderscoreToCamelCase" value="true" />
           </bean>
       </property>
       <!-- 插件配置 -->
       <property name="plugins">
           <array>
               <!-- 分页插件的配置 拦截器实现分页-->
               <bean class="com.github.pagehelper.PageInterceptor">
                   <!-- 这里的几个配置主要演示如何使用，如果不理解，一定要去掉下面的配置 -->
                   <property name="properties">
                       <value>
                           helperDialect=mysql
                           reasonable=true
                           supportMethodsArguments=true
                           params=count=countSql
                           autoRuntimeDialect=true
                       </value>
                   </property>
               </bean>
           </array>
       </property>
   </bean>
   
   <!-- mapper的批量扫描,将在com.sz.mapper里面扫描出的mapper接口,自动创建代理对象并在spring容器中注册,注册的bean的id是类名(首字母小写)-->
   <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
       <!--指定扫描的包名-->
       <property name="basePackage" value="com.sz.mapper" />
       <property name="sqlSessionFactoryBeanName" value="sqlSessionFactoryBean" />
   </bean>
   
   <!-- 事务管理 使用数据源事务管理类进行管理 -->
   
   <bean id="transactionManager"
         class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <property name="dataSource" ref="dataSource" />
   </bean>
   <!-- 确定事务管理的策略 transaction-manager:指向上面的transactionManage -->
   <tx:advice transaction-manager="transactionManager" id="transactionAdvice">
       <!-- 事务处理的相关值以及它的传播性 -->
       <tx:attributes>
           <!-- 查询相关配置为只读 select开头或者get 或者query -->
           <tx:method name="select*" read-only="true" />
           <tx:method name="get*" read-only="true" />
           <tx:method name="query*" read-only="true" />
           <tx:method name="delete*" propagation="REQUIRED"
                      rollback-for="Exception" />
           <tx:method name="update*" propagation="REQUIRED"
                      rollback-for="Exception" />
           <tx:method name="insert*" propagation="REQUIRED"
                      rollback-for="Exception" />
           <tx:method name="add*" propagation="REQUIRED"
                      rollback-for="Exception" />
       </tx:attributes>
   </tx:advice>
   <!-- 使用aop对事务管理的范围进行织入 明确几个点 1 对哪些地方需要进行事务的管理 execution书写，明确边界 2 使用什么策略去管理
       策略我们使用了tx:advice全部书写于其中，在我们的aop的advisor当中只需要去引用这个事务管理者的建议即可。 -->
   <aop:config>
       <aop:pointcut expression="execution(* com.sz.service..*.*(..))"
                     id="txCut" />
       <aop:advisor advice-ref="transactionAdvice" pointcut-ref="txCut" />
   </aop:config>
   
   <!-- 采用注解进行事务配置，请在Service的实现类上面加上@Transanctional注解 -->
   <tx:annotation-driven transaction-manager="transactionManager" />
   </beans>
   
   ```

   db.properties

   ```properties
   jdbc.driver=com.mysql.jdbc.Driver
   jdbc.url=jdbc:mysql://localhost:3306/ssm?useSSL=false&serverTimezone=UTC
   jdbc.username=root
   jdbc.password=root
   ```

   log4j.properties

   ```properties
   #定义LOG输出级别
   log4j.rootLogger=INFO,Console,File
   
   #定义日志输出目的地为控制台
   log4j.appender.Console=org.apache.log4j.ConsoleAppender
   log4j.appender.Console.Target=System.out
   #可以灵活的指定日志输出格式，下面一行是指定具体的格式
   log4j.appender.Console.layout=org.apache.log4j.PatternLayout
   log4j.appender.Console.layout.ConversionPattern=[%c]-%m%n
   
   #mybatis显示SQL语句日志配置
   log4j.logger.com.sz.mapper=DEBUG
   
   #文件大小到达指定尺寸的时候产生一个新的文件
   log4j.appender.File=org.apache.log4j.RollingFileAppender
   #指定输出目录 这里会放在tomcat之下
   log4j.appender.File.File=D:/log.log
   #log4j.appender.File.File=logs/ssm.log
   #定义文件最大大小
   log4j.appender.File.MaxFileSize=10MB
   #输出所有日志，如果换成DEBUG表示输出DEBUG以上级别日志
   log4j.appender.File.Threshold=ALL
   log4j.appender.File.layout=org.apache.log4j.PatternLayout
   log4j.appender.File.layout.ConversionPattern=[%p][%d{yyyy-MM-dd HH\:mm|\:ss}][%c]%m%n
   ```

   spring-context.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xmlns:c="http://www.springframework.org/schema/c"
          xmlns:cache="http://www.springframework.org/schema/cache"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:jdbc="http://www.springframework.org/schema/jdbc"
          xmlns:jee="http://www.springframework.org/schema/jee"
          xmlns:lang="http://www.springframework.org/schema/lang"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:p="http://www.springframework.org/schema/p"
          xmlns:task="http://www.springframework.org/schema/task"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xmlns:util="http://www.springframework.org/schema/util"
          xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd
           http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
           http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
           http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd
           http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd ">
   <!-- 做spring的基础配置 -->
   <!-- 1 spring容器注册 -->
   <context:annotation-config/>
   <!-- 2 自动扫描配置 -->
   <context:component-scan base-package="com.sz"/>
   
   <!-- 3 激活aop注解方式的代理 -->
   <aop:aspectj-autoproxy/>
   
   <!-- 消息格式转换 -->
   <bean id="conversionService"
         class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
       <property name="registerDefaultFormatters" value="false" />
       <property name="formatters">
           <set>
               <bean class="org.springframework.format.number.NumberFormatAnnotationFormatterFactory" />
           </set>
       </property>
       <property name="formatterRegistrars">
           <set>
               <bean
    class="org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar">
                   <property name="dateFormatter">
                       <bean
                               class="org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean">
                           <property name="pattern" value="yyyyMMdd" />
                       </bean>
                   </property>
               </bean>
           </set>
       </property>
   </bean>
   </beans>
   
   ```

## 分页功能的说明

mybatis分页插件会帮我们自动完成分页功能，只需要按照如下的模板编写即可。

```java
PageHelper.startPage(pageNO,5);
List<Product> l = productMapper.list();
## PageInfo<Product> pageInfo = new PageInfo<Product>(l);

```









