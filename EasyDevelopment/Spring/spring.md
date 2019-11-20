# spring简介

## 基础技术

1. java
2. 反射
3. xml
4. xml解析
5. 代理
6. 大量设计模式

关键在于在容器中获取对象,spring默认是单例模式

## 基础环境搭建

```xml
数据库的操作
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.13</version>
</dependency>
<dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-tx</artifactId>
          <version>5.0.8.RELEASE</version>
</dependency>
<dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-jdbc</artifactId>
          <version>5.0.8.RELEASE</version>
      </dependency>
<!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.2</version>
</dependency>

```

1. 添加spring依赖(有四个context,core,beans,expression,但是context依赖其他的3种,所以只需要导入context即可)

   ```xml
   <dependency>
             <groupId>org.springframework</groupId>
             <artifactId>spring-context</artifactId>
             <version>5.0.8.RELEASE</version>
   </dependency>
   ```

2. 编写一个spring的配置文件(放在resources下)

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   <!--将对象的创建交给spring容器,在这个配置文件中声明我要什么对象
       bean标签的class: 写java类的全限定类名,它是通过全类名然后使用反射技术进行创建的
       bean标签的id:标识符,获取对象的时候使用-->
       <bean class="com.sz.pojo.Girl" id="girl">
           <property name="age" value="10"></property>
           <property name="name" value="顶顶顶顶"></property>
       </bean>
   </beans>
   ```

   

3. 通过spring的应用程序上下文对象获取对象

```java
@Test
public void m1(){
        //获取上下文对象,spring里面声明对象都需要通过上下文对象获取
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        Girl girl1 = ctx.getBean("girl", Girl.class);
        System.out.println(girl);
    }
```

ClassPathXmlApplicationContext()函数里面可以传入String数组,达到读取多个配置文件的目的

## 核心学习内容

### IOC

1. 控制反转(inverse of control)也称依赖注入

   控制:创建对象 ,彼此关系的权利

   反转:此权利交给了spring容器

2. spring的配置文件值的注入:

+ setter注入(最常用):

必须其字段有对应setter方法才可以完成name setName();

通过property子节点完成注入

报错分析:

```java
No default constructor found; nested exception is java.lang.NoSuchMethodException: com.sz.pojo.Girl.<init>()
```

com.sz.pojo.Girl这个类必须要有无参构造器

+ 构造注入:

```xml
<!--前提是Girl有构造方法public Girl(String name, Integer age) constructor-arg里面的name属性与构造器的参数匹配-->
<bean class="com.sz.pojo.Girl" id="girl_demo">
        <constructor-arg name="name" value="wc"></constructor-arg>
        <constructor-arg name="age" value="10"></constructor-arg>
</bean>
<!--前提是Girl有构造方法public Girl(String name)-->
<bean class="com.sz.pojo.Girl" id="girl_demo">
        <constructor-arg name="name" value="wc"></constructor-arg>
