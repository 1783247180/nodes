# Bean的注入



## 通过@Bean的注解将Bean注入Spring容器

我们都应该知道使用xml文件来配置bean，在xml中配置的bean会注入到Spring容器中，我们就可以通过`ApplicationContext.getBean()`方法获取相关的对象，那么我们使用注解怎么实现这个功能呢？下面先给出代码，然后根据代码进行讲解：

**Person类**

```java
package com.jiayifan.bean;
import org.springframework.beans.factory.annotation.Value;
/**
 * Created by Yifan Jia on 2018/6/12.
 */
public class Person {
    private String name;
    private Integer age;
    public Person() {
    }
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
//省略get,set,toString
}
```

**配置类**

```java
@Configuration//告诉spring这是一个配置类
public class MainConfig {
    @Bean//给容器中至少一个bean，bean的类型就是返回值得类型，id默认为方法名
    public Person person() {
        return new Person("RongCarryU", 20);
    }
}
```

首先，我们也需要一个像xml一样的配置文件来配置我们想注入容器的bean，这里我们创建了一个配置类，我们就可以把这个配置类当做以前的xml配置文件，在xml中可以配置的东西在配置类中都可以使用相应的注解实现，上面例子中，我们希望将Person类注入到容器中，创建一个配置类后，我们需要使用`@Configuration`注解来告诉Spring这是一个配置类，然后我们可以通过写一个返回我们需要对象的方法加上`@Bean`注解，就是：

```java
@Bean//给容器中至少一个bean，bean的类型就是返回值得类型，id默认为方法名
    public Person person() {
        return new Person("贾一帆", 20);
    }
```

来实现bean的注入，然后我们就可以测试一下了：

```java
@Test
    public void test01() {
        //获取容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        //查看容器中类型是Person类的BeanName
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for(String name : beanNamesForType) {
            System.out.println(name);
        }
    }
```

这里我们创建的容器不再是`ClassPathXmlApplicationContext`而是`AnnotationConfigApplicationContext`，然后传入的参数也不是xml配置文件，而是配置类，下面看一下测试的结果：

![](..\img\注解1.jpg)

这里我们也可以看到容器中的`Person`类的`BeanName`（Bean的id）是方法名。我们如果想要指定`BeanName`，我们可以在`@Bean`注解中添加属性，比如：`@Bean("myPerson")`，这样一改上面的测试结果就变成了

![](..\img\注解2.jpg)

## 通过包扫描的方法为容器中注入Bean

### xml中扫描包

```xml
<context:component-scan base-package="com.yy"/>
```

根据实际情况有时也会将Controller组件和其他组件分开

springmvc.xml中：

![](..\img\注解4.png)

applicationContext.xml中：

![](..\img\注解5.png)

### 注解方式扫描包

我们都知道在xml配置文件中可以通过包扫描的方法批量的将bean注入到容器中，而在配置类中，我们也有这样的功能，下面我们先看一下代码：
**这里我创建了其他一些POJO来作为Bean注入到容器中，和上面的Person类相似，下面就显示一个POJO，其他的就不展示了：**

```java
@Component//使用包扫描功能时需要添加这个注解
public class Yellow {
}
```

**配置类**

```java
@ComponentScan(value = "com.yy.bean"})
@Configuration//告诉spring这是一个配置类
public class MainConfig {
}
```

上面的代码我们看出来我们并没有使用`@Bean`注解的方法添加bean到容器中，而是使用了`@ComponentScan(value = "com.jiayifan.bean"})`注解，扫描`com.jiayifan。bean`包下有`@Component`注解标注的类，自动加入到容器中。下面我们来测试一下这个类：

```java
private void printBeans() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        //获取容器中所有的bean的名字
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name : beanDefinitionNames) {
            System.out.println(name);
        }
}
```

**上面这个方法可以打印容器中所有的bean的名字，我们以后会经常使用这个方法，大家请记住这个方法，后面的讲解中会调用了这个方法。**

```java
@Test
    public void importTest() {
         printBeans();
    }
```

测试的结果是：

![](..\img\注解3.jpg)



我们可以看到除了Spring容器启动时自动加载的一些bean，还加载了`mainConfig`、`yellow`、`myPerson`，这里的`mainConfig`就是我们的配置类，其他的两个就是`com.jiayifan.bean`中使用`@Component`标注了的类，当然我们也可以使用`@Controller`、`@Repository`、`@Service`来标注需要包扫描添加的bean，不过因为不涉及到web，所以使用了`@Component`。

### @ComponentScan与@ComponentScans

@ComponentScans注解中的value是@ComponentScan数组,可以在@ComponentScans添加多个@ComponentScan

### @ComponentScan的其他属性(过滤)

过滤规则有很多，这里简单介绍两个：

![](F:\Typora\EasyDevelopment\Spring\img\注解6.png)

includeFilters是扫描的时候只包含哪些，excludeFilters是扫描的时候排除哪些。它们的值都是一个Filer数组，看看Filer数组是什么：

![](F:\Typora\EasyDevelopment\Spring\img\注解7.png)

是@Filter注解，看看这个注解的写法：

可以定义排除规则，可以按注解排除，也可以按类排除等等，是类就是类的Class，是注解就是注解的Class：

