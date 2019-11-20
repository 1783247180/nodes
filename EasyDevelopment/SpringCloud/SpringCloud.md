# 整体简介

### 服务发现(Eureka)

AB服务需要注册到Eureka中

### 负载均衡(Ribbon)

A服务想调用B服务,需要选择哪台B服务的服务器就需要使用负载均衡了

### 断路器(Hystrix)

A调用B,B调用C,C调用D 但是D或者C服务出现问题导致A服务与B服务一直处于等待状态,这是需要在B服务引入断路器,当B服务请求C服务失败几次后,B服务直接返回告诉A服务,访问失败

### 服务网关(Zuul)

过滤请求

### 分布式配置(SpringCloud)

数据部署到不同的地方

![1561084778115](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561084778115.png)

![1561086299621](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561086299621.png)

### 版本问题

SpringCloud有三个版本

Edgware：依赖的spring boot版本升级仍然是1.5， 许多组件的名称变化

Finchley：依赖的spring boot版本升级到2.0，真正的大版本升级，重大变化

Greenwich：依赖的spring boot版本升级到2.1，支持java11

详情看:<https://juejin.im/post/5cb5bb296fb9a0686a22470b>

Greenwich中:

spring cloud的组件的artifactId名称变更: 左边是旧，右边是新的

```word
spring-cloud-starter-eureka --> spring-cloud-starter-netflix-eureka-client(eureka客户端)
spring-cloud-starter-eureka-server  -->  spring-cloud-starter-netflix-eureka-server(eureka服务器)
spring-cloud-starter-feign --> spring-cloud-starter-openfeign(服务间调用,负载均衡)
spring-cloud-starter-hystrix -> spring-cloud-starter-netflix-hystrix(断路器)
```

### QuickStart(eureka)

具体步骤:

1. 建立一个空项目

2. new 3个module(SpringBoot项目),一个作为eureka服务器选择eureka服务器模块,一个作为provider(eureka客户端,需要web模块),一个作为consumer(eureka客户端,需要web模块)

   ![1561108904084](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561108904084.png)

3. 服务器的module

   - yml配置

     ```yml
     server:
       port: 8761
     eureka:
       instance:
         hostname: eureka-server
       client:
         register-with-eureka: false #不将服务器自己也注册到eureka
         fetch-registry: false #表示自己就是注册中心，职责是维护实例，不参加检索
         service-url:
           defaultZone: http://localhost:8761/eureka/ #默认eureka的管理页面
           #第二种写法http://${eureka.instance.hostname}:${server.port}/eureka/
     ```

   - 主配置类

     ```java
     package com.yy.eureka;
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
     @EnableEurekaServer
     @SpringBootApplication
     public class EurekaApplication {
     //启动就可以访问http://localhost:8761/eureka/了
         public static void main(String[] args) {
             SpringApplication.run(EurekaApplication.class, args);
         }
     }
     ```

4. provider配置

   - yml配置

     ```yml
     server:
       port: 8002
     spring:
       application:
         name: provider-01   #服务名必须要有
     eureka:
       instance:
         prefer-ip-address: true #注册服务时使用服务的IP地址
       client:
         service-url:
           defaultZone: http://localhost:8761/eureka/  #将此服务注册到地址http://localhost:8761/eureka/
     ```

   - ![1561109866539](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561109866539.png)

   - 运行即可

   - 同一个provider在服务器上怎么注册几次?

     打包成jar包

     ![1561109952542](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561109952542.png)

     ![1561110006880](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561110006880.png)

     把jar包复制出来

     ![1561110044452](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561110044452.png)

     cmd运行 java -jar jar包名字.jar

     ![1561110094335](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561110094335.png)

​    