</bean>
```



### bean分析

1. 属性分析

abstract(true):表示该bean不能被实例化,只能被继承

parent:表示该bean继承于哪个bean,用id做标识符

init-method:初始化时自动调用的方法,适合数据库连接,数据源的注入

destroy-method:销毁时自动调用的方法

lazy-init(true):默认容器ctx被实例化时就会去执行配置文件applicationContext.xml中里面注册了的bean的无参构造方法

但是调用了这个属性的bean,会在调用bean的时候才执行无参构造方法

scope(prototype):调用该bean声明的对象都是不同的,都是新new出来的

scope(singleton):调用该bean声明的对象是相同的,都是同一个,默认是这种

depends-on:依赖的bean,如果某一个bean严重依赖另一个bean

```java
ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
((ClassPathXmlApplicationContext) ctx).close();
((ClassPathXmlApplicationContext) ctx).refresh();
//这两个方法调用其中之一才会执行destroy-method方法
```

2. 子标签分析

   property标签:

   name(javabean的属性),value(字面值可以表示的),ref(字面值无法表示的)

   ```xml
   <bean class="com.sz.pojo.Girl" id="girl">
   <property name="age" value="10"></property>
           <property name="name" value="你"></property>
           <property name="dog" ref="dog"></property>
       </bean>
      <bean class="com.sz.pojo.Dog" id="dog">
      </bean> 
   ```

   其他类型

   ```xml
   <bean class="com.sz.pojo.Girl" id="girl">
           <property name="age" value="10"></property>
           <property name="name" value="你"></property>
           <property name="dog" ref="dog"></property>
       <!--数组-->
           <property name="str">
               <array>
                   <value>刘德华</value>
                   <value>郭富城</value>
               </array>
           </property>
       <!--集合-->
           <property name="list">
               <list>
                   <value>1</value>
                   <value>2</value>
               </list>
           </property>
       <!--list集合里面装dog对象-->
           <property name="listDog">
               <list>
                   <!--内部bean无法被外部引用-->
                   <bean class="com.sz.pojo.Dog">
                       <property name="name" value="good boy"></property>
                   </bean>
                   <bean class="com.sz.pojo.Dog">
                       <property name="name" value="good girl"></property>
                   </bean>
               </list>
           </property>
       <!--map集合里面装dog对象-->
       <property name="mapDog">
           <map>
           <entry key="dog1">
               <bean class="com.sz.pojo.Dog">
                       <property name="name" value="good boy"></property>
               </bean>
           </entry>
           <entry key="dog2">
               <bean class="com.sz.pojo.Dog">
                       <property name="name" value="good boy"></property>
               </bean>
           </entry>
           </map>
       </property>
       </bean>
   ```

### 自动注入(autowire)

+ byType:按照类型注入,在上下文搜索对应需要的bean(primary="true"默认也是true)

  如果该类型有且只有一个,匹配成功

  如果没有该类型则不注入

  如果超过一个,则报错

  ```xml
  <!--User类里面有类型为Dog的属性-->
  <bean class="com.sz.pojo.User" id="user" autowire="byType">
  </bean>
      <bean class="com.sz.pojo.Dog">
      <property name="name" value="my Dog"></property>
  </bean>
  ```

+ byName:按照javabean对应的pojo里面的属性名来匹配

  ```xml
  <!--User类里面有类型为Dog的属性,且属性名为dog-->
  <bean class="com.sz.pojo.User" id="user" autowire="byName">
  </bean>
      <bean class="com.sz.pojo.Dog" id="dog">
      <property name="name" value="my Dog"></property>
      </bean>
  ```

  下面的了解即可,用处不大

+ byConstructor

+ default

+ no

### 引入外部文件

```xml
applicationContext.xml中:
<!--通过这种方式引入类路径下的文件-->
<context:property-placeholder location="classpath:jdbc.properties"/>
    <!--这个标签可以将其他的配置文件引入,这样的话,我们将来整体读取这一核心配置文件即可,意思就是要用UserBean.xml里面的bean的话,可以只引入applicationContext.xml-->
 <!--而且classpath:UserBean.xml支持*匹配UserBean-*.xml就会引入所以"UserBean-"开头的xml文件-->
    <import resource="classpath:UserBean.xml"/>
    <bean class="com.sz.dao.ProviderDao">
        <!--${}表达式可以去引用我们引入的这些properties里面的属性的值通过他的键名来得到值-->
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="user" value="${username}"/>
        <property name="password" value="${password}"/>
    </bean>
```

## 常用注解

### component

### controller(springmvc,强调处理控制层)

### service(业务层)

### repository(强调数据访问层)

四个注解功能相同

1. 注解里面的userService就是bean的id

```java
package com.sz.service;
@Component("userService")
public class UserService {
    public void eat(){
        System.out.println("吃了");
    }
}
```

2. 注册com.sz.service下的有@Component注解的bean

```xml
application.xml中:
<!--扫描该包以及所有子包-->
<context:component-scan base-package="com.sz.service"/>
```

## AOP(xml版本)

### 专业术语

1. target：目标类，需要被代理的类。例如：UserService

2. Joinpoint(连接点):所谓连接点是指那些可能被拦截到的方法。例如：所有的方法

3. PointCut 切入点：已经被增强的连接点。例如：addUser()

4. advice 通知/增强，增强代码。例如：after、before

5. Weaving(织入):是指把增强advice应用到目标对象target来创建新的代理对象proxy的过程.

6. proxy 代理类

7. Aspect(切面): 是切入点pointcut和通知advice的结合

​	一个线是一个特殊的面。

​	一个切入点和一个通知，组成成一个特殊的面。

### 通知

• 前置通知 org.springframework.aop.MethodBeforeAdvice

• 在目标方法执行前实施增强

• 后置通知 org.springframework.aop.MethodAfterAdvice

• 在目标方法执行前实施增强

• 后置返回通知 org.springframework.aop.AfterReturningAdvice

• 在目标方法返回值执行后实施增强

• 环绕通知 org.aopalliance.intercept.MethodInterceptor(功能最强,但是用得少)目标方法的调用由环绕通知决定，即你可以决定是否调用目标方法，而前置和后置通知   是不能决定的，他们只是在方法的调用前后执行通知而已，即目标方法肯定是要执行的。

• 在目标方法执行前后实施增强

• 异常抛出通知 org.springframework.aop.ThrowsAdvice

• 在方法抛出异常后实施增强

• 引介通知 org.springframework.aop.IntroductionInterceptor

在目标类中添加一些新的方法和属性

### 切入点表达式

1.execution()  用于描述方法 【掌握】

​	语法：execution(修饰符  返回值  包.类.方法名(参数) throws异常)

​		修饰符，一般省略

​			public		公共方法

​			*			任意

​		返回值，不能省略

​			void			返回没有值

​			String		返回值字符串

​			* 			任意

​		包，[省略]

​			com.itheima.crm			固定包

​			com.itheima.crm.*.service	com包下面子包任意 （例如：com.itheima.crm.staff.service）

​			com.itheima.crm..			com包下面的所有子包（含自己）

​			com.itheima.crm.*.service..	com包下面任意子包，固定目录service，service目录任意包

​		类，[省略]

​			UserServiceImpl			指定类

​			*Impl					以Impl结尾

​			User*					以User开头

​			*						任意

​		方法名，不能省略

​			addUser					固定方法

​			add*						以add开头

​			*Do						以Do结尾

​			*						任意

​		(参数)

​			()						无参

​			(int)						一个整型

​			(int ,int)					两个

​			(..)						参数任意

​		throws ,可省略，一般不写。

### 步骤

1. 额外补充的依赖

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.2</version>
</dependency>
```

