# SpringBoot与cache

## 核心步骤

1. 使用前准备

   + 创建springboot的时候选择web,mysql,mybatis模块
   + 导入数据库文件创建出department和employee表
   + 创建javaBean封装数据
   + 整合Mybatis操作数据库(扫描com.atguigu.cache.mapper和配置数据源)

   ```java
   @MapperScan("com.atguigu.cache.mapper")
   @SpringBootApplication
   @EnableCaching
   public class Springboot2Application {
       public static void main(String[] args) {
           SpringApplication.run(Springboot2Application.class, args);
       }
   }
   ```

   ```properties
   application.properties中:
   spring.datasource.url=jdbc:mysql://localhost:3306/jdbc?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   #spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver(默认可以检测出驱动,无需手写)
   ```

2. 快速开始

   + 开启基于注解的缓存 @EnableCaching(在上面Springboot2Application已经写了)
   + 在service层方法上面标注缓存注解即可(@Cacheable,@CacheEvict,@CachePut),默认会将方法参数的值作为key,方法的返回值为value存放在缓存中

3. 注解的使用

   + @Cacheable(用于查询)：

     + 使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件

     + 去Cache中查找缓存的内容，使用一个key，默认就是方法的参数； key是按照某种策略生成的；默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key；
         SimpleKeyGenerator生成key的默认策略；
           如果@Cacheable没有key参数；key=new SimpleKey()；
           如果@Cacheable有一个key参数：key=参数的值
           如果@Cacheable有多个key参数：key=new SimpleKey(params)；

     + 自定义keyGenerator

       ```java
       package com.atguigu.cache.config;
       import org.springframework.cache.interceptor.KeyGenerator;
       import org.springframework.context.annotation.Bean;
       import org.springframework.context.annotation.Configuration;
       import java.lang.reflect.Method;
       import java.util.Arrays;
       @Configuration
       public class MyCacheConfig {
           @Bean("myKeyGenerator")
           public KeyGenerator keyGenerator(){
               return new KeyGenerator(){
                   @Override
                   public Object generate(Object target, Method method, Object... params) {
                       return method.getName()+"["+ Arrays.asList(params).toString()+"]";
                   }
               };
           }
       }
       ```

       ```java
       @Cacheable(value = {"emp"},keyGenerator = "myKeyGenerator")
       public Employee getEmp(Integer id){
           System.out.println("查询"+id+"号员工");
           Employee emp = employeeMapper.getEmpById(id);
           return emp;
       }
       ```

     + 几个属性

        cacheNames/value：指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存；
        key：缓存数据使用的key；可以用它来指定。默认是使用方法参数的值  1-方法的返回值
        编写SpEL； #id;参数id的值   #a0  #p0  #root.args[0]
        keyGenerator：key的生成器；可以自己指定key的生成器的组件id
            key/keyGenerator：二选一使用;(不要同时使用)
        cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
             condition：指定符合条件的情况下才缓存；
             condition = "#id>0"
             condition = "#a0>1"：第一个参数的值》1的时候才进行缓存
       unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；可以获取到结果进行判断
              unless = "#result == null"
              unless = "#a0==2":如果第一个参数的值是2，结果不缓存；
       sync：是否使用异步模式

   + @CachePut：既调用方法，又更新缓存数据；同步更新缓存(用于update与add操作)

   + @CacheEvict：缓存清除(用于删除操作)

```java
一、搭建基本环境
* 1、导入数据库文件 创建出department和employee表
* 2、创建javaBean封装数据
* 3、整合MyBatis操作数据库
*     1.配置数据源信息
*     2.使用注解版的MyBatis；
*        1）、@MapperScan指定需要扫描的mapper接口所在的包
* 二、快速体验缓存
*     步骤：
*        1、开启基于注解的缓存 @EnableCaching
*        2、标注缓存注解即可
*           @Cacheable
*           @CacheEvict
*           @CachePut
* 默认使用的是ConcurrentMapCacheManager==ConcurrentMapCache；将数据保存在   ConcurrentMap<Object, Object>中
* 开发中使用缓存中间件；redis、memcached、ehcache；
@MapperScan("com.atguigu.cache.mapper")
@SpringBootApplication
@EnableCaching
public class Springboot2Application {
    public static void main(String[] args) {
        SpringApplication.run(Springboot2Application.class, args);
    }

}
```

