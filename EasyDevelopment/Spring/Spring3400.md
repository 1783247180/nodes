# BeanFactory接口介绍

BeanFacotry是spring中比较原始的Factory。ApplicationContext接口,它由BeanFactory接口派生而来，ApplicationContext包含BeanFactory的所有功能,在Spring中，所有的Bean都是由BeanFactory(也就是IOC容器)来进行管理的。

# FactoryBean<T>接口介绍

实现FactoryBean接口的类A，根据该Bean的ID（类A对应的id）从BeanFactory中获取的对象实际上是FactoryBean的getObject()返回的对象，而不是FactoryBean本身，如果要获取FactoryBean对象，请在id前面加一个&符号来获取

首先咱们一起来看下FactoryBean的源代码如下：

```java
public interface FactoryBean<T> {
    /**
     * 获取bean对应的实例对象
     * @return
     * @throws Exception
     */
    T getObject() throws Exception;
    /**
     * 获取factoryBean获取到的实例类型
     * @return
     */
    Class<?> getObjectType();
    /**
     * factoryBean创建的实例是否是单实例
     * @return
     */
    boolean isSingleton();
}
```

可以发现**FactoryBean**是一个接口主要有3个方法，每个方法的意思代码中都有注释这个就不重复了

下面我们通过**FactoryBean**来创建一个图形Circular的bean，代码如下：

```java
/**
 * 创建一个自定义的spring的FactoryBean
 * @author zhangqh
 * @date 2018年5月2日
 */
public class MyFactoryBean implements FactoryBean<Circular>{
    public Circular getObject() throws Exception {
        return new Circular();
    }
    public Class<?> getObjectType() {
        return Circular.class;
    }
    /**
     * 是否是单实例的，可以通过改变返回值测试效果
     */
    public boolean isSingleton() {
        return true;
    }
}
```

再在MainConfig主配置中增加一个bean如下

```java
/**
* id为方法名
*/
@Bean
public MyFactoryBean getMyFactoryBean(){
    return new MyFactoryBean();
}
```

测试代码如下：

```java
AnnotationConfigApplicationContext applicationContext2 = new AnnotationConfigApplicationContext(MainConfig.class);
Object object = applicationContext2.getBean("getMyFactoryBean");
System.out.println("实例bean为 === "+object);
Object object2 = applicationContext2.getBean("&getMyFactoryBean");
System.out.println("实例bean为 === "+object2);
```

运行结果:

```java
实例bean为 === com.zhang.bean.Circular@3bd94634
实例bean为 === com.zhang.bean.MyFactoryBean@58a90037
```

# mybatis的SqlSessionFactoryBean的介绍

SqlSessionFactoryBean实现了接口FactoryBean

在基本的 MyBatis 中,SqlSessionFactory可以使用 SqlSessionFactoryBuilder 来创建。而在MyBatis-Spring 中,则使用 SqlSessionFactoryBean 来替代。

要创建工厂 bean,放置下面的代码在 Spring 的 XML 配置文件中:

![](F:\Typora\EasyDevelopment\Spring\img\1.jpg)

要注意 SqlSessionFactoryBean 实现了 Spring 的 FactoryBean 接口,这就说明了由 Spring 最终创建的 bean 不是 SqlSessionFactoryBean 本身,。而是工厂类的 getObject()返回的方法的结果。这种情况下,Spring 将会在应用启动时为你创建 SqlSessionFactory 对象,然后将它以 SqlSessionFactory 为名来存储。在 Java 中,相同的代码是:

![](F:\Typora\EasyDevelopment\Spring\img\2.jpg)

属性

DataSource: SqlSessionFactory 有一个单独的必须属性,就是 JDBC 的 DataSource。这可以是任意的 DataSource,其配置应该和其它 Spring 数据库连接是一样的。

MapperLocations: mapperLocations 属性使用一个资源位置的 list。这个属性可以用来指定 MyBatis 的 XML映射器(mybatisConfig.xml)文件的位置。