5. consumer配置

   - yml配置

     ```yml
     server:
       port: 8200
     spring:
       application:
         name: consumer-01
     eureka:
       instance:
         prefer-ip-address: true #注册服务时使用服务的IP地址
       client:
         service-url:
           defaultZone: http://localhost:8761/eureka/  #将此服务注册到地址http://localhost:8761/eureka/
     ```

   - 主配置类

     ```java
     package com.yy.consumer;
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
     import org.springframework.cloud.client.loadbalancer.LoadBalanced;
     import org.springframework.context.annotation.Bean;
     import org.springframework.web.client.RestTemplate;
     
     @EnableDiscoveryClient  //开启发现服务功能
     @SpringBootApplication
     public class ConsumerApplication {
     
         public static void main(String[] args) {
             SpringApplication.run(ConsumerApplication.class, args);
         }
         @LoadBalanced  //开启负载均衡,如果一个provider服务开启了多次,那么就会负载均衡
         @Bean  //RestTemplate是consumer发送请求获取provider服务的类,这里是将RestTemplate注册到spring容器中
         public RestTemplate restTemplate(){
             return new RestTemplate();
     }
     }
     
     ```

   - controller类

     ```java
     package com.yy.consumer.controller;
     
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.web.bind.annotation.RequestMapping;
     import org.springframework.web.bind.annotation.RestController;
     import org.springframework.web.client.RestTemplate;
     
     @RestController
     public class Mycontroller {
         @Autowired
         private RestTemplate restTemplate;//自动注入
         @RequestMapping("/buy")
         public String buy(){
             //getForObject方法发送请求给provider-01服务,provider-01里面提供了/ticket请求,返回值是string
             //在provider-01中
             //@RequestMapping("/ticket")
         //public String getT(){
         //System.out.println("8001");
         //String ticket = myService.getTicket();
         //return ticket;
     //}
             return "我买了"+restTemplate.getForObject("http://PROVIDER-01/ticket",String.class);
         }
     }
     
     ```

# 基本知识(eureka)

### RestTemplate类

#### 介绍

RestTemplate是Spring提供的用于访问Rest服务的客户端模板工具集，提供了多种远程访问Http的方法

[RestTemplate的API]: https://docs.spring.io/spring/docs/5.0.6.RELEASE/javadoc-api/

#### 意义

在一些不涉及实现方法的模块中（消费者），只需要调用其他服务暴露出的接口即可满足的需求，使用RestTemplate类中的方法可以发出需要的HTTP请求并得到返回结果。（类似Ajax）

#### RestTemplate用法

```java
RestTemplate restTemplate = new RestTemplate();
//url:请求地址                 
//map:请求参数
        //MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        //map.add("typeId",typeId);
        //map.add("typeName",typeName);
        //说明MultiValueMap是一个接口,可以实现一个key对应多个value,LinkedMultiValueMap是其对应的实现类
//type.class:HTTP响应转换成的对象类型(其实就是url对应的方法的返回值)
restTemplate.getForObject(url,type.class);
restTemplate.postForObject(url,map,type.class);
```

被调方：

```java
@PostMapping("/modify")
public RespBody modify(@RequestParam("typeId")int typeId,@RequestParam("typeName")String typeName) {
    try {
        service.modify(typeId,typeName);
        return RespBody.success();
    }catch (Exception e) {
        logger.error("出错", e);
        return RespBody.fail(ErrorCode.ERROR);
    }
}
```

调用方：

```java
public RespBody modify(long typeId,String typeName) {
    try {
        MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("typeId",typeId);
        map.add("typeName",typeName);
        //注意是postForObject
        RespBody respBody = httpRestTemplate.postForObject(URL+ "/modify",RespBody.class,map);
        return respBody;
    } catch (Exception e) {
        log.error("出错", e);
    }
}
```



### Eureka介绍及原理

#### 理解

==Eureka就像一个物业管理公司，其他微服务就像小区的住户，每个住户入住时都要向物业管理公司注册，并定时向物业公司交管理费==

#### 原理

- Eureka使用的是C-S结构（客户端-服务端）
- 两大组件：Eureka Server（提供注册服务）、 Eureka Client（JAVA客户端，负责发送心跳）
- 系统中的其他微服务使用Eureka客户端连接到Eureka服务端维持心跳连接（即注册）。SpringCloud的其他模块可以通过Eureka Server 来发现系统中的微服务并加以调用

![Eureka的架构图](D:\尚硅谷\JAVA\SpringCloud\notes\images\Eureka的架构图.png)

#### 运用