```java
package com.atguigu.cache.service;
import com.atguigu.cache.bean.Employee;
import com.atguigu.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
@CacheConfig(cacheNames="emp"/*,cacheManager = "employeeCacheManager"*/) //抽取缓存的公共配置
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    /**
     * 将方法的运行结果进行缓存；以后再要相同的数据，直接从缓存中获取，不用调用方法；
     * CacheManager管理多个Cache组件的，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字；
     *
     *
     * 原理：
     *   1、自动配置类；CacheAutoConfiguration
     *   2、缓存的配置类
     *   org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     *   org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration【默认】
     *   org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *   3、哪个配置类默认生效：SimpleCacheConfiguration；
     *
     *   4、给容器中注册了一个CacheManager：ConcurrentMapCacheManager
     *   5、可以获取和创建ConcurrentMapCache类型的缓存组件；他的作用将数据保存在ConcurrentMap中；
     *
     *   运行流程：
     *   @Cacheable：
     *   1、方法运行之前，先去查询Cache（缓存组件），按照cacheNames指定的名字获取；
     *      （CacheManager先获取相应的缓存），第一次获取缓存如果没有Cache组件会自动创建。
     *   2、去Cache中查找缓存的内容，使用一个key，默认就是方法的参数；
     *      key是按照某种策略生成的；默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key；
     *          SimpleKeyGenerator生成key的默认策略；
     *                  如果没有参数；key=new SimpleKey()；
     *                  如果有一个参数：key=参数的值
     *                  如果有多个参数：key=new SimpleKey(params)；
     *   3、没有查到缓存就调用目标方法；
     *   4、将目标方法返回的结果，放进缓存中
     *
     *   @Cacheable标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存，
     *   如果没有就运行方法并将结果放入缓存；以后再来调用就可以直接使用缓存中的数据；
     *
     *   核心：
     *      1）、使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件
     *      2）、key使用keyGenerator生成的，默认是SimpleKeyGenerator
     *
     *
     *   几个属性：
     *      cacheNames/value：指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存；
     *
     *      key：缓存数据使用的key；可以用它来指定。默认是使用方法参数的值  1-方法的返回值
     *              编写SpEL； #i d;参数id的值   #a0  #p0  #root.args[0]
     *              getEmp[2]
     *
     *      keyGenerator：key的生成器；可以自己指定key的生成器的组件id
     *              key/keyGenerator：二选一使用;
     *
     *
     *      cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
     *
     *      condition：指定符合条件的情况下才缓存；
     *              ,condition = "#id>0"
     *          condition = "#a0>1"：第一个参数的值》1的时候才进行缓存
     *
     *      unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；可以获取到结果进行判断
     *              unless = "#result == null"
     *              unless = "#a0==2":如果第一个参数的值是2，结果不缓存；
     *      sync：是否使用异步模式
     * @param id
     * @return
     *
     */
    @Cacheable(value = {"emp"}/*,keyGenerator = "myKeyGenerator",condition = "#a0>1",unless = "#a0==2"*/)
    public Employee getEmp(Integer id){
        System.out.println("查询"+id+"号员工");
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }
    /**
     * @CachePut：既调用方法，又更新缓存数据；同步更新缓存
     * 修改了数据库的某个数据，同时更新缓存；
     * 运行时机：
     *  1、先调用目标方法
     *  2、将目标方法的结果缓存起来
     *
     * 测试步骤：
     *  1、查询1号员工；查到的结果会放在缓存中；
     *          key：1  value：lastName：张三
     *  2、以后查询还是之前的结果
     *  3、更新1号员工；【lastName:zhangsan；gender:0】
     *          将方法的返回值也放进缓存了；
     *          key：传入的employee对象  值：返回的employee对象；
     *  4、查询1号员工？
     *      应该是更新后的员工；
     *          key = "#employee.id":使用传入的参数的员工id；
     *          key = "#result.id"：使用返回后的id
     *             @Cacheable的key是不能用#result
     *      为什么是没更新前的？【1号员工没有在缓存中更新】
     */
    @CachePut(/*value = "emp",*/key = "#result.id")
    public Employee updateEmp(Employee employee){
        System.out.println("updateEmp:"+employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }
    /**
     * @CacheEvict：缓存清除
     *  key：指定要清除的数据
     *  allEntries = true：指定清除这个缓存(emp)中所有的数据
     *  beforeInvocation = false：缓存的清除是否在方法之前执行
     *      默认代表缓存清除操作是在方法执行之后执行;如果出现异常缓存就不会清除
     *
     *  beforeInvocation = true：
     *      代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     */
    @CacheEvict(value="emp",beforeInvocation = true/*key = "#id",*/)
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp:"+id);
        //employeeMapper.deleteEmpById(id);
        int i = 10/0;
    }
    // @Caching 定义复杂的缓存规则
    @Caching(
         cacheable = {
             @Cacheable(/*value="emp",*/key = "#lastName")
         },
         put = {
             @CachePut(/*value="emp",*/key = "#result.id"),
             @CachePut(/*value="emp",*/key = "#result.email")
         }
    )
    public Employee getEmpByLastName(String lastName){
        return employeeMapper.getEmpByLastName(lastName);
    }
}
```