![](F:\Typora\EasyDevelopment\Spring\img\注解8.png)

例如这里排除@Controller注解：

![](F:\Typora\EasyDevelopment\Spring\img\注解9.png)

再测试看看，少了@Controllr的组件：

![](F:\Typora\EasyDevelopment\Spring\img\注解10.png)

再来测试只包含@Controller的注解，这里要注意的是要加上useDefaultFilters=false，即关闭默认的过滤规则：

![](F:\Typora\EasyDevelopment\Spring\img\注解11.png)

再看看@ComponentScan注解：

![](F:\Typora\EasyDevelopment\Spring\img\注解12.png)

说明它是支持重复注解的，也就是说可以多个@ComponentScan一起使用，不过这个要jdk1.8以上才支持，在使用的时候要注意的是根据反射获取注解的属性值的时候会有点小变化，具体简单测试下就行了。


## 通过`@Import`将Bean注入到容器

我们也可以通过`@Import`注解来快速的为容器中注入我们所需要的bean，下面还是先看代码：
**导入的POJO类省略**
**配置类**

```java
@Configuration//告诉spring这是一个配置类
@Import(value = {Color.class})//快速导入bean，id默认为全类名
public class MainConfig2 {
}
```

这时候我们测试一下容器中有哪些bean:

![](F:\Typora\EasyDevelopment\Spring\img\注解13.jpg)


这里我换了另外一个配置类当做配置文件，我们可以看到容器中只有`com.jiayifan.bean.Color`和`mainConfig2`，这里需要注意，使用`@Import`注解导入的bean的名称为全类名。
除了使用`@Import(value = {Color.class})`直接导入bean之外，我们可以通过查看`@Import`注解的源码，看一下`@Import`还可以通过其他的方法导入bean：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

    /**
     * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
     * or regular component classes to import.
     */
    Class<?>[] value();

}
```

我们可以看到`value`属性除了可以直接添加类之外，还可以添加`ImportSelector`，其实这个`ImportSelector`是一个接口，我们可以自定义实现一个`ImportSelector`：

```java
public class MyImportSelect implements ImportSelector {
    //返回值就是要导入到容器中的bean全类名
    //AnnotationMetadata：当前标注@Import注解的类的所有注解信息
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        //可以返回一个空数组，但是不要返回null，会报错
        return new String[]{"com.jiayifan.bean.Blue", "com.jiayifan.bean.Yellow", "com.jiayifan.bean.Red"};
    }
}
```

通过实现的`MyImportSelect`我们可以看到，其实这个`ImportSelector`，就是一个将需要注入容器的bean的信息包装起来的类，我们主要将bean的全类名包装在这个类中，然后在`@Import`的属性中添加该类，就可以实现将多个bean一起注入到容器，不过这个类的主要作用应该是可以使用`importingClassMetadata`参数对所需要注入的bean进行筛选。我们看一下使用这种方法怎么将bean注入容器：

```java
@Configuration//告诉spring这是一个配置类
@Import(value = {MyImportSelect.class})
public class MainConfig2 {
}
```

测试结果：

![](F:\Typora\EasyDevelopment\Spring\img\注解14.jpg)

在上面的`@Import`注解的源码中，我们还发现除了类、`ImportSelector`之外，还可以添加`ImportBeanDefinitionRegister`,我们上面两种方法添加到容器中的bean的名称都是全类名，但是如果使用`ImportBeanDefinitionRegister`，我们就可以自定义bean的名称了。
这里实现一个自己的`ImportBeanDefinitionRegister`：

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata  当前类的注解信息
     * @param registry bean定义的注册类
     * 把所有需要注册到容器中的bean：通过BeanDefinitionRegistry注册到容器中
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            //指定bean和BeanDefinition
            //BeanDefinition自己创建
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Yellow.class);
            registry.registerBeanDefinition("yellow", rootBeanDefinition);

    }
}
```

我们可以发现在这个方法中，我们通过自定义`BeanDefinition`，手动的将`BeanDefinition`注册到容器中实现将bean注入容器的功能，这个方法中我们的自由度更大，感觉只是注册一个类有点大材小用的感觉。我们接着测试一下：

```java
@Configuration//告诉spring这是一个配置类
@Import(value = {MyImportBeanDefinitionRegistrar.class})
public class MainConfig2 {
}
```

测试结果：

![img](https://pic1.zhimg.com/80/v2-8f2ea3f9d253c6e58e532d2ecb3b9dac_hd.jpg)


上我们可以发现bean的名称就是我们在`registry.registerBeanDefinition("yellow", rootBeanDefinition);`中添加的名称。

## 通过FactoryBean注入Bean

可以看spring3400.md,这里不再重复

# @Scope

这个注解放在Spring组件上可以控制该组件是单实例还是多实例，作用域等等，和xml文件中的那个属性是一样的。

要注意的是：Spring  IOC容器启动，在多实例下，只有在用到的时候才会去创建，而在单实例下，是IOC容器启动就会去创建。

具体用法:

@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)这个是说在每次注入的时候回自动创建一个新的bean实例

@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)单例模式，在整个应用中只能创建一个实例