+ eureka服务器pom.xml配置

  ```xml
  <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
  </dependency>
  ```

+ provider与consumer配置

  ```xml
  <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  ```

  

### actuator与微服务注册完善

#### 需求分析

![1561129324735](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1561129324735.png)

A处的192.168.180.1:provider-01:8002是自己生成的一个名字,需要更改

把鼠标放在A处,出现B处 这是一个链接,默认这个链接是无法访问的,所以需要配置该链接,并且将192.168.180.1:provider-01:8002对应的信息在B处页面显示出来

A,B处的修改无需导入actuator只需在application.yml中加入

```yml
eureka:
  instance:
    instance-id: consumer--8200		#修改别名,A处会变成consumer--8200
    prefer-ip-address: true #注册服务时使用服务的IP地址,就是B处开头是192.168.180.1的原因(自己的电脑在校园网内网中的地址)
```



#### B处介绍

一款可以帮助你监控系统数据的框架,其可以监控很多很多的系统数据,如:

显示应用的健康信息

显示Info应用信息

显示HTTP跟踪信息

显示当前应用程序的“Metrics”信息

显示所有的@RequestMapping的路径信息

#### 应用

点击A处出现对应服务的信息（192.168.180.1/8001/actuator/info），即info内容的详细信息修改

##### 作用

在查看Eureka时点击进入某个微服务的info时，能给查看者一些必要的信息，可以帮助查看者快速的了解该微服务，开发中十分有意义。

##### 修改方法

1. 当前工程添加依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

1. 在当前工程的application.yml文件添加回显信息

```yml
info:
  author: XXX
  build-version: $project.version$ #上面指定的动态信息修改以$标志开头结尾
```

### Eureka的自我保护

#### 介绍

Eureka的自我保护机制主要是为了网络异常时保持高可用设计的，当在Eureka中注册的一个微服务超过设定是时间内（默认90秒）没有向Eureka服务端发送心跳，该微服务会进入自我保护模式。

#### 理解

好死不如赖活：Eureka的设计哲学是宁可保留错误的服务信息，也不盲目注销可能健康的服务。所以异常的服务不会被注销，而是进入了自我保护模式。

#### 自我保护模式的开关

在Eureka Server模块下的yml文件中添加配置信息即可，true表示打开自我保护模式；false表示关闭自我保护模式（不推荐）

```java
  server:
    enable-self-preservation: false
```

### Eureka的服务发现

#### 介绍

系统中的微服务可以通过Eureka的服务发现去获得在Eureka中注册的服务的信息，这是一个对外暴露的接口。(@EnableDiscoveryClient)

#### 使用方法（provider中）

1. 注入DiscoveryClient 对象（spring包下的），在controller方法中获取

```java
@Autowired
private DiscoveryClient discoveryClient;    

@ResponseBody
@GetMapping("/provider/discovery")
public Object discovery(){
    //获取在eureka注册的所有服务
        List<String> list = discoveryClient.getServices();
        System.out.println(list);
        //在注册中心获取provider01对应的服务列表
    //spring.application.name=provider01
        List<ServiceInstance> insList = discoveryClient.getInstances("provider01");
        for (ServiceInstance si:insList) {
            System.out.println(si.getHost() +"," + si.getServiceId() +"," +si.getPort() +"," +si.getUri() +"," +si.getMetadata());
        }
        return this.discoveryClient;
    }
```

1. 在主启动类中加入@EnableDiscoveryClient注解

```java
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class Provider8001_APP {
    public static void main(String[] args) {
        SpringApplication.run(Provider8001_APP.class,args);
    }
}
```

#### 使用方法（consumer中）

在controller方法中使用restTemplate对象调用provider中暴露的URL 并获得返回对象即可

```java
@GetMapping("/discovery")
public Object discovery() {
        return restTemplate.getForObject(URL_PREFIX+"/provider/discovery",Object.class);
    }
```

### Eureka的集群配置

#### 集群

集群就是在不同的机器上配置相同的服务来构建要一个大的运算整体,QuickStart中将provider的module打包就是实现集群

### Eureka与Zookeeper对比

#### 结论