# SpringBoot与redis

## 核心步骤

1. 使用前准备

   * 打开redis

   * 配置redis(spring.redis.host=127.0.0.1)

     如果是在虚拟机中使用:

     docker pull redis

     docker run -d -p 6379:6379 --name myredis redis(镜像名)

   * 创建springboot的时候选择web,mysql,mybatis,redis模块(默认使用RedisCacheConfiguration)

   * 导入数据库文件创建出department和employee表

   * 创建javaBean封装数据

   * 整合Mybatis操作数据库(扫描com.atguigu.cache.mapper和配置数据源)

2. 快速使用

   ```java
   package com.atguigu.cache.springboot2;
   import com.atguigu.cache.bean.Employee;
   import com.atguigu.cache.mapper.EmployeeMapper;
   import org.junit.Test;
   import org.junit.runner.RunWith;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.test.context.junit4.SpringRunner;
   @RunWith(SpringRunner.class)
   @SpringBootTest
   public class Springboot2cacheApplicationTests {
       @Autowired
       EmployeeMapper employeeMapper;
       //导入了redis的start就可以将redisTemplate和stringRedisTemplate自动注入
       @Autowired
       RedisTemplate redisTemplate自动注入; //操作k-v都是对象
       @Autowired
       StringRedisTemplate stringRedisTemplate;  //操作k-v都是字符串的,因为这种类型比较多所以redis封装了一个
       @Test
       public void contextLoads() {
           Employee empById = employeeMapper.getEmpById(1);
           //默认如果保存对象，使用jdk序列化机制，序列化后的数据保存到redis中,JdkSerializationRedisSerializer是默认序列化方式
           //redisTemplate.opsForValue().set("emp-01",empById);
   		//1、将数据以json的方式保存
   		 //(1)自己将对象转为json
   		 //(2)redisTemplate默认的序列化规则；改变默认的序列化规则；自己写一个redisTemplate
           redisTemplate.opsForValue().set("emp-01",empById);
       }
   }
   ```

3. 序列化方式的研究

   1. JdkSerializationRedisSerializer是默认序列化方式，是最简单的也是最安全的，只要实现了Serializer接口，实体类型，集合，Map等都能序列化与反序列化，但缺陷是序列化数据很多，会对redis造成更大压力，且可读性和跨平台基本无法实现

   2. Jackson2JsonRedisSerializer用的是json的序列化方式，能解决JdkSerializationRedisSerializer带来的缺陷，但复杂类型(集合,泛型,实体包装类)反序列化时会报错，且Jackson2JsonRedisSerializer需要指明序列化的类Class，这代表一个实体类就有一个RedisCacheManager，代码冗余

   3. 最后查看源码，发现RedisSerializer（Jackson2JsonRedisSerializer也实现此接口）的实现类中有一个GenericJackson2JsonRedisSerializer，此类不用需要指明序列化的类，写一个RedisCacheManager即可，代码更精简，复杂类型(集合,泛型)反序列化时不会报错，查看redis数据可以发现实现原理是在json数据中放一个@class属性，指定了类的全路径包名，方便反序列化，所以在内存占用上高一点点，但是反序列化性能自测要比Jackson2JsonRedisSerializer高（有兴趣的老哥也可以自己试试性能差别），空间换时间，当然也因为此特性，项目一部分的结构（实体全类名）会json数据中体现。
      注：经过反复考虑，目前使用了GenericJackson2JsonRedisSerializer作为序列化方式

4. 改变序列化方式

   1. 使用Jackson2JsonRedisSerializer序列化