@Scope(value=WebApplicationContext.SCOPE_GLOBAL_SESSION)全局session中的一般不常用

@Scope(value=WebApplicationContext.SCOPE_APPLICATION)在一个web应用中只创建一个实例

@Scope(value=WebApplicationContext.SCOPE_REQUEST)在一个请求中创建一个实例

@Scope(value=WebApplicationContext.SCOPE_SESSION)每次创建一个会话中创建一个实例

里面还有个属性

proxyMode=ScopedProxyMode.INTERFACES创建一个JDK代理模式

proxyMode=ScopedProxyMode.TARGET_CLASS基于类的代理模式

proxyMode=ScopedProxyMode.NO（默认）不进行代理

# @Conditional

@Conditional注解是可以根据一些自定义的条件动态的选择是否加载该bean到springIOC容器中去

@Conditional可以作用在方法上,也可以作用在类上

eg:

定义一个Condition

```java
public class MyCondition implements Condition {
    public boolean matches(ConditionContext context,AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        String system = env.getProperty("os.name");
        System.out.println("系统环境为 ==="+system);
        // 系统环境在Windows才加载该bean到容器中
        if(system.contains("Windows")){
            return true;
        }
        return false;
    }
}
```

定义一个bean加上@Conditional注解如下：

```java
@Conditional({MyCondition.class})
@Bean(value="user1")
public User getUser1(){
    System.out.println("创建user1实例");
    return new User("李四",26);
}
```

测试如下：

```java
AnnotationConfigApplicationContext applicationContext2 = new AnnotationConfigApplicationContext(MainConfig.class);
String[] beanNames = applicationContext2.getBeanDefinitionNames();
for(int i=0;i<beanNames.length;i++){
    System.out.println("bean名称为==="+beanNames[i]);
}
```

运行结果：

```text
bean名称为===mainConfig
bean名称为===user0
bean名称为===user1
```

我这边电脑系统是window所以user1实例是有创建出来的，如果把MyCondition中的判断改成**if(system.contains("linux"))**那么user1是不会加载到spring容器中的

# @PropertySource

加载指定的配置文件；

```java
@PropertySource(value = {"classpath:person.properties"})
@Component
public class Person {
    @Value("${person.last-name}")
    private String lastName;
    private Integer age;
    private Boolean boss;
```

# @Value

@Value("${server.port}")

@Value("#{userBean.name}")

userBean为UserBean实体类在spring容器注册的默认id

第一个注入的是外部配置文件对应的property，第二个则是SpEL表达式对应的内容。 

# @Autowired

IOC容器中的bean都可以使用@Autowired

问题：一个Animal接口被多个实现类实现的时候，在业务层用注解声明Animal接口的时候，会报错，它不知道去取具体哪一个实现类？

＠Primary----在实现类上面添加此注解，表明这个实现类优先级最高。

＠Quelifier---在业务层声明Animal接口的时候，加上这个注解，声明要用哪个实现类即可。

```java
@Component 
@Primary 
public class Cat implements Animal { xxxx }
//-----------------------------------------------------
@Autowired 
@Qualifier ( "dog") 
private Animal animal; 
```

# @Resource

@Resource的作用相当于@Autowired，只不过@Autowired按byType自动注入，而@Resource默认按 byName自动注入罢了。

# @Profile

@profile注解是spring提供的一个用来标明当前运行环境的注解。我们正常开发的过程中经常遇到的问题是，开发环境是一套环境，qa测试是一套环境，线上部署又是一套环境。

https://blog.csdn.net/u012129558/article/details/78258951

# Bean生命周期

创建,初始化,销毁

在单例模式中(@Scope("singleton")):

@Bean(init-method="init",destroy-method="destroy")

就会在加载IOC容器的时候调用初始化方法,在关闭BeanFactory(ApplicationContext)的时候调用销毁方法

在多例模式下spring不会管理bean的生命周期



# AOP

```java
package com.atguigu.aop;
public class MathCalculator {
	public int div(int i,int j){
		System.out.println("MathCalculator...div...");
		return i/j;	
	}
}
```

```java
package com.atguigu.aop;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 切面类
 * @author lfy
 * 
 * @Aspect： 告诉Spring当前类是一个切面类
 *
 */
@Aspect
public class LogAspects {
	//抽取公共的切入点表达式
	//1、本类引用
	//2、其他的切面引用
	@Pointcut("execution(public int com.atguigu.aop.MathCalculator.*(..))")
	public void pointCut(){};
	//@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
	@Before("pointCut()")
	public void logStart(JoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		System.out.println(""+joinPoint.getSignature().getName()+"运行。。。@Before:参数列表是：{"+Arrays.asList(args)+"}");
	}
	
	@After("com.atguigu.aop.LogAspects.pointCut()")
	public void logEnd(JoinPoint joinPoint){
		System.out.println(""+joinPoint.getSignature().getName()+"结束。。。@After");
	}
	//JoinPoint一定要出现在参数表的第一位
	@AfterReturning(value="pointCut()",returning="result")
	public void logReturn(JoinPoint joinPoint,Object result){
		System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
	}
	@AfterThrowing(value="pointCut()",throwing="exception")
	public void logException(JoinPoint joinPoint,Exception exception){
		System.out.println(""+joinPoint.getSignature().getName()+"异常。。。异常信息：{"+exception+"}");
	}
}

```