Eureka可以很好的应对网络故障导致部分节点失去连接的情况，而不会像zookeeper那样导致整个注册服务系统的瘫痪。

### Ribbon负载均衡

#### 介绍

简单来说负载均衡就是将用户的请求ping平摊的分配到多个任务上，从而是系统达到HA（高可用）,QuickStart中的consumer使用的@LoadBalanced注解就是Ribbon负载均衡(无须添加任何依赖,SpringCloud默认使用ribbon负载均衡)

#### Ribbon核心组件IRule

IRule：根据特定算法从服务列表中选取一个需要访问的服务

#### 七大方法

==IRule是一个接口，七大方法是其自带的落地实现类==

- RoundRobinRule：轮询（默认方法）
- RandomRule：随机
- AvailabilityFilteringRule：先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务进行轮询
- WeightedResponseTimeRule：根据平均响应时间计算服务的权重。统计信息不足时会按照轮询，统计信息足够会按照响应的时间选择服务
- RetryRule：正常时按照轮询选择服务，若过程中有服务出现故障，在轮询一定次数后依然故障，则会跳过故障的服务继续轮询。
- BestAvailableRule：先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
- ZoneAvoidanceRule：默认规则，符合判断server所在的区域的性能和server的可用性选择服务

#### 切换规则方法

只需在==配置类==中配置一个返回具体方法的bean即可

```java
@Bean
public IRule MyRule(){
        return new RandomRule();    
    }
```



### Feign负载均衡

#### 介绍

Feign是一个声明式WebService客户端，使用方法时定义一个接口并在上面添加注解即可。Feign支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，使其支持SpringMVC和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡。

#### 应用

在consumer中:

1. 加入依赖

   ```xml
   <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. 主配置类加上注解@EnableFeignClients

3. feign是采用基于接口的注解,使用@FeignClient表明该注解是一个Feign声明的客户端，设置里面的value参数为provider-01，表明它将去调用服务提供商的provider-01服务，即注解里面的值需要和服务提供商provider-01里的application.yaml配置文件中的名字(spring.application.name=provider-01)一致。

   ```java
   package com.yy.consumer.service;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.RequestMapping;
   @FeignClient(value = "provider-01")
   public interface MyService {
       @RequestMapping("/ticket")
        String ticket();
   }
   ```

   controller中调用接口MyService

   ```java
   package com.yy.consumer.controller;
   import com.yy.consumer.service.MyService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   import org.springframework.web.client.RestTemplate;
   @RestController
   public class Mycontroller {
       @Autowired
       private MyService myService;//可能会报错,无所谓运行就行了
       //访问consumer中的这个方法的路径就可以测试了
       @RequestMapping("/ticket")
       public String ticket(){
           return myService.ticket();
       }
   }
   ```

   

   在provider中:

   ```java
   package com.yy.provider.controller;
   import com.yy.provider.service.MyService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   @RestController
   public class MyController {
       @Autowired
       private MyService myService;
   @RequestMapping("/ticket")
       public String getT(){
       System.out.println("8001");
       String ticket = myService.getTicket();
       return ticket;
   }
   }
   ```

   ```yml
   spring:
     application:
       name: provider-01   #服务名必须要有
   ```

   

### Hystrix断路器

#### 需求分析

扇出:多个微服务互相调用的时候，如果A调用B、C，而B、C又继续调用其他微服务，这就是扇出。

问题：如果其中一个服务provider出现问题，那么会导致这整个被调用的微服务等待响应，从而浪费资源

解决：出现问题的微服务，向调用方（consumer）返回一个符合预期的，可处理的备选响应（FallBack） ，而不是长时间的等待。

#### 介绍

- Hystrix是一个用于处理分布式系统延迟和容错的开源库。分布式系统中，依赖避免不了调用失败，比如超时，异常等。Hystrix能保证在出现问题的时候，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。
- Hystrix类似一个“断路器”，当系统中异常发生时，断路器给调用返回一个符合预期的，可处理的FallBack，这样就可以避免长时间无响应或抛出异常，使故障不能再系统中蔓延，造成雪崩。

#### 服务熔断

- 熔断机制的注解是@HystrixCommand
- 熔断机制是应对雪崩效应的一种==链路保护机制==，一般存在于服务端（provider）
- 当扇出链路的某个服务出现故障或响应超时，会进行服务降级，进而熔断该节点的服务调用，快速返回“错误”的相应信息。
- Hystrix的熔断存在阈值，缺省是5秒内20次调用失败就会触发

##### 熔断案例

1. 添加依赖(provider)

   ```xml
   <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   ```

2. 主配置类加注解@EnableCircuitBreaker

3. 在controller里面

   ```java
   package com.yy.provider.controller;
   import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
   import com.yy.provider.service.MyService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   @RestController
   public class MyController {
       @Autowired
       private MyService myService;
       @HystrixCommand(fallbackMethod = "nullStringFallBack")
       @RequestMapping("/FallBackTest")
       public String getT(){
           String ticket = myService.getTicket();
           if(ticket == null){
               throw new RuntimeException("返回值为空！");
           }
       return ticket;
   }
       //切记nullStringFallBack方法的返回值必须与getT的返回值类型一致
       public String nullStringFallBack() {
           System.out.println("进入FallBack");
           return "error";
       }
   }
   ```

4. 在service中返回null

   ```java
   package com.yy.provider.service;
   import org.springframework.stereotype.Component;
   @Component
   public class MyService {
       public String getTicket(){
           return null;
       }
   }
   ```

5. 在consumer里面去调用该provider,就会把nullStringFallBack里面的return "error"返回到consumer

#### 解耦与降级处理

用处不大,以后再学

#### HystrixDashboard服务监控

用处不大,以后再学

### Zuul路由网关

外部的应用如何来访问内部各种各样的微服务呢？

在微服务架构中，后端服务往往不直接开放给外部第三方调用端，而是通过一个API网关根据请求的url，路由到相应的服务。当添加API网关后，在第三方调用端和服务提供方之间就创建了一面墙，这面墙直接与调用方通信进行权限控制，后将请求均衡分发给后台服务端。

主要用途用于为外部应用提供访问

#### 简单使用(转发)

1、添加依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
```