```java
package com.atguigu.cache.config;
import com.atguigu.cache.bean.Department;
import com.atguigu.cache.bean.Employee;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.net.UnknownHostException;
import java.time.Duration;
@Configuration
public class MyRedisConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofDays(1))
                        .disableCachingNullValues()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer
                                (new Jackson2JsonRedisSerializer<Employee>(Employee.class)));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}
```

2. 使用GenericJackson2JsonRedisSerializer序列化

```java
//springboot检查到了缓存管理器,就不会使用默认的缓存管理器了,会使用我们在下面创建的缓存管理器
//有多个CacheManager需要用注解@Primary  //将某个缓存管理器作为默认的
@Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofDays(1))
                        .disableCachingNullValues()
.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                                GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
```

测试

```java
//可以在注解中指定cacheManager,cacheManager = "cacheManager",这个cacheManager是上面的方法名
@Cacheable(cacheNames = "dept")
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department = departmentMapper.getDeptById(id);
        return department;
    }
```

三、整合redis作为缓存
 * Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。

 * 1、安装redis：使用docker；

 * 2、引入redis的starter

 * 3、配置redis

 * 4、测试缓存

 * 原理：CacheManager===Cache 缓存组件来实际给缓存中存取数据

 * 1）、引入redis的starter，容器中保存的是 RedisCacheManager；

 * 2）、RedisCacheManager 帮我们创建 RedisCache 来作为缓存组件；RedisCache通过操作redis缓存数据的

 * 3）、默认保存数据 k-v 都是Object；利用序列化保存；如何保存为json

 * 1、引入了redis的starter，cacheManager变为 RedisCacheManager；

 * 2、默认创建的 RedisCacheManager 操作redis的时候使用的是 RedisTemplate<Object, Object>

 * 3、RedisTemplate<Object, Object> 是 默认使用jdk的序列化机制

 * 4）、自定义CacheManager；
     
     ```java
     @MapperScan("com.atguigu.cache.mapper")
      @SpringBootApplication
      @EnableCaching
      public class Springboot2Application {
      public static void main(String[] args) {
          SpringApplication.run(Springboot2Application.class, args);
      }
    }
    ```
    
    

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

2. 打开idea的springboot模块选中web与rabbitmq

3. 原理

   ```java
   自动配置
   * 1.RabbitAutoConfiguration
   * 2.有自动配置了连接ConnectionFactory
   * 3.RabbitProperties封装了RabbitMQ的配置(application.properties里面spring.rabbitmq)
   * 4.RabbitTemplate:给RabbitMQ发送和接受消息
   * 5.AmqpAdmin:RabbitMQ系统管理功能组件
   ```

4. 在application.properties中写:

   ```properties
   spring.rabbitmq.host=192.168.40.133
   spring.rabbitmq.username=guest
   spring.rabbitmq.password=guest
   spring.rabbitmq.port=5672
   #spring.rabbitmq.virtual-host=不写默认是/
   ```

​      在test里面

```java
package com.atguigu.amqp;
import com.atguigu.amqp.bean.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02AmqpApplicationTests {
@Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
	AmqpAdmin amqpAdmin;
	@Test
	public void createExchange(){
//		amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
//		System.out.println("创建完成");
        //下面的true代表是持久化的,就是重启也不会删除
//		amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));
		//创建绑定规则,第一个参数是指定绑定的目的地,第二个参数就是第一个参数的类型是队列还是交换器,第三个参数是交换器,第四个参数是Routing key,第五个参数就是绑定时的附加参数
//		amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE,"amqpadmin.exchange","amqp.haha",null));
        //删除操作
		//amqpAdmin.de
	}
//单播点对点,发送数据
    @Test
    public void contextLoads() {
        //Message需要自己构造一个;定义消息体与消息头
       // rabbitTemplate.send(exchage,routeKey,message);
        //object默认当成消息体,只需要传入要发送的对象,自动序列化发送给rabbitmq
        //rabbitTemplate.convertAndSend(exchage,routeKey,object);
        Map<String, Object> map = new HashMap<>();
        map.put("msg","这是第一条消息");
        map.put("data", Arrays.asList("helloworld",123,true));
        //进入http://192.168.40.133:15672/#/queues中的atguigu.news点进去找到get Message
        // map被默认序列化后发送出去,exchange.direct交换器里面配置了路由key与序列的映射关系,atguigu.news是路由key,会将atguigu.news对应的queue找到,并且把map放在对应的queue里面
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",
        map);
    }
    //接收数据
@Test
    public void receive(){
    Object o = rabbitTemplate.receiveAndConvert("atguigu.news");
    System.out.println(o.getClass());
    System.out.println(o);
}
    /**
     * 广播
     */
    @Test
    public void sendMsg(){
        //因为
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("红楼梦","曹雪芹"));
    }
}
```

