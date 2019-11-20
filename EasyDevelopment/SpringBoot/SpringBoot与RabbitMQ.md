<https://blog.csdn.net/hellozpc/article/details/81436980#8SpringbootRabbitMQ_1267>

主要学习使用简单队列和订阅模式

# SpringBoot消息

订单处理，就可以由前端应用将订单信息放到队列，后端应用从队列里依次获得消息处理，高峰时的大量订单可以积压在队列里慢慢处理掉。由于同步通常意味着阻塞，而大量线程的阻塞会降低计算机的性能。

## 核心步骤

1. 在docker里面下载docker pull rabbitmq:3.7-management

   使用docker ps查看运行时的docker

   (第一次安装)开始运行:docker run -d -p 5672:5672 -p 15672:15672 --name myrabbitmq   rabbitmq:3.7-management

   如果不是第一次使用:直接运行docker容器

   查看所有容器docker ps -a

   ![1560935199547](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560935199547.png)

   可以看到rabbitmq对应的容器id是eac50dfdbee6

   直接运行docker start eac50dfdbee6

   ![1560935293159](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560935293159.png)

   再查看运行中的容器docker ps

   ![1560935324844](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560935324844.png)

   在谷歌访问192.168.40.133:15672输入用户名和密码,默认都是guest,进行exchanges与queue的配置(java代码里面也可以创建)

   ### 消息基础

   1. 消息服务中重要概念：
      消息代理（message broker）和目的地（destination）
      当消息发送者发送消息以后，将由消息代理接管，消息代理保证消息传递到指定目的地。
      消息队列主要有两种形式的目的地
      队列（queue）：点对点消息通信（point-to-point）
      主题（topic）：发布（publish）/订阅（subscribe）消息通信

   2. RabbitMQ简介：
      RabbitMQ是一个由erlang开发的AMQP(Advanved Message Queue Protocol)的开源实现。
      核心概念
      Message
      消息，消息是不具名的，它由消息头和消息体组成。消息体是不透明的，而消息头则由一系列的可选属性组成，这些属性包括routing-key（路由键）、priority（相对于其他消息的优先权）、delivery-mode（指出该消息可能需要持久性存储）等。
      Publisher
      消息的生产者，也是一个向交换器发布消息的客户端应用程序。
      Exchange
      交换器，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。
      Exchange有4种类型：direct(默认)，fanout, topic, 和headers，不同类型的Exchange转发消息的策略有所区别

      Queue
      消息队列，用来保存消息直到发送给消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个队列。消息一直在队列里面，等待消费者连接到这个队列将其取走。
      Binding
      绑定，用于消息队列和交换器之间的关联。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则，所以可以将交换器理解成一个由绑定构成的路由表。
      Exchange 和Queue的绑定可以是多对多的关系。
      Connection
      网络连接，比如一个TCP连接。
      Channel
      信道，多路复用连接中的一条独立的双向数据流通道。信道是建立在真实的TCP连接内的虚拟连接，AMQP 命令都是通过信道发出去的，不管是发布消息、订阅队列还是接收消息，这些动作都是通过信道完成。因为对于操作系统来说建立和销毁 TCP 都是非常昂贵的开销，所以引入了信道的概念，以复用一条 TCP 连接。

      Consumer
      消息的消费者，表示一个从消息队列中取得消息的客户端应用程序。
      Virtual Host
      虚拟主机，表示一批交换器、消息队列和相关对象。虚拟主机是共享相同的身份认证和加密环境的独立服务器域。每个 vhost 本质上就是一个 mini 版的 RabbitMQ 服务器，拥有自己的队列、交换器、绑定和权限机制。vhost 是 AMQP 概念的基础，必须在连接时指定，RabbitMQ 默认的 vhost 是 / 。
      Broker
      表示消息队列服务器实体

   ### 两种主要的消息模式

   1. 简单队列

      ![img](https://img-blog.csdn.net/20180805224818627?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pwY2FuZHpoag==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

      P：消息的生产者
      C：消息的消费者
      红色：队列

      生产者将消息发送到队列，消费者从队列中获取消息。

       SpringBoot与rabbitmq(简单队列)步骤简介

      1.配置pom文件，主要是添加spring-boot-starter-amqp的支持

      ```xml
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-amqp</artifactId>
      </dependency>
      ```

      

      2.配置application.properties文件配置rabbitmq的安装地址、端口以及账户信息

      ```properties
      spring.rabbitmq.host=127.0.0.1
      spring.rabbitmq.port=5672
      spring.rabbitmq.username=admin
      spring.rabbitmq.password=admin
      ```

      

      3.队列配置类

      ```java
      import org.springframework.amqp.core.Queue;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      
      @Configuration
      public class RabbitConfig {
          @Bean
          public Queue queue() {
              return new Queue("q_hello");
          }
      }
      ```

      

      4.发送者

      ```java
      import org.springframework.amqp.core.AmqpTemplate;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Component;
      
      import java.text.SimpleDateFormat;
      import java.util.Date;
      
      @Component
      public class HelloSender {
          @Autowired
          private AmqpTemplate rabbitTemplate;
      
          public void send() {
              String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//24小时制
              String context = "hello " + date;
              System.out.println("Sender : " + context);
              //简单对列的情况下routingKey即为Q名
              this.rabbitTemplate.convertAndSend("q_hello", context);
          }
      }
      ```

      

      5.接收者

      ```java
      import org.springframework.amqp.rabbit.annotation.RabbitHandler;
      import org.springframework.amqp.rabbit.annotation.RabbitListener;
      import org.springframework.stereotype.Component;
      
      @Component
      //接收者会监听队列q_hello,如果队列中有数据就会读出来
      @RabbitListener(queues = "q_hello")
      public class HelloReceiver {
      
          @RabbitHandler
          public void process(String hello) {
              System.out.println("Receiver  : " + hello);
          }
      }
      ```

      

      6.测试

      ```java
      import org.junit.Test;
      import org.junit.runner.RunWith;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.test.context.junit4.SpringRunner;
      
      @RunWith(SpringRunner.class)
      @SpringBootTest
      public class RabbitMqHelloTest {
      
          @Autowired
          private HelloSender helloSender;
      
          @Test
          public void hello() throws Exception {
              helloSender.send();
          }
      }
      ```

      

   2. 订阅模式

      1、1个生产者，多个消费者
      2、每一个消费者都有自己的一个队列
      3、生产者没有将消息直接发送到队列，而是发送到了交换机
      4、每个队列都要绑定到交换机
      5、生产者发送的消息，经过交换机，到达队列，实现，一个消息被多个消费者获取的目的

      注意：一个消费者队列可以有多个消费者实例，只有其中一个消费者实例会消费

      ![img](https://img-blog.csdn.net/20180805225218886?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pwY2FuZHpoag==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

      SpringBoot与rabbitmq(订阅模式)步骤简介

      1.配置pom文件，主要是添加spring-boot-starter-amqp的支持

      ```xml
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-amqp</artifactId>
      </dependency>
      ```

      

      2.配置application.properties文件配置rabbitmq的安装地址、端口以及账户信息

      ```properties
      spring.rabbitmq.host=127.0.0.1
      spring.rabbitmq.port=5672
      spring.rabbitmq.username=admin
      spring.rabbitmq.password=admin
      ```

      3.配置队列，绑定交换机

      ```java
      import org.springframework.amqp.core.Binding;
      import org.springframework.amqp.core.BindingBuilder;
      import org.springframework.amqp.core.FanoutExchange;
      import org.springframework.amqp.core.Queue;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      
      @Configuration
      public class FanoutRabbitConfig {
      
          @Bean
          public Queue aMessage() {
              return new Queue("q_fanout_A");
          }
      
          @Bean
          public Queue bMessage() {
              return new Queue("q_fanout_B");
          }
      
          @Bean
          public Queue cMessage() {
              return new Queue("q_fanout_C");
          }
      
          @Bean
          FanoutExchange fanoutExchange() {
              return new FanoutExchange("mybootfanoutExchange");
          }
      
          @Bean
          Binding bindingExchangeA(Queue aMessage, FanoutExchange fanoutExchange) {
              return BindingBuilder.bind(aMessage).to(fanoutExchange);
          }
      
          @Bean
          Binding bindingExchangeB(Queue bMessage, FanoutExchange fanoutExchange) {
              return BindingBuilder.bind(bMessage).to(fanoutExchange);
          }
      
          @Bean
          Binding bindingExchangeC(Queue cMessage, FanoutExchange fanoutExchange) {
              return BindingBuilder.bind(cMessage).to(fanoutExchange);
          }
      }
      ```

      4.创建3个消费者

      ```java
      package com.zpc.rabbitmq.fanout;
      
      import org.springframework.amqp.rabbit.annotation.RabbitHandler;
      import org.springframework.amqp.rabbit.annotation.RabbitListener;
      import org.springframework.stereotype.Component;
      
      @Component
      @RabbitListener(queues = "q_fanout_A")
      public class ReceiverA {
      
          @RabbitHandler
          public void process(String hello) {
              System.out.println("AReceiver  : " + hello + "/n");
          }
      }
      ```

      ```java
      package com.zpc.rabbitmq.fanout;
      
      import org.springframework.amqp.rabbit.annotation.RabbitHandler;
      import org.springframework.amqp.rabbit.annotation.RabbitListener;
      import org.springframework.stereotype.Component;
      
      @Component
      @RabbitListener(queues = "q_fanout_B")
      public class ReceiverB {
      
          @RabbitHandler
          public void process(String hello) {
              System.out.println("BReceiver  : " + hello + "/n");
          }
      }
      ```

      ```java
      package com.zpc.rabbitmq.fanout;
      
      import org.springframework.amqp.rabbit.annotation.RabbitHandler;
      import org.springframework.amqp.rabbit.annotation.RabbitListener;
      import org.springframework.stereotype.Component;
      
      @Component
      @RabbitListener(queues = "q_fanout_C")
      public class ReceiverC {
      
          @RabbitHandler
          public void process(String hello) {
              System.out.println("CReceiver  : " + hello + "/n");
          }
      }
      ```

      5.生产者

      ```java
      import org.springframework.amqp.core.AmqpTemplate;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Component;
      
      @Component
      public class MsgSenderFanout {
      
          @Autowired
          private AmqpTemplate rabbitTemplate;
      
          public void send() {
              String context = "hi, fanout msg ";
              System.out.println("Sender : " + context);
              //第一个参数是mybootfanoutExchange交换机(生产者没有将消息直接发送到队列，而是发送到了交换机,每个队列都要绑定到交换机),第二个参数是队列名""代表绑定该交换机的所有队列
              this.rabbitTemplate.convertAndSend("mybootfanoutExchange","", context);
          }
      }
      ```

      6.测试

      ```java
      import org.junit.Test;
      import org.junit.runner.RunWith;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.test.context.junit4.SpringRunner;
      
      @RunWith(SpringRunner.class)
      @SpringBootTest
      public class RabbitFanoutTest {
      
          @Autowired
          private MsgSenderFanout msgSender;
      
          @Test
          public void send1() throws Exception {
              msgSender.send();
          }
      }
      ```

      

   ![img](https://img-blog.csdn.net/20180805224641957?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pwY2FuZHpoag==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)