引入`spring-cloud-starter-zuul`包

2、配置文件

```properties
spring.application.name=gateway-service-zuul
server.port=8888

#这里的配置表示，访问/it/** 直接重定向到http://www.ityouknow.com/**
zuul.routes.baidu.path=/it/**
zuul.routes.baidu.url=http://www.ityouknow.com/
```

3、启动类

```java
@SpringBootApplication
@EnableZuulProxy
public class GatewayServiceZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceZuulApplication.class, args);
	}
}
```

启动类添加`@EnableZuulProxy`，支持网关路由。

史上最简单的zuul案例就配置完了

4、测试

启动`gateway-service-zuul-simple`项目，在浏览器中访问：`http://localhost:8888/it/spring-cloud`，看到页面返回了：`http://www.ityouknow.com/spring-cloud` 页面的信息，如下：

![](F:\Typora\EasyDevelopment\SpringCloud\img\zuul-01.jpg)

我们以前面文章的示例代码producer01为例来测试请求的重定向，在配置文件中添加：

```properties
zuul.routes.hello.path=/hello/**
zuul.routes.hello.url=http://localhost:8010/
```

访问：`http://localhost:8888/hello/FallBackTest`，返回：`error`

说明访问`gateway-service-zuul-simple`的请求自动转发到了`producer01`，并且将结果返回。

#### 服务化(主要使用这种)

通过url映射的方式来实现zuul的转发有局限性，比如每增加一个服务就需要配置一条内容，另外后端的服务如果是动态来提供，就不能采用这种方案来配置了。实际上在实现微服务架构时，服务名与服务实例地址的关系在eureka server中已经存在了，所以只需要将Zuul注册到eureka server上去发现其他服务，就可以实现转发。

我们结合示例来说明，在上面示例项目`gateway-service-zuul-simple`的基础上来改造。

1、添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

增加`spring-cloud-starter-eureka`包，添加对eureka的支持。

2、配置文件

配置修改为：