```java
package com.atguigu.config;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import com.atguigu.aop.LogAspects;
import com.atguigu.aop.MathCalculator;
/**
 * AOP：【动态代理】
 * 		指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
 * 
 * 1、导入aop模块；Spring AOP：(pom.xml中的依赖spring-aspects)
 * 2、定义一个业务逻辑类（MathCalculator）；在业务逻辑运行的时候将日志进行打印（方法之前、方法运行结束、方法出现异常，xxx）
 * 3、定义一个日志切面类（LogAspects）：切面类里面的方法需要动态感知MathCalculator.div运行到哪里然后执行；
 * 		通知方法：
 * 			前置通知(@Before)：logStart：在目标方法(div)运行之前运行
 * 			后置通知(@After)：logEnd：在目标方法(div)运行结束之后运行（无论方法正常结束还是异常结束）
 * 			返回通知(@AfterReturning)：logReturn：在目标方法(div)正常返回之后运行
 * 			异常通知(@AfterThrowing)：logException：在目标方法(div)出现异常以后运行
 * 			环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.procced()）
 * 4、给切面类的目标方法标注何时何地运行（通知注解）；
 * 5、将切面类和业务逻辑类（目标方法所在类）都加入到容器中;
 * 6、必须告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
 * [7]、给配置类中加 @EnableAspectJAutoProxy 【开启基于注解的aop模式】
 * 		在Spring中很多的 @EnableXXX;
 * 
 * 三步：
 * 	1）、将业务逻辑组件和切面类都加入到容器中；告诉Spring哪个是切面类（@Aspect）
 * 	2）、在切面类上的每一个通知方法上标注通知注解，告诉Spring何时何地运行（切入点表达式）
 *  3）、开启基于注解的aop模式；@EnableAspectJAutoProxy
 *  
 * AOP原理：【看给容器中注册了什么组件，这个组件什么时候工作，这个组件的功能是什么？】
 * 		@EnableAspectJAutoProxy；
 * 1、@EnableAspectJAutoProxy是什么？
 * 		在@EnableAspectJAutoProxy上有@Import(AspectJAutoProxyRegistrar.class)：给容器中导入AspectJAutoProxyRegistrar,而 AspectJAutoProxyRegistrar里面的 implements ImportBeanDefinitionRegistrar
 ImportBeanDefinitionRegistrar这个类我们很熟悉,在@Import注解里面介绍过,在AspectJAutoProxyRegistrar里面的
registerBeanDefinitions方法里面打断点再运行test01方法(AspectJAutoProxyRegistrar类在后面)
一步步调试到AopConfigUtils里面的registerOrEscalateApcAsRequired(Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source)方法,通过调试可以看到cls为AnnotationAwareAspectJAutoProxyCreator.class
方法里面有一段
RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
registry.registerBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator", beanDefinition);
 * 			利用AspectJAutoProxyRegistrar自定义给容器中注册bean；
 org.springframework.aop.config.internalAutoProxyCreator为bean名称
 AnnotationAwareAspectJAutoProxyCreator为bean类型
 * 结果:
 * 		给容器中注册一个AnnotationAwareAspectJAutoProxyCreator；
 * 
 * 2、 AnnotationAwareAspectJAutoProxyCreator(继承关系)：
 * 		AnnotationAwareAspectJAutoProxyCreator
 * 			->AspectJAwareAdvisorAutoProxyCreator
 * 				->AbstractAdvisorAutoProxyCreator
 * 					->AbstractAutoProxyCreator
 * 							implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 * 						关注后置处理器（在bean初始化完成前后做事情）、自动装配BeanFactory
 * 
 * AbstractAutoProxyCreator.setBeanFactory()
 * AbstractAutoProxyCreator.有后置处理器的逻辑；
 * 
 * AbstractAdvisorAutoProxyCreator.setBeanFactory()-》initBeanFactory()
 * 
 * AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 *
 *
 * 流程：
 * 		1）、传入配置类，创建ioc容器
 * 		2）、注册配置类，调用refresh（）刷新容器；
 * 		3）、registerBeanPostProcessors(beanFactory);注册bean的后置处理器来方便拦截bean的创建；
 * 			1）、先获取ioc容器已经定义了的需要创建对象的所有BeanPostProcessor
 * 			2）、给容器中加别的BeanPostProcessor
 * 			3）、优先注册实现了PriorityOrdered接口的BeanPostProcessor；
 * 			4）、再给容器中注册实现了Ordered接口的BeanPostProcessor；
 * 			5）、注册没实现优先级接口的BeanPostProcessor；
 * 			6）、注册BeanPostProcessor，实际上就是创建BeanPostProcessor对象，保存在容器中；
 * 				创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】
 * 				1）、创建Bean的实例
 * 				2）、populateBean；给bean的各种属性赋值
 * 				3）、initializeBean：初始化bean；
 * 						1）、invokeAwareMethods()：处理Aware接口的方法回调
 * 						2）、applyBeanPostProcessorsBeforeInitialization()：应用后置处理器的postProcessBeforeInitialization（）
 * 						3）、invokeInitMethods()；执行自定义的初始化方法
 * 						4）、applyBeanPostProcessorsAfterInitialization()；执行后置处理器的postProcessAfterInitialization（）；
 * 				4）、BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)创建成功；--》aspectJAdvisorsBuilder
 * 			7）、把BeanPostProcessor注册到BeanFactory中；
 * 				beanFactory.addBeanPostProcessor(postProcessor);
 * =======以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程========
 * 
 * 			AnnotationAwareAspectJAutoProxyCreator => InstantiationAwareBeanPostProcessor
 * 		4）、finishBeanFactoryInitialization(beanFactory);完成BeanFactory初始化工作；创建剩下的单实例bean
 * 			1）、遍历获取容器中所有的Bean，依次创建对象getBean(beanName);
 * 				getBean->doGetBean()->getSingleton()->
 * 			2）、创建bean
 * 				【AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，InstantiationAwareBeanPostProcessor，会调用postProcessBeforeInstantiation()】
 * 				1）、先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用，否则再创建；
 * 					只要创建好的Bean都会被缓存起来
 * 				2）、createBean（）;创建bean；
 * 					AnnotationAwareAspectJAutoProxyCreator 会在任何bean创建之前先尝试返回bean的实例
 * 					【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】
 * 					【InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试用后置处理器返回对象的】
 * 					1）、resolveBeforeInstantiation(beanName, mbdToUse);解析BeforeInstantiation
 * 						希望后置处理器在此能返回一个代理对象；如果能返回代理对象就使用，如果不能就继续
 * 						1）、后置处理器先尝试返回对象；
 * 							bean = applyBeanPostProcessorsBeforeInstantiation（）：
 * 								拿到所有后置处理器，如果是InstantiationAwareBeanPostProcessor;
 * 								就执行postProcessBeforeInstantiation
 * 							if (bean != null) {
								bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
							}
 * 
 * 					2）、doCreateBean(beanName, mbdToUse, args);真正的去创建一个bean实例；和3.6流程一样；
 * 					3）、
 * 			
 * 		
 * AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】	的作用：
 * 1）、每一个bean创建之前，调用postProcessBeforeInstantiation()；
 * 		关心MathCalculator和LogAspect的创建
 * 		1）、判断当前bean是否在advisedBeans中（保存了所有需要增强bean）
 * 		2）、判断当前bean是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean，
 * 			或者是否是切面（@Aspect）
 * 		3）、是否需要跳过
 * 			1）、获取候选的增强器（切面里面的通知方法）【List<Advisor> candidateAdvisors】
 * 				每一个封装的通知方法的增强器是 InstantiationModelAwarePointcutAdvisor；
 * 				判断每一个增强器是否是 AspectJPointcutAdvisor 类型的；返回true
 * 			2）、永远返回false
 * 
 * 2）、创建对象
 * postProcessAfterInitialization；
 * 		return wrapIfNecessary(bean, beanName, cacheKey);//包装如果需要的情况下
 * 		1）、获取当前bean的所有增强器（通知方法）  Object[]  specificInterceptors
 * 			1、找到候选的所有的增强器（找哪些通知方法是需要切入当前bean方法的）
 * 			2、获取到能在bean使用的增强器。
 * 			3、给增强器排序
 * 		2）、保存当前bean在advisedBeans中；
 * 		3）、如果当前bean需要增强，创建当前bean的代理对象；
 * 			1）、获取所有增强器（通知方法）
 * 			2）、保存到proxyFactory
 * 			3）、创建代理对象：Spring自动决定
 * 				JdkDynamicAopProxy(config);jdk动态代理；
 * 				ObjenesisCglibAopProxy(config);cglib的动态代理；
 * 		4）、给容器中返回当前组件使用cglib增强了的代理对象；
 * 		5）、以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程；
 * 		
 * 	
 * 	3）、目标方法执行	；
 * 		容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象，xxx）；
 * 		1）、CglibAopProxy.intercept();拦截目标方法的执行
 * 		2）、根据ProxyFactory对象获取将要执行的目标方法拦截器链；
 * 			List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 * 			1）、List<Object> interceptorList保存所有拦截器 5
 * 				一个默认的ExposeInvocationInterceptor 和 4个增强器；
 * 			2）、遍历所有的增强器，将其转为Interceptor；
 * 				registry.getInterceptors(advisor);
 * 			3）、将增强器转为List<MethodInterceptor>；
 * 				如果是MethodInterceptor，直接加入到集合中
 * 				如果不是，使用AdvisorAdapter将增强器转为MethodInterceptor；
 * 				转换完成返回MethodInterceptor数组；
 * 
 * 		3）、如果没有拦截器链，直接执行目标方法;
 * 			拦截器链（每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制）
 * 		4）、如果有拦截器链，把需要执行的目标对象，目标方法，
 * 			拦截器链等信息传入创建一个 CglibMethodInvocation 对象，
 * 			并调用 Object retVal =  mi.proceed();
 * 		5）、拦截器链的触发过程;
 * 			1)、如果没有拦截器执行执行目标方法，或者拦截器的索引和拦截器数组-1大小一样（指定到了最后一个拦截器）执行目标方法；
 * 			2)、链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行；
 * 				拦截器链的机制，保证通知方法与目标方法的执行顺序；
 * 		
 * 	总结：
 * 		1）、  @EnableAspectJAutoProxy 开启AOP功能
 * 		2）、 @EnableAspectJAutoProxy 会给容器中注册一个组件 AnnotationAwareAspectJAutoProxyCreator
 * 		3）、AnnotationAwareAspectJAutoProxyCreator是一个后置处理器；
 * 		4）、容器的创建流程：
 * 			1）、registerBeanPostProcessors（）注册后置处理器；创建AnnotationAwareAspectJAutoProxyCreator对象
 * 			2）、finishBeanFactoryInitialization（）初始化剩下的单实例bean
 * 				1）、创建业务逻辑组件和切面组件
 * 				2）、AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程
 * 				3）、组件创建完之后，判断组件是否需要增强
 * 					是：切面的通知方法，包装成增强器（Advisor）;给业务逻辑组件创建一个代理对象（cglib）；
 * 		5）、执行目标方法：
 * 			1）、代理对象执行目标方法
 * 			2）、CglibAopProxy.intercept()；
 * 				1）、得到目标方法的拦截器链（增强器包装成拦截器MethodInterceptor）
 * 				2）、利用拦截器的链式机制，依次进入每一个拦截器进行执行；
 * 				3）、效果：
 * 					正常执行：前置通知-》目标方法-》后置通知-》返回通知
 * 					出现异常：前置通知-》目标方法-》后置通知-》异常通知
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {	 
	//业务逻辑类加入容器中
	@Bean
	public MathCalculator calculator(){
		return new MathCalculator();
	}
	//切面类加入到容器中
	@Bean
	public LogAspects logAspects(){
		return new LogAspects();
	}
}
```