configuration 属性.它能够在没有mybatisConfig.xml文件的情况下，对其属性进行配置。

```xml
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
</bean>
```



# SPI机制



![](.\img\3.jpg)

SPI 全称为 (Service Provider Interface) ,是JDK内置的一种服务提供发现机制。

一个服务(Service)通常指的是已知的接口或者抽象类，服务提供方就是对这个接口或者抽象类的实现，然后按照SPI 标准存放到资源路径META-INF/services目录下，文件的命名为该服务接口的全限定名。如有一个服务接口：

```java
package com.cainiao.ys.spi.learn;
import java.util.List;
public interface Search {
    public List<String> searchDoc(String keyword);   
}
```

文件搜索实现

```java
package com.cainiao.ys.spi.learn;
import java.util.List;
public class FileSearch implements Search{
    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("文件搜索 "+keyword);
        return null;
    }
}
```

数据库搜索实现

```java
package com.cainiao.ys.spi.learn;
import java.util.List;
public class DatabaseSearch implements Search{
    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("数据搜索 "+keyword);
        return null;
    }
}
```

接下来可以在resources下新建META-INF/services/目录，然后新建接口全限定名的文件：com.cainiao.ys.spi.learn.Search，里面加上我们需要用到的实现类

```text
com.cainiao.ys.spi.learn.FileSearch
```

然后写一个测试方法

```java
package com.cainiao.ys.spi.learn;
import java.util.Iterator;
import java.util.ServiceLoader;
public class TestCase {
    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
           Search search =  iterator.next();
           search.searchDoc("hello world");
        }
    }
}
```

可以看到输出结果：文件搜索 hello world

如果在com.cainiao.ys.spi.learn.Search文件里写上两个实现类，那最后的输出结果就是两行了。
这就是因为ServiceLoader.load(Search.class)在加载某接口时，会去META-INF/services下找接口的全限定名文件，再根据里面的内容加载相应的实现类。

关键步骤:

在META-INF/services/目录中创建以Service接口全限定名命名的文件，该文件内容为Service接口具体实现类的全限定名，文件编码必须为UTF-8。
使用ServiceLoader.load(Class class); 动态加载Service接口的实现类。
如SPI的实现类为jar，则需要将其放在当前程序的classpath下。
Service的具体实现类必须有一个不带参数的构造方法。

# ServletContainerInitializer

Servlet3通过SPI的机制允许我们自定义一个ServletContainerInitializer的实现类，Servlet容器会在启动的时候自动调用实现类的`onStartup`方法，我们可以在该方法中进行一些Servlet对象的注册。

ServletContainerInitializer接口的定义如下：

```java
public interface ServletContainerInitializer {
    public void onStartup(Set<Class<?>> c, ServletContext ctx)
        throws ServletException; 
}
```

在下面的代码中自定义了一个ServletContainerInitializer的实现类，叫MyServletContainerInitialier。在其onStartup方法中我们通过入参ServletContext分别注册了一个Servlet、一个监听器和一个Filter。

```java
public class MyServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        //注册Servlet
        javax.servlet.ServletRegistration.Dynamic servletRegistration = ctx.addServlet("hello", HelloServlet.class);
        servletRegistration.addMapping("/servlet3/hello");
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.setAsyncSupported(true);
        
        //注册监听器
        ctx.addListener(StartupListener.class);
        
        //注册Filter
        javax.servlet.FilterRegistration.Dynamic filterRegistration = ctx.addFilter("hello", HelloFilter.class);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/servlet3/*");
    }

}
```

它是基于SPI发现的，这需要我们在classpath下面的META-INF/services下新建一个名为`javax.servlet.ServletContainerInitializer`的文件，然后在里面加上我们自定义的ServletContainerIntializer的全路径名称。如果有多个实现类，每一个实现类写一行。