```properties
spring.application.name=gateway-service-zuul
server.port=8888
#这里的配置表示，访问/it/** 直接重定向到http://www.ityouknow.com/**
#zuul.routes.baidu.path=/it/**
#zuul.routes.baidu.url=http://www.ityouknow.com/
#zuul.routes.hello.path=/hello/**
#zuul.routes.hello.url=http://localhost:8010/
zuul.routes.api-a.path=/helloProvider-02/**
zuul.routes.api-a.serviceId=provider-02
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/

```

3、测试

外面的服务器访问微服务统统使用该地址`http://localhost:8888/helloProvider-02/Test`，返回：`test`

注意会自动实现Ribbon负载均衡

**网关的默认路由规则**

但是如果后端服务多达十几个的时候，每一个都这样配置也挺麻烦的，spring cloud zuul已经帮我们做了默认配置。默认情况下，Zuul会代理所有注册到Eureka Server的微服务，并且Zuul的路由规则如下：`http://ZUUL_HOST:ZUUL_PORT/微服务在Eureka上的serviceId/**`会被转发到serviceId对应的微服务。

我们注销掉`gateway-service-zuul-eureka`项目中关于路由的配置：

```properties
#zuul.routes.api-a.path=/helloProvider-02/**
#zuul.routes.api-a.serviceId=provider-02
#默认配置:
#zuul.routes.api-a.path=/provider-02/**
#zuul.routes.api-a.serviceId=provider-02
```

重新启动后，访问`http://localhost:8888/provider-02/Test`，测试返回结果和上述示例相同，说明Spring cloud zuul默认已经提供了转发功能。

### SpringCloud Config 

在我们了解spring cloud config之前，我可以想想一个配置中心提供的核心功能应该有什么

- 提供服务端和客户端支持
- 集中管理各环境的配置文件
- 配置文件修改之后，可以快速的生效
- 可以进行版本管理
- 支持大的并发查询
- 支持各种语言

Spring Cloud Config可以完美的支持以上所有的需求。

Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用。Spring cloud使用git或svn存放配置文件，默认情况下使用git，我们先以git为例做一套示例。

首先在github上面创建一个工作空间config-repo再工作空间中创建了一个文件夹config-repo用来存放配置文件，为了模拟生产环境，我们创建以下三个配置文件：

```txt
// 开发环境
neo-config-dev.properties
// 测试环境
neo-config-test.properties
// 生产环境
neo-config-pro.properties
```

每个配置文件中都写一个属性neo.hello,属性值分别是 hello im dev/test/pro 。下面我们开始配置server端

#### server 端

##### 1、添加依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-server</artifactId>
	</dependency>
</dependencies>
```

只需要加入spring-cloud-config-server包引用既可。

##### 2、配置文件

```yml
server:
  port: 8020
spring:
  application:
    name: spring-cloud-config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/1783247180/config-repo     # 配置git仓库的地址
          search-paths: config-repo                             # git仓库地址下的文件夹相对地址，可以配置多个，用,分割。
          username: 1783247180                                             # git仓库的账号
          password: qq19991015*.                                       # git仓库的密码
```

##### 3、启动类

启动类添加`@EnableConfigServer`，激活对配置中心的支持

```java
package com.yy.configserverapplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
@EnableConfigServer
@SpringBootApplication
public class ConfigserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigserverapplicationApplication.class, args);
    }
}
```

到此server端相关配置已经完成

##### 4、测试

首先我们先要测试server端是否可以读取到github上面的配置信息，直接访问：`http://localhost:8020/neo-config/dev`