2. 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:c="http://www.springframework.org/schema/c"
       xmlns:cache="http://www.springframework.org/schema/cache"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:jee="http://www.springframework.org/schema/jee"
       xmlns:lang="http://www.springframework.org/schema/lang"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:task="http://www.springframework.org/schema/task"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
		http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd
		http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang.xsd">
<!--1.aop是基于代理完成的,所以我们要激活我们自动代理-->
    <aop:aspectj-autoproxy/>
    <!--目标类要在spring中注册,没有注册不能作为目标类-->
    <bean class="com.sz.service.ProviderService" id="providerService">
    </bean>
<!--2.注册一个切面要使用的类-->
    <bean class="com.sz.advice.BeforeAdvice" id="beforeAdvice">
    </bean>
<!--3.配置切面的元素-->
<aop:config>
    <aop:aspect  ref="beforeAdvice">
        <!--aop:before表明它确实是前置通知(可以有多个前置通知)
method:指明它使用哪个方法来切beforeAdvice里面的方法名字
pointcut:切入点
你要什么包下面的什么类下面的什么方法
        -->
        <!--execution(* com.sz.service.ProviderService.*())切无参
           execution(* com.sz.service.ProviderService.*(java.lang.Integer))切一个参数是Integer类型的
          execution(* com.sz.service.ProviderService.*(int))切一个参数是int类型的
包装类与基本数据类型有严格的区分
        -->
<aop:before method="methodBefore" pointcut="execution(* com.sz.service.ProviderService.*(..))"></aop:before>
    </aop:aspect>
</aop:config>
</beans>
```

3. 测试

```java
 ApplicationContext ctx=new ClassPathXmlApplicationContext("application.xml");
 ProviderService demo = ctx.getBean("providerService", ProviderService.class);
 demo.add();
```

## AOP(注解)

@order(数字)注解可以指定before通知的执行顺序,数字小的先执行(类级别的注解)

@order(数字)注解可以指定after通知的执行顺序,数字大的先执行(类级别的注解)

```java
package com.sz.advice;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
@Order(1)//标记顺序
@Aspect//标记为切面
@Component//相对于在xml中注册bean
public class BeforeAdvice {
    @Before("execution(* com.sz.service.*.*(..))")
    public void before(){
        System.out.println("在一个世纪以前");
    }
}
```

### 常见注解总结

1. Configuration:标明一个类为配置类,然后程序启动的时候只要扫描这个类,就可以清楚所有的配置规则
2. Component:标明一个类为spring的一个组件,可以被spring容器所管理
3. Service:同上,语义为服务层
4. Repository:同上,语义为Dao层
5. Controller:同上,语义为控制层
6. ComponentScan:组件扫描,可以绝对去扫描哪些包
7. Bean:用于在spring容器中注册一个Bean
8. Autowired:自动注册bean,会按类型装配
9. @Resource默认按名称装配，如果不到与名称匹配的bean，会按类型装配。