```java
package org.springframework.context.annotation;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {
    AspectJAutoProxyRegistrar() {
    }
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //注册AspectJAnnotationAutoProxyCreator
        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
        AnnotationAttributes enableAspectJAutoProxy = AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
        if (enableAspectJAutoProxy != null) {
            if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
                AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
            }

            if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
                AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
            }
        }
    }
}
```



# 声明式事务

Spring提供了对事务的管理, 这个就叫声明式事务管理。 
Spring提供了对事务控制的实现。用户如果想用Spring的声明式事务管理，只需要在配置文件中配置即可； 不想使用时直接移除配置。这个实现了对事务控制的最大程度的解耦。 
Spring声明式事务管理，核心实现就是基于Aop。

```java
package com.atguigu.tx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Transactional
	public void insertUser(){
		userDao.insert();
		//otherDao.other();xxx
		System.out.println("插入完成...");
		int i = 10/0;
	}
}
```

```java
package com.atguigu.tx;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void insert(){
		String sql = "INSERT INTO `tbl_user`(username,age) VALUES(?,?)";
		String username = UUID.randomUUID().toString().substring(0, 5);
		jdbcTemplate.update(sql, username,19);	
	}
}
```

```java
package com.atguigu.tx;
import java.beans.PropertyVetoException;
import javax.sql.DataSource;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;
import org.springframework.transaction.annotation.Transactional;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 声明式事务：
 * 
 * 环境搭建：
 * 1、导入相关依赖
 * 		数据源、数据库驱动、Spring-jdbc模块
 * 2、配置数据源、JdbcTemplate（Spring提供的简化数据库操作的工具）操作数据
 * 3、给方法上标注 @Transactional 表示当前方法是一个事务方法；
 * 4、 @EnableTransactionManagement 开启基于注解的事务管理功能；
 * 		@EnableXXX
 * 5、配置事务管理器来控制事务;
 * 		@Bean
 * 		public PlatformTransactionManager transactionManager()
 * 
 * 
 * 原理：
 * 1）、@EnableTransactionManagement
 * 			利用TransactionManagementConfigurationSelector给容器中会导入组件
 * 			导入两个组件
 * 			AutoProxyRegistrar
 * 			ProxyTransactionManagementConfiguration
 * 2）、AutoProxyRegistrar：
 * 			给容器中注册一个 InfrastructureAdvisorAutoProxyCreator 组件；
 * 			InfrastructureAdvisorAutoProxyCreator：？
 * 			利用后置处理器机制在对象创建以后，包装对象，返回一个代理对象（增强器），代理对象执行方法利用拦截器链进行调用；
 * 
 * 3）、ProxyTransactionManagementConfiguration 做了什么？
 * 			1、给容器中注册事务增强器；
 * 				1）、事务增强器要用事务注解的信息，AnnotationTransactionAttributeSource解析事务注解
 * 				2）、事务拦截器：
 * 					TransactionInterceptor；保存了事务属性信息，事务管理器；
 * 					他是一个 MethodInterceptor；
 * 					在目标方法执行的时候；
 * 						执行拦截器链；
 * 						事务拦截器：
 * 							1）、先获取事务相关的属性
 * 							2）、再获取PlatformTransactionManager，如果事先没有添加指定任何transactionmanger
 * 								最终会从容器中按照类型获取一个PlatformTransactionManager；
 * 							3）、执行目标方法
 * 								如果异常，获取到事务管理器，利用事务管理回滚操作；
 * 								如果正常，利用事务管理器，提交事务
 * 			
 */
@EnableTransactionManagement
@ComponentScan("com.atguigu.tx")
@Configuration
public class TxConfig {
	//数据源
	@Bean
	public DataSource dataSource() throws Exception{
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("123456");
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
		return dataSource;
	}	
	@Bean
	public JdbcTemplate jdbcTemplate() throws Exception{
		//Spring对@Configuration类会特殊处理；给容器中加组件的方法，多次调用都只是从容器中找组件
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}	
	//注册事务管理器在容器中
	@Bean
	public PlatformTransactionManager transactionManager() throws Exception{
		return new DataSourceTransactionManager(dataSource());
	}
}
```