(上面的文件名是neo-config-dev.properties,所以访问http://localhost:8020/neo-config/dev会将这个文件的配置信息返回)返回信息如下：

```json
{
	"name": "neo-config",
	"profiles": ["dev"],
	"label": null,
	"version": "b094ca6b039fc5ddaaa918855d4b260485f7f0b3",
	"state": null,
	"propertySources": [{
		"name": "https://github.com/1783247180/config-repo/config-repo/neo-config-dev.properties",
		"source": {
			"neo.hello": "hello im dev  update"
		}
	}]
}
```

上述的返回的信息包含了配置文件的位置、版本、配置文件的名称以及配置文件中的具体内容，说明server端已经成功获取了git仓库的配置信息。(也可以试试访问<http://localhost:8020/neo-config/test>)

如果直接查看配置文件中的配置信息可访问：`http://localhost:8020/neo-config-dev.properties`，返回：`neo.hello: hello im dev`

修改配置文件`neo-config-dev.properties`中配置信息为：`neo.hello=hello im dev update`,(记得在github Desktop里面提交)再次在浏览器访问`http://localhost:8020/neo-config-dev.properties`，返回：`neo.hello: hello im dev update`。说明server端会自动读取最新提交的内容

仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：

- /{application}-{profile}.properties

以neo-config-dev.properties为例子，它的application是neo-config，profile是dev。client会根据填写的参数来选择读取对应的配置。

#### client 端

主要展示如何在业务项目中去获取server端的配置信息

##### 1、添加依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>
```

引入spring-boot-starter-web包方便web测试

##### 2、配置文件

需要配置两个配置文件，application.properties和bootstrap.properties

application.properties如下：

```properties
spring.application.name=spring-cloud-config-client
server.port=8021
```

bootstrap.properties如下：

```properties
spring.cloud.config.name=neo-config
spring.cloud.config.profile=dev
spring.cloud.config.uri=http://localhost:8020/
spring.cloud.config.label=master
```

- spring.application.name：对应{application}部分
- spring.cloud.config.profile：对应{profile}部分
- spring.cloud.config.label：对应git的分支。如果配置中心使用的是本地存储，则该参数无用
- spring.cloud.config.uri：配置中心的具体地址
- spring.cloud.config.discovery.service-id：指定配置中心的service-id，便于扩展为高可用配置集群。

> 特别注意：上面这些与spring-cloud相关的属性必须配置在bootstrap.properties中，config部分内容才能被正确加载。因为config的相关配置会先于application.properties，而bootstrap.properties的加载也是先于application.properties。

##### 3、启动类

无需额外添加注解

```java
@SpringBootApplication
public class ConfigClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}
```

##### 4、web测试

使用`@Value`注解来获取server端参数的值

```java
@RestController
class HelloController {
    @Value("${neo.hello}")
    private String hello;

    @RequestMapping("/hello")
    public String from() {
        return this.hello;
    }
}
```

启动项目后访问：`http://localhost:8021/hello`，返回：`hello im dev update`说明已经正确的从server端获取到了参数。到此一个完整的服务端提供配置服务，客户端获取配置参数的例子就完成了。

我们在进行一些小实验，手动修改`neo-config-dev.properties`中配置信息为：`neo.hello=hello im dev update1`提交到github,再次在浏览器访问`http://localhost:8021/hello`，返回：`neo.hello: hello im dev update`，说明获取的信息还是旧的参数，这是为什么呢？因为springboot项目只有在启动的时候才会获取配置文件的值，修改github信息后，client端并没有在次去获取，所以导致这个问题。如何去解决这个问题呢？留到下一章我们在介绍。

#### refresh

现在来解决上一篇的遗留问题，Spring Cloud Config分服务端和客户端，服务端负责将git中存储的配置文件发布成REST接口，客户端可以从服务端REST接口获取配置。但客户端并不能主动感知到配置的变化，从而主动去获取新的配置。客户端如何去主动获取新的配置信息呢，springcloud已经给我们提供了解决方案，每个客户端通过POST方法触发各自的`/refresh`。

修改`spring-cloud-config-client`项目已到达可以refresh的功能。

##### 1、添加依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

增加了`spring-boot-starter-actuator`包，`spring-boot-starter-actuator`是一套监控的功能，可以监控程序在运行时状态，其中就包括`/refresh`的功能。

##### 2、 开启更新机制

需要给加载变量的类上面加载`@RefreshScope`，在客户端执行`/refresh`的时候就会更新此类下面的变量值。

```java
@RestController
@RefreshScope // 使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中。
class HelloController {

    @Value("${neo.hello}")
    private String hello;

    @RequestMapping("/hello")
    public String from() {
        return this.hello;
    }
}
```

##### 3、测试

*springboot 1.5.X 以上默认开通了安全认证，所以需要在配置文件application.yml添加以下配置*

需要在server应用添加：

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

在client应用上添加：

```yml
management:
  endpoints:
    web:
      exposure:
        include: refresh
```

OK 这样就改造完了，打开cmd执行curl -X POST http://localhost:8002/refresh` 就会更新修改后的配置文件。

我们再次来测试，首先访问`http://localhost:8021/hello`，返回：`hello im dev`，我将库中的值修改为`hello im dev update`。在win上面打开cmd执行`curl -X POST http://localhost:8002/refresh`，返回`["neo.hello"]`说明已经更新了`neo.hello`的值。我们再次访问`http://localhost:8021/hello`，返回：`hello im dev update`,客户端已经得到了最新的值。

每次手动刷新客户端也很麻烦，有没有什么办法只要提交代码就自动调用客户端来更新呢，SpringCloud Bus是一个好的办法(后面会介绍)

#### 配置中心服务化与高可用

高可用解决的问题:

一个服务做了负载均衡(2台),其中一个挂掉了,会识别出有一个挂掉了,就单独使用另一个

中心服务化解决的问题:

客户端都是直接调用配置中心的server端来获取配置文件信息。这样就存在了一个问题，客户端和服务端的耦合性太高，如果server端要做集群，客户端只能通过原始的方式来路由，server端改变IP地址的时候，客户端也需要修改配置，不符合springcloud服务治理的理念。springcloud提供了这样的解决方案，我们只需要将server端当做一个服务注册到eureka中，client端去eureka中去获取配置中心server端的服务既可

##### server端改造

###### 1、添加依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-config-server</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
</dependencies>
```

需要多引入spring-cloud-starter-netflix-eureka-client包，来添加对eureka的支持。

###### 2、配置文件

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/   ## 注册中心eurka地址
server:
  port: 8020
spring:
  application:
    name: spring-cloud-config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/1783247180/config-repo     # 配置git仓库的地址
          search-paths: config-repo                             # git仓库地址下的文件夹相对地址，可以配置多个，用,分割。
          username: 1783247180                                             # git仓库的账号
          password: qq19991015*.                                       # git仓库的密码
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

增加了eureka注册中心的配置

###### 3、启动类

启动类添加`@EnableDiscoveryClient`激活对注册中心的支持

```java
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
```

这样server端的改造就完成了。先启动eureka注册中心，在启动server端，在浏览器中访问：`http://localhost:8000/`就会看到server端已经注册了到注册中心了。

![](F:\Typora\EasyDevelopment\SpringCloud\img\eureka-config01.jpg)

按照上篇的测试步骤对server端进行测试服务正常。

##### client改造

###### 1、添加依赖

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>
```

需要多引入包spring-cloud-starter-netflix-eureka-client，来添加对eureka的支持。

###### 2、配置文件

```properties
#application.properties中
spring.application.name=spring-cloud-config-client
server.port=8021
#bootstrap.properties中
spring.cloud.config.name=neo-config
spring.cloud.config.profile=dev
spring.cloud.config.label=master
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=spring-cloud-config-server
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
```

```yml
#application.yml中
management:
  endpoints:
    web:
      exposure:
        include: refresh
```

主要是去掉了`spring.cloud.config.uri`直接指向server端地址的配置，增加了最后的三个配置：

- `spring.cloud.config.discovery.enabled` ：开启Config服务发现支持
- `spring.cloud.config.discovery.serviceId` ：指定server端的name,也就是server端`spring.application.name`的值
- `eureka.client.serviceUrl.defaultZone` ：指向注册中心的地址

这三个配置文件都需要放到`bootstrap.properties`的配置中

###### 3、启动类

启动类添加`@EnableDiscoveryClient`激活对配置中心的支持

```java
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}
```

启动client端，在浏览器中访问：`http://localhost:8761/` 就会看到server端和client端都已经注册了到注册中心了。

![](F:\Typora\EasyDevelopment\SpringCloud\img\eureka-config02.jpg)

为什么我把提供配置中心的服务都停了，把注册中心都停了，客户端还可以请求获取数据?

客户端会缓存，服务端挂了不影响，客户端只要不重启都可以用缓存