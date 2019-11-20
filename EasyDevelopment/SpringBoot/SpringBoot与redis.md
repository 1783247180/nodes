# SpringBoot与redis

步骤简介

1.在主机或者虚拟机中打开redis

2.SpringBoot项目中加入依赖

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

3.在application.properties中配置

```properties
spring.redis.host=127.0.0.1
```

4.加入一个Redis配置类,并且写一个缓存管理器方法

```java
package com.yy.sb_redis.config;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;
@Configuration
public class MyRedisConfig {
    //springboot检查到了缓存管理器,就不会使用默认的缓存管理器了,会使用我们在下面创建的缓存管理器
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofDays(1))
                        .disableCachingNullValues()             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                                GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}
```

5.主配置类中使用注解@EnableCaching开启redis的注解

```java
package com.yy.sb_redis;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@MapperScan("com.yy.sb_redis.mapper")
@SpringBootApplication
@EnableCaching
public class SbRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(SbRedisApplication.class, args);
    }

}
```

6.service中使用注解@Cacheable(cacheNames = "department",cacheManager = "cacheManager")

```java
package com.yy.sb_redis.service;
import com.yy.sb_redis.bean.Department;
import com.yy.sb_redis.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;
    /**
     *方法的返回值会存储进cache中
     cacheManager = "cacheManager",这个cacheManager是MyRedisConfig类里面的方法名
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "department",cacheManager = "cacheManager")
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department =  departmentMapper.getDeptById(id);
        return department;
    }
}
```



## 快速开始步骤

1. 使用前准备

   - 打开redis

   - 配置redis(spring.redis.host=127.0.0.1)

     如果是在虚拟机中使用:

     docker pull redis

     docker run -d -p 6379:6379 --name myredis redis(镜像名)

   - 创建springboot的时候选择web,mysql,mybatis,redis模块(默认使用RedisCacheConfiguration)

   - 导入数据库文件创建出department和employee表

   - 创建javaBean封装数据

   - 整合Mybatis操作数据库(扫描com.atguigu.cache.mapper和配置数据源)

2. 快速使用(只是个例子,项目中使用的在下面)

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

1. 使用GenericJackson2JsonRedisSerializer序列化

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

- Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。
- 1、安装redis：使用docker；
- 2、引入redis的starter
- 3、配置redis
- 4、测试缓存
- 原理：CacheManager===Cache 缓存组件来实际给缓存中存取数据
- 1）、引入redis的starter，容器中保存的是 RedisCacheManager；
- 2）、RedisCacheManager 帮我们创建 RedisCache 来作为缓存组件；RedisCache通过操作redis缓存数据的
- 3）、默认保存数据 k-v 都是Object；利用序列化保存；如何保存为json
- 1、引入了redis的starter，cacheManager变为 RedisCacheManager；
- 2、默认创建的 RedisCacheManager 操作redis的时候使用的是 RedisTemplate<Object, Object>
- 3、RedisTemplate<Object, Object> 是 默认使用jdk的序列化机制
- 4）、自定义CacheManager；
  *
  */
  @MapperScan("com.atguigu.cache.mapper")
  @SpringBootApplication
  @EnableCaching
  public class Springboot2Application {
  public static void main(String[] args) {
      SpringApplication.run(Springboot2Application.class, args);
  }

}

## 核心步骤(与SpringBoot结合使用)

```xml
添加额外依赖:
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

```properties
spring.redis.host=127.0.0.1
```

```java
package com.yy.sb_redis.config;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;
@Configuration
public class MyRedisConfig {
    //springboot检查到了缓存管理器,就不会使用默认的缓存管理器了,会使用我们在下面创建的缓存管理器
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofDays(1))
                        .disableCachingNullValues()             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                                GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }


}
```

```java
package com.yy.sb_redis.service;

import com.yy.sb_redis.bean.Department;
import com.yy.sb_redis.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;
    /**
     *方法的返回值会存储进cache中
     cacheManager = "cacheManager",这个cacheManager是MyRedisConfig类里面的方法名
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "department",cacheManager = "cacheManager")
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department =  departmentMapper.getDeptById(id);
        return department;
    }
}
```

```java
package com.yy.sb_redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.yy.sb_redis.mapper")
@SpringBootApplication
@EnableCaching
public class SbRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbRedisApplication.class, args);
    }

}
```