# SpringMVC/spring/servlet容器

可以先看servlet容器

## Spring容器

Spring容器是管理service和dao的。

## SpringMVC容器

SpringMVC容器是管理controller对象的。

Spring容器和SpringMVC容器的关系是父子容器的关系。Spring容器是父容器，SpringMVC容器是子容器。在子容器里可以访问父容器里的对象，但是在父容器里不可以访问子容器的对象，说的通俗点就是，在controller里可以访问service对象，但是在service里不可以访问controller对象。所以这么看的话，所有的bean，都是被Spring或者SpringMVC容器管理的，他们可以直接注入。然后SpringMVC的拦截器也是SpringMVC容器管理的，所以在SpringMVC的拦截器里，可以直接注入bean对象。

## web.xml

1、TOMCAT启动，Servlet容器随即启动，然后读取server.xml配置文件，启动里面配置的web应用，为每个应用创建一个全局上下文环境（ServletContext）；

2、创建Spring容器实例。调用web.xml中配置的ContextLoaderListener，初始化WebApplicationContext上下文环境（即IOC容器），加载context­param指定的配置文件信息到IOC容器中。WebApplicationContext在ServletContext中以键值对的形式保存。

```xml
<context-param>
　　<param-name>contextConfigLocation</param-name>
　　<param-value>classpath:spring-context.xml</param-value>
</context-param>
<!--监听ServletContext的启动事件,目的是当Servlet容器启动时,创建并初始化全局的Spring容器.-->
<listener>
　　<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

3、创建SpringMVC容器实例。调用web.xml中配置的servlet-class，为其初始化自己的上下文信息，并加载其设置的配置信息到该上下文中。将WebApplicationContext设置为它的父容器。

```xml
<!-- springMVC配置 -->
<servlet>
　　<servlet-name>appServlet</servlet-name>
　　<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
　　<init-param>
　　　　<param-name>contextConfigLocation</param-name>
　　　　<param-value>classpath:spring-servlet.xml</param-value>
　　</init-param>
　　<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
　　<servlet-name>appServlet</servlet-name>
　　<url-pattern>*.do</url-pattern>
</servlet-mapping>
```

4、此后的所有servlet的初始化都按照3步中方式创建，初始化自己的上下文环境，将WebApplicationContext设置为自己的父上下文环境。当Spring在执行ApplicationContext的getBean时，如果在自己context中找不到对应的bean，则会在父ApplicationContext中去找。



## 纯注解

1、web容器在启动的时候，会扫描每个jar包下的META-INF/services/javax.servlet.ServletContainerInitializer，加载这个文件指定的类SpringServletContainerInitializer

```txt
org.springframework.web.SpringServletContainerInitializer
```

2、spring的应用一启动会加载感兴趣的WebApplicationInitializer接口的下的所有组件；

```java
package org.springframework.web;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口，抽象类等）传递过来；
//这里指定了感兴趣的接口WebApplicationInitializer.class
@HandlesTypes({WebApplicationInitializer.class})
public class SpringServletContainerInitializer implements ServletContainerInitializer {
    public SpringServletContainerInitializer() {
    }
    public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList();
        Iterator var4;
        if (webAppInitializerClasses != null) {
            var4 = webAppInitializerClasses.iterator();
            while(var4.hasNext()) {
                Class<?> waiClass = (Class)var4.next();
                //为WebApplicationInitializer组件创建对象,组件是实现类(抽象类与子接口都不是组件)
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) && WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
                    try {            initializers.add((WebApplicationInitializer)ReflectionUtils.accessibleConstructor(waiClass, new Class[0]).newInstance());
                    } catch (Throwable var7) {
                        throw new ServletException("Failed to instantiate WebApplicationInitializer class", var7);
                    }
                }
            }
        }
        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
        } else {
            servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
            AnnotationAwareOrderComparator.sort(initializers);
            var4 = initializers.iterator();
            while(var4.hasNext()) {
                WebApplicationInitializer initializer = (WebApplicationInitializer)var4.next();
                initializer.onStartup(servletContext);
            }
        }
    }
}
```

3、WebApplicationInitializer接口下面的子接口,抽象类与实现类

```java
package org.springframework.web;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//子接口,抽象类与实现类AbstractContextLoaderInitializer,AbstractDispatcherServletInitializer,AbstractAnnotationConfigDispatcherServletInitializer
public interface WebApplicationInitializer {
    void onStartup(ServletContext var1) throws ServletException;
}
```

1. AbstractContextLoaderInitializer抽象类：创建spring根容器；createRootApplicationContext()抽象方法；

2. AbstractDispatcherServletInitializer：
   			创建一个springmvc的ioc容器；createServletApplicationContext()抽象方法;
      			创建了DispatcherServlet；createDispatcherServlet()方法；
      			将创建的DispatcherServlet添加到ServletContext中；

3. AbstractAnnotationConfigDispatcherServletInitializer抽象类 extends AbstractDispatcherServletInitializer：注解方式配置的DispatcherServlet初始化器
   			创建根容器：createRootApplicationContext()方法(其实就是实现了步骤1中的抽象方法)
      					getRootConfigClasses();传入一个配置类
      			创建web的ioc容器： createServletApplicationContext()方法;(其实就是实现了步骤2中的抽象方法)
      					获取配置类；getServletConfigClasses();

   ```java
   package org.springframework.web.servlet.support;
   import org.springframework.lang.Nullable;
   import org.springframework.util.ObjectUtils;
   import org.springframework.web.context.WebApplicationContext;
   import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
   public abstract class AbstractAnnotationConfigDispatcherServletInitializer extends AbstractDispatcherServletInitializer {
       public AbstractAnnotationConfigDispatcherServletInitializer() {
       }
       @Nullable
       protected WebApplicationContext createRootApplicationContext() {
           //需要实现抽象方法getRootConfigClasses
           Class<?>[] configClasses = this.getRootConfigClasses();
           if (!ObjectUtils.isEmpty(configClasses)) {
               AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
               context.register(configClasses);
               return context;
           } else {
               return null;
           }
       }
       protected WebApplicationContext createServletApplicationContext() {
           AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
           //需要实现抽象方法getServletConfigClasses
           Class<?>[] configClasses = this.getServletConfigClasses();
           if (!ObjectUtils.isEmpty(configClasses)) {
               context.register(configClasses);
           }
           return context;
       }
       @Nullable
       protected abstract Class<?>[] getRootConfigClasses();
       @Nullable
       protected abstract Class<?>[] getServletConfigClasses();
   }
   ```

   总结：
   	以注解方式来启动SpringMVC；继承AbstractAnnotationConfigDispatcherServletInitializer；
   实现抽象方法；

```java
package com.atguigu;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.atguigu.config.AppConfig;
import com.atguigu.config.RootConfig;
//web容器启动的时候创建对象；调用方法来初始化容器以前前端控制器
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	//获取根容器的配置类；（Spring的配置文件）父容器；
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}
	//获取web容器的配置类（SpringMVC配置文件）  子容器；
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{AppConfig.class};
	}
	//获取DispatcherServlet的映射信息
	//  /：拦截所有请求（包括静态资源（xx.js,xx.png）），但是不包括*.jsp；
	//  /*：拦截所有请求；连*.jsp页面都拦截；jsp页面是tomcat的jsp引擎解析的；
	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[]{"/"};
	}
}
```

```java
package com.atguigu.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
//Spring的容器不扫描controller;父容器
@ComponentScan(value="com.atguigu",excludeFilters={
		@Filter(type=FilterType.ANNOTATION,classes={Controller.class})
})
public class RootConfig {
}
```

```java
package com.atguigu.config;
import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.atguigu.controller.MyFirstInterceptor;
//SpringMVC只扫描Controller；子容器
//useDefaultFilters=false 禁用默认的过滤规则；
@ComponentScan(value="com.atguigu",includeFilters={
		@Filter(type=FilterType.ANNOTATION,classes={Controller.class})
},useDefaultFilters=false)
@EnableWebMvc
public class AppConfig  implements WebMvcConfigurer  {
	//定制
	//视图解析器
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		//默认所有的页面都从 /WEB-INF/ xxx .jsp
		//registry.jsp();
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	//静态资源访问
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//super.addInterceptors(registry);
		registry.addInterceptor(new MyFirstInterceptor()).addPathPatterns("/**");
	}
}
```

# Servlet3.0异步请求

# SpringBoot是怎么取代web.xml的

使用SpringBootServletInitialier取代web.xml里面的配置

http://cache.baiducontent.com/c?m=9d78d513d98203ef03b1c1690c66873b441297624cc0a16668a5965f92144c37467192ba30236013a2b66b1701b8385dfd803065367337c79ddffc39cacae33f588c3035000bf74205a269b8ca36609670875b99b81990adf142d9f18cc798140dc509433cc2b19c5b7309d73fae4964f4a6ee1954094caded4739a81f6a73cc7808e11ca3b66a7354c1b0860d129437863657c4f46bf12318b719a41b1a2347&p=aa769a47c8d818ff57ed946d555e&newp=92759a45d69b09fb0cfec7710f6492695d0fc20e3addd201298ffe0cc4241a1a1a3aecbf22231503d6c07e6107aa4c58e0f631723d0034f1f689df08d2ecce7e77c0736063&user=baidu&fm=sc&query=SpringBoot%CA%C7%D3%C3%CA%B2%C3%B4%B4%FA%CC%E6%C1%CBweb%2Exml&qid=ecafe43e000703e9&p1=2

https://msd.misuland.com/pd/3255817928875968656