在config包下

```java
//将存放在队列queue中的数据的序列化规则改成Jackson2JsonMessageConverter();而不是默认的SimpleMessageConverter();
package com.atguigu.amqp.config;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MyAMQPConfig {
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

```

主配置类

```java
package com.atguigu.amqp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
*
* 自动配置
* 1.RabbitAutoConfiguration
* 2.有自动配置了连接ConnectionFactory
* 3.RabbitProperties封装了RabbitMQ的配置(application.properties里面spring.rabbitmq)
* 4.RabbitTemplate:给RabbitMQ发送和接受消息
* 5、 AmqpAdmin ： RabbitMQ系统管理功能组件;
 *     AmqpAdmin：创建和删除 Queue，Exchange，Binding
 *  6、@EnableRabbit +  @RabbitListener 监听消息队列的内容
* */
@SpringBootApplication
public class Springboot02AmqpApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot02AmqpApplication.class, args);
    }
}
```

service里面

```java
package com.atguigu.amqp.service;
import com.atguigu.amqp.bean.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
@Service
public class BookService {
    @RabbitListener(queues = "atguigu.news")
    public void receive(Book book){
        System.out.println("收到消息："+book);
    }
    @RabbitListener(queues = "atguigu")
    public void receive02(Message message){
        System.out.println(message.getBody());
        System.out.println(message.getMessageProperties());
    }
}
```

pojo

```java
package com.atguigu.amqp.bean;
public class Book {
    private String bookName;
    private String author;
    public Book() {
    }
    public Book(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

```

# ES

安装在SpringBoot篇的docker中

![1558669600583](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1558669600583.png)

对于雇员目录，我们将做如下操作：

- 每个雇员索引一个文档，包含该雇员的所有信息。
- 每个文档都将是 `employee` *类型* 。
- 该类型位于 *索引* `megacorp` 内。
- 该索引保存在我们的 Elasticsearch 集群中。

实践中这非常简单（尽管看起来有很多步骤），我们可以通过一条命令完成所有这些动作：

```js
PUT /megacorp/employee/1
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```

注意，路径 `/megacorp/employee/1` 包含了三部分的信息：

- `megacorp`

  索引名称

- `employee`

  类型名称

- `1`

  特定雇员的ID

请求体 —— JSON 文档 —— 包含了这位员工的所有详细信息，他的名字叫 John Smith ，今年 25 岁，喜欢攀岩。

搜索员工

https://www.elastic.co/guide/cn/elasticsearch/guide/current/_search_lite.html

## SpringBoot与ElasticSearch整合

```xml
<!--SpringBoot默认使用SpringData Elasticsearch模块进行操作-->
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

SpringBoot默认支持两种技术来和ES交互

1. Jest(默认不生效)

需要导入jest工具包(io.searchbox.client.JestClient)虚拟机装的版本是5.1.1,所以pom.xml需要5开头的版本

给ES中添加数据的步骤

+ 找到springboot的自动配置包

+ 找到elasticsearch.jest包,看下面的JestProperties其中的配置,其中spring.elasticsearch.jest.uris默认配置是本机所以需要修改spring.elasticsearch.jest.uris=http://192.168.40.133:9200

+ 写一个article类,id上面加上注解@JestId

+ 在service里面创建article的对象并且自动注入

  @Autowired

  JestClient jestClient;

+ 构建索引 new Index.Builder(article).index("atguigu").type("news").build();

+ 执行索引

  try {
      //执行
      jestClient.execute(build);
  } catch (IOException e) {
      e.printStackTrace();
  }

+ 访问http://192.168.40.133:9200/atguigu/news/1

搜索ES中的数据

```java
@Test
    public void search(){
        //搜索
        String json="{\n" +
                "    \"query\" : {\n" +
                "        \"match_phrase\" : {\n" +
                "            \"content\" : \"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
    Search search = new Search.Builder(json).addIndex("atguigu").addType("news").build();
    try {
        SearchResult execute = jestClient.execute(search);
        System.out.println("----"+execute.getJsonString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

2. SpringData ElasticSearch

   - Client节点信息clusterNodes;clusterName
   - ElasticsearchTemplate 操作es
   - 编写一个ElasticsearchRepository的子接口来操作ES

   