# 手撕mybatis源码

### 预备知识

#### 反射

1、获取类（Class）对象

获取类对象有三种方法：

- 通过forName() -> 示例：Class.forName("PeopleImpl")
- 通过getClass() -> 示例：new PeopleImpl().getClass()
- 直接获取.class -> 示例：PeopleImpl.class

2、类的常用方法

- getName()：获取类完整方法；
- getSuperclass()：获取类的父类；
- newInstance()：创建实例对象；
- getFields()：获取当前类和父类的public修饰的所有属性；
- getDeclaredFields()：获取当前类（不包含父类）的声明的所有属性；
- getMethod()：获取当前类和父类的public修饰的所有方法；
- getDeclaredMethods()：获取当前类（不包含父类）的声明的所有方法；

3、总结

1.在反射中核心的方法是 newInstance() 获取类实例，getMethod(..) 获取方法，使用 invoke(..) 进行方法调用

Class myClass = Class.forName("example.PeopleImpl");

Object object = myClass.newInstance();

myClass.getMethod("privSayHi").invoke(object);

参考(https://www.cnblogs.com/vipstone/p/10104020.html  ,D:\Java\java反射)

#### 代理模式

代理模式的作用是：为其他对象提供一种代理以控制对这个对象的访问。在某些情况下，一个客户不想或者不能直接引用另一个对象，而代理对象可以在客户端和目标对象之间起到中介的作用。

抽象角色：声明真实对象和代理对象的共同接口；

代理角色：代理对象角色内部含有对真实对象的引用，从而可以操作真实对象，同时代理对象提供与真实对象相同的接口以便在任何时刻都能代替真实对象。同时，代理对象可以在执行真实对象操作时，附加其他的操作，相当于对真实对象进行封装。

真实角色：代理角色所代表的真实对象，是我们最终要引用的对象。

#####  静态代理

先从直观的示例说起，假设我们有一个接口`Hello`和一个简单实现`HelloImp`：

```java
// 接口
interface Hello{
    String sayHello(String str);
}
// 实现
class HelloImp implements Hello{
    @Override
    public String sayHello(String str) {
        return "HelloImp: " + str;
    }
}
```

```java
// 静态代理方式
class StaticProxiedHello implements Hello{
    private Hello hello = new HelloImp();
    @Override
    public String sayHello(String str) {
        logger.info("You said: " + str);
        return hello.sayHello(str);
    }
}
```

上例中静态代理类`StaticProxiedHello`作为`HelloImp`的代理，实现了相同的`Hello`接口

#####  JDK动态代理

1. JDK Proxy 只能代理实现接口的类,例如UserMapper接口

2. 写一个类实现InvocationHandler接口重写invoke(Object proxy, Method method, Object[] args)方法 ,例如MapperProxy

3. invoke(Object proxy, Method method, Object[] args)

   proxy (生成的代理对象实例)

   method(代理对象调用的方法)

   args (方法里面的参数)

4. 在一个类中调用方法Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler handler) ,例如SqlSession(虚拟机会在运行时使用你提供的类加载器，将所有指定的接口类加载进方法区，然后反射读取这些接口中的方法并结合处理器类生成一个代理类型。)

   1. loader，指定代理对象的类加载器；
   2. interfaces，代理对象需要实现的接口，可以同时指定多个接口；
   3. handler，方法调用的实际处理者，代理对象的方法调用都会转发到这里。例如MapperProxy

5. newProxyInstance()会返回一个实现了指定接口的代理对象，对该对象的所有方法调用都会转发给InvocationHandler.invoke()方法。

   动态代理神奇的地方就是：

   1. 代理对象是在程序运行时产生的，而不是编译期；
   2. 对代理对象的所有接口方法调用都会转发到InvocationHandler.invoke()方法，在`invoke()`方法里我们可以加入任何逻辑，比如修改方法参数，加入日志功能、安全检查功能等；

#### 泛型方法

泛型类，是在实例化类的时候指明泛型的具体类型；泛型方法，是在调用方法的时候指明泛型的具体类型 。

```java
/**
 * 泛型方法的基本介绍
 * @param tClass 传入的泛型实参
 * @return T 返回值为T类型
 * 说明：
 *     1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
 *     2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
 *     3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
 *     4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
 */
public <T> T genericMethod(Class<T> tClass)throws InstantiationException ,
  IllegalAccessException{
        T instance = tClass.newInstance();
        return instance;
}
```

#### 装饰模式

何时使用:在不想增加很多子类的情况下扩展类。

装饰模式包含如下角色：

- Component: 抽象构件
- ConcreteComponent: 具体构件
- Decorator: 抽象装饰类
- ConcreteDecorator: 具体装饰类

关键代码： 1、Component 类充当抽象角色，不应该具体实现。 2、修饰类引用和继承 Component 类，具体扩展类重写父类方法。

example:每次英雄升级都会附加一个额外技能点学习技能。具体的英雄就是ConcreteComponent，技能栏就是装饰器Decorator，每个技能就是ConcreteDecorator；

```java
//Component 英雄接口 
public interface Hero {
    //学习技能
    void learnSkills();
}
//ConcreteComponent 具体英雄盲僧
public class BlindMonk implements Hero {
    private String name;
    public BlindMonk(String name) {
        this.name = name;
    }
    @Override
    public void learnSkills() {
        System.out.println(name + "学习了以上技能！");
    }
}
//Decorator 技能栏
public class Skills implements Hero{
    //持有一个英雄对象接口
    private Hero hero;
    public Skills(Hero hero) {
        this.hero = hero;
    }
    @Override
    public void learnSkills() {
            hero.learnSkills();
    }    
}
//ConreteDecorator 技能：Q
public class Skill_Q extends Skills{
    private String skillName;
    public Skill_Q(Hero hero,String skillName) {
        super(hero);
        this.skillName = skillName;
    }
    @Override
    public void learnSkills() {
        System.out.println("学习了技能Q:" +skillName);
        super.learnSkills();
    }
}
//ConreteDecorator 技能：W
public class Skill_W extends Skills{
    private String skillName;
    public Skill_W(Hero hero,String skillName) {
        super(hero);
        this.skillName = skillName;
    }
    @Override
    public void learnSkills() {
        System.out.println("学习了技能W:" + skillName);
        super.learnSkills();
    }
}
//ConreteDecorator 技能：E
public class Skill_E extends Skills{
    private String skillName;
    public Skill_E(Hero hero,String skillName) {
        super(hero);
        this.skillName = skillName;
    }
    @Override
    public void learnSkills() {
        System.out.println("学习了技能E:"+skillName);
        super.learnSkills();
    }
}
//ConreteDecorator 技能：R
public class Skill_R extends Skills{    
    private String skillName;
    public Skill_R(Hero hero,String skillName) {
        super(hero);
        this.skillName = skillName;
    }
    @Override
    public void learnSkills() {
        System.out.println("学习了技能R:" +skillName );
        super.learnSkills();
    }
}
//客户端：召唤师
public class Player {
    public static void main(String[] args) {
        //选择英雄
        Hero hero = new BlindMonk("李青");
        Skills skills = new Skills(hero);
        Skills r = new Skill_R(skills,"猛龙摆尾");
        Skills e = new Skill_E(r,"天雷破/摧筋断骨");
        Skills w = new Skill_W(e,"金钟罩/铁布衫");
        Skills q = new Skill_Q(w,"天音波/回音击");
        //学习技能
        q.learnSkills();
    }
}
```

```text
学习了技能Q:天音波/回音击
学习了技能W:金钟罩/铁布衫
学习了技能E:天雷破/摧筋断骨
学习了技能R:猛龙摆尾
李青学习了以上技能！
```

#### selectById(参数)

显然，null并不能作为基本数据类型的值，int a = null;这句代码编译都过不了，如果返回值为null的话就会尝试将null强转为基本数据类型，此时就会报空指针异常。但是对于其包装类型就不存在这个问题了，给包装类型返回null是合理的，因为Integer a = null是被允许的。所以参数最好是Integer而不是int

#### classPath

classPath即为java文件编译之后的classes文件的编译目录

src路径下的java文件夹与resources文件夹在编译后会放到classPath下,所以src下的文件可以直接用classpath引用

目录为

![1568722738905](F:\Typora\EasyDevelopment\Mybatis\mybatisResource\img\1568722738905.png)

可以看出webapp文件夹下的文件并没有放入classes文件夹里面,而java文件夹与resources文件夹下的文件都被放入了classes(classpath)

#### 类加载器

##### 主要有三种类加载器
BootstrapClassloader
负责将存放在Java_Home\lib目录下的类库加载到虚拟机,BootstrapClassloader由c++编写,所以启动类加载器(BootstrapClassloader)无法直接被java程序使用,如果需要使用BootstrapClassloader,则将请求委派给null即可,这样就会默认去使用BootstrapClassloader
ExtensionClassLoader
它负责加载JRE的扩展目录，lib/ext或者由java.ext.dirs系统属性指定的目录中的JAR包的类。由Java语言实现，父类加载器为null(即BootstrapClassloader)。
ApplicationClassLoader
负责加载应用程序classpath目录下的所有jar和class文件。它的父加载器为ExtensionClassLoader

##### 步骤

类加载器加载Class大致要经过如下8个步骤：
1.检测此Class是否载入过，即在缓冲区中是否有此Class，如果有直接进入第8步，否则进入第2步。
2.如果没有父类加载器，则要么Parent是根类加载器，要么本身就是根类加载器，则跳到第4步，如果父类加载器存在，则进入第3步。
3.请求使用父类加载器去载入目标类，如果载入成功则跳至第8步，否则接着执行第5步。
4.请求使用根类加载器(BootstrapClassloader)去载入目标类，如果载入成功则跳至第8步，否则跳至第7步。
5.当前类加载器尝试寻找Class文件，如果找到则执行第6步，如果找不到则执行第7步。
6.从文件中载入Class，成功后跳至第8步。
7.抛出ClassNotFountException异常。
8.返回对应的java.lang.Class对象。

### MYBATIS源码分析

1. 读取配置文件

   ```java
   //Resources的getResourceAsStream方法
   //    1.从类路径加载 SQL Map 配置文件（如 sqlMap-config.xml）。 
     //  2. 从类路径加载 DAO Manager 配置文件（如 dao.xml）。 
    //   3. 从类路径加载各种.properties 文件。 
   String resource = "mybatis.cfg.xml";
   InputStream in = Resources.getResourceAsStream(resource);
   ```

   这一步主要是加载5个类加载器去查找资源,返回包含mybatis.cfg.xml信息的InputStream

   ```java
   ClassLoaderWrapper中
   //一共5个类加载器
     ClassLoader[] getClassLoaders(ClassLoader classLoader) {
       return new ClassLoader[]{
           classLoader,
           defaultClassLoader,
           Thread.currentThread().getContextClassLoader(),
           getClass().getClassLoader(),
           systemClassLoader};
     }
   ```

   

2. 创建SqlSessionFactory

   ```java
   private static SqlSessionFactory sqlSessionFactory;
   sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
   ```

   进入build方法里面查看源代码

   ```java
   SqlSessionFactoryBuilder中
   public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
           SqlSessionFactory var5;
           try {
               XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
               //parser.parse()返回configuration对象
               var5 = this.build(parser.parse());
   ```

   进入parse方法

   ```java
   XMLConfigBuilder中
   public Configuration parse() {
           if (this.parsed) {
               throw new BuilderException("Each XMLConfigBuilder can only be used once.");
           } else {
               this.parsed = true;
               this.parseConfiguration(this.parser.evalNode("/configuration"));
               //对这些node进行解析，塞到configuration中。this.configuration对象就是解析mybatis.cfg.xml得到的对象
               return this.configuration;
           }
       }
       private void parseConfiguration(XNode root) {
           try {
               this.propertiesElement(root.evalNode("properties"));
               Properties settings = this.settingsAsProperties(root.evalNode("settings"));
               this.loadCustomVfs(settings);
               this.typeAliasesElement(root.evalNode("typeAliases"));
               this.pluginElement(root.evalNode("plugins"));
               this.objectFactoryElement(root.evalNode("objectFactory"));
               this.objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
               this.reflectorFactoryElement(root.evalNode("reflectorFactory"));
               this.settingsElement(settings);
               this.environmentsElement(root.evalNode("environments"));
               this.databaseIdProviderElement(root.evalNode("databaseIdProvider"));
               this.typeHandlerElement(root.evalNode("typeHandlers"));
               this.mapperElement(root.evalNode("mappers"));
           } catch (Exception var3) {
               throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + var3, var3);
           }
       }
   ```

   解析完后，进行build，返回DefaultSqlSessionFactory。

   ```java
   SqlSessionFactoryBuilder中
   public SqlSessionFactory build(Configuration config) {
   //DefaultSqlSessionFactory是SqlSessionFactory的子类
           return new DefaultSqlSessionFactory(config);
       }
   ```

3. 生成SqlSession

   ```java
   public static SqlSession getSession() {
           return sqlSessionFactory.openSession();
       }
   ```

   进入openSession方法

   ```java
   DefaultSqlSessionFactory中
   public SqlSession openSession() {
           return this.openSessionFromDataSource(this.configuration.getDefaultExecutorType(), (TransactionIsolationLevel)null, false);
       }
       
       private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
           Transaction tx = null;
   
           DefaultSqlSession var8;
           try {
               Environment environment = this.configuration.getEnvironment();
               //事务工厂
               TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
               //通过事务工厂来产生一个事务
               tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
               //生成一个执行器(事务包含在执行器里)
               Executor executor = this.configuration.newExecutor(tx, execType);
               //然后产生一个DefaultSqlSession
               var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);
           } catch (Exception var12) {
               this.closeTransaction(tx);
               throw ExceptionFactory.wrapException("Error opening session.  Cause: " + var12, var12);
           } finally {
               ErrorContext.instance().reset();
           }
   
           return var8;
       }
   ```

   第一步生成事务

   第二步生成执行器，这里会选择一种执行器，另外，会调用插件，通过插件可以改变Executor行为。

   第三步，return new DefaultSqlSession(configuration, executor, autoCommit);

   这样sqlSession就生成了。

4. JDK动态代理UserMapper接口

   ```java
   在Test中
   GirlMapper mapper = sqlSession.getMapper(
                   GirlMapper.class
           );
   ```

   进入getMapper方法

   ```java
   DefaultSqlSession中
   public <T> T getMapper(Class<T> type) {
           return this.configuration.getMapper(type, this);
       }
   ```

   进入configuration.getMapper(type, this);方法

   ```java
   Configuration类中
   public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
           return this.mapperRegistry.getMapper(type, sqlSession);
       }
   ```

   进入mapperRegistry.getMapper(type, sqlSession);

   ```java
   MapperRegistry中
   public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
           MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
           if (mapperProxyFactory == null) {
               throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
           } else {
               try {
                   return mapperProxyFactory.newInstance(sqlSession);
               } catch (Exception var5) {
                   throw new BindingException("Error getting mapper instance. Cause: " + var5, var5);
               }
           }
       }
   ```

   进入mapperProxyFactory.newInstance(sqlSession);

   ```java
   MapperProxyFactory中
   protected T newInstance(MapperProxy<T> mapperProxy) {
   //JDK动态代理返回一个实现了指定接口的代理对象
           return Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{this.mapperInterface}, mapperProxy);
       }
   public T newInstance(SqlSession sqlSession) {
           MapperProxy<T> mapperProxy = new MapperProxy(sqlSession, this.mapperInterface, this.methodCache);
           //返回的mapperProxy传入this.newInstance(MapperProxy<T> mapperProxy);
           return this.newInstance(mapperProxy);
       }
   ```

   进入MapperProxy(sqlSession, this.mapperInterface, this.methodCache);

   ```java
    MapperProxy中
    public class MapperProxy<T> implements InvocationHandler, Serializable {
    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
           this.sqlSession = sqlSession;
           this.mapperInterface = mapperInterface;
           this.methodCache = methodCache;
       }
   //当UserMapper接口调用方法时就会进入invoke方法
       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           try {
               if (Object.class.equals(method.getDeclaringClass())) {
                   return method.invoke(this, args);
               }
   
               if (this.isDefaultMethod(method)) {
                   return this.invokeDefaultMethod(proxy, method, args);
               }
           } catch (Throwable var5) {
               throw ExceptionUtil.unwrapThrowable(var5);
           }
   
           MapperMethod mapperMethod = this.cachedMapperMethod(method);
           return mapperMethod.execute(this.sqlSession, args);
       }
   ```

5. 当调用Girl g = mapper.queryById(1l);时,进入invoke方法,再进入 mapperMethod.execute(this.sqlSession, args);

   ```java
   在MapperMethod中
   public Object execute(SqlSession sqlSession, Object[] args) {
           Object param;
           Object result;
           switch(this.command.getType()) {
           case INSERT:
               param = this.method.convertArgsToSqlCommandParam(args);
               result = this.rowCountResult(sqlSession.insert(this.command.getName(), param));
               break;
           case UPDATE:
               param = this.method.convertArgsToSqlCommandParam(args);
               result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
               break;
           case DELETE:
               param = this.method.convertArgsToSqlCommandParam(args);
               result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));
               break;
               //mapper.queryById(1l);是查询方法所以进入这里面
           case SELECT:
               if (this.method.returnsVoid() && this.method.hasResultHandler()) {
               //queryById返回值是null,进入这里
                   this.executeWithResultHandler(sqlSession, args);
                   result = null;
               } else if (this.method.returnsMany()) {
               //queryById返回值是集合,进入这里
                   result = this.executeForMany(sqlSession, args);
               } else if (this.method.returnsMap()) {
               //queryById返回值是Map,进入这里
                   result = this.executeForMap(sqlSession, args);
               } else if (this.method.returnsCursor()) {
               //queryById返回值是游标,进入这里
                   result = this.executeForCursor(sqlSession, args);
               } else {
               //由于我们的queryById方法返回是user对象,所以进入这里
                   param = this.method.convertArgsToSqlCommandParam(args);
                   result = sqlSession.selectOne(this.command.getName(), param);
               }
               break;
           case FLUSH:
               result = sqlSession.flushStatements();
               break;
           default:
               throw new BindingException("Unknown execution method for: " + this.command.getName());
           }
   
           if (result == null && this.method.getReturnType().isPrimitive() && !this.method.returnsVoid()) {
               throw new BindingException("Mapper method '" + this.command.getName() + " attempted to return null from a method with a primitive return type (" + this.method.getReturnType() + ").");
           } else {
               return result;
           }
       }
   ```

   进入sqlSession.selectOne(this.command.getName(), param);

   ```java
   DefaultSqlSession中
   public <T> T selectOne(String statement, Object parameter) {
           List<T> list = this.selectList(statement, parameter);
           if (list.size() == 1) {
               return list.get(0);
           } else if (list.size() > 1) {
               throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
           } else {
               return null;
           }
       }
       public <E> List<E> selectList(String statement, Object parameter) {
           return this.selectList(statement, parameter, RowBounds.DEFAULT);
       }
       public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
           List var5;
           try {
               MappedStatement ms = this.configuration.getMappedStatement(statement);
               //执行器executor执行query方法
               var5 = this.executor.query(ms, this.wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
           } catch (Exception var9) {
               throw ExceptionFactory.wrapException("Error querying database.  Cause: " + var9, var9);
           } finally {
               ErrorContext.instance().reset();
           }
   
           return var5;
       }
   ```

   执行器executor里面封装了JDBC代码,通过JDBC对数据库进行操作

   

其他源码(https://blog.csdn.net/nmgrd/article/details/54608702)和(https://www.cnblogs.com/demingblog/p/9544774.html)

mybaits使用XPath解析mapper的配置文件后将其中的resultMap、parameterMap、cache、statement等节点使用关联的builder创建并将得到的对象关联到configuration对象中，而这个configuration对象可以从sqlSession中获取的，这就解释了我们在使用sqlSession对数据库进行操作时mybaits怎么获取到mapper并执行其中的sql语句的问题。
原文链接：https://blog.csdn.net/flashflight/article/details/43926091

### 仿写MYBATIS底层

![img](F:\Typora\EasyDevelopment\Mybatis\mybatisResource\img\重写mybatis.png)

#### 大概步骤

分析完源码后，自己写一个这样的框架
1、解析配置文件–UserMapper.xml的解析工具类
硬编码：直接写死在那里

UserMapper.xml
namespace
map key-value

2、实现动态代理–MapperProxy

MapperProxy
实现的invokahandler
invoke方法method,args

3、加载代理对象并执行SQL–SqlSession

SqlSession
T getMapper
T selectOne(String sql,)

4、封装JDBC–Executor接口和它的实现SimpleExecutor

Executor
query

5、mybatis的mapper接口–UserMapper类

6、映射数据库实体–User类

7、测试类–Test类



#### 详细步骤

新建项目,导入mysql的依赖

MyBatisXml类

```java
package com.yy.entity;
import java.util.HashMap;
import java.util.Map;
public class MyBatisXml {
    public static String namespace="com.yy.mapper.UserMapper";
    public static Map<String,String> map=new HashMap<>();
    static {
        map.put("selectById","select * from users where id = ?");
    }
}
```

User类

```java
package com.yy.entity;

public class User {
    private int id;
    private String username;
    private String password;
    //get set tostring 已经省略
}
```

Executor接口

```java
package com.yy.executor;
public interface Executor {
    <T> T query(String sql,Object o);
}
```

SimpleExecutor接口

```java
package com.yy.executor;
import com.yy.entity.User;
import java.sql.*;
public class SimpleExecutor implements Executor {
    @Override
    public <T> T query(String sql, Object o) {
        Connection c = getConnection();
        try {
            PreparedStatement statement=c.prepareStatement(sql);
            statement.setInt(1,(Integer) o);
            ResultSet resultSet = statement.executeQuery();
            User u=null;
            while(resultSet.next()){
                u=new User();
                u.setId(resultSet.getInt("id"));
                u.setUsername(resultSet.getString("username"));
                u.setPassword(resultSet.getString("password"));
            }
            return (T)u;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Connection getConnection(){
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/user?useSSL=false&serverTimezone=UTC";
        String username="root";
        String password="root";
        try {
            Class.forName(driver);
            Connection c= DriverManager.getConnection(url,username,password);
            return c;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

SqlSession类

```java
package com.yy.executor;
import com.yy.mapper.MapperProxy;
import java.lang.reflect.Proxy;
public class SqlSession {
private Executor e=new SimpleExecutor();
public <T> T selectOne(String sql,Object o){
    return e.query(sql,o);
}
public <T> T getMapper(Class<T> tClass){
//第一个参数:类加载器,在类类型上调用getClassLoader()方法是得到当前类型的类加载器，我们知道在Java中所有的类都是通过加载器加载到虚拟机中的
    //第二个参数:the list of interfaces for the proxy class to implement
    //第三个参数:InvocationHandler动态代理
    //因为被代理的接口是UserMapper,所以需要UserMapper的类加载器
    return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass},new MapperProxy(this));
}
}
```

MapperProxy类

```java
package com.yy.mapper;
import com.yy.entity.MyBatisXml;
import com.yy.entity.User;
import com.yy.executor.SqlSession;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class MapperProxy implements InvocationHandler {
    private SqlSession sqlSession;
    public MapperProxy(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        System.out.println("-------"+method);
        if(method.getDeclaringClass().getName().equals(MyBatisXml.namespace)){
            String sql = MyBatisXml.map.get(method.getName());
             return sqlSession.selectOne(sql,args[0]);
        }
        return null;
    }
}
```

测试类

```java
@Test
    public void test01(){
        SqlSession session=new SqlSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectById(1);
        System.out.println(user);
    }
```



#### 问题

调试仿写MYBATIS底层的代码的时候,出现代理对象为null,MapperProxy类中invoke方法的 System.out.println("-------"+method);重复输出?

单步调试时IDEA会调用被代理类的toString()方法，调用一次被代理类的toString()方法就会进入一次invoke方法，因此会重复输出。invoke方法中method参数是代理类的toString,invoke的返回值就是对应的代理类(因为代理的是toString方法)

#### 仿写代码深度解析

我们通过指定虚拟机启动参数，让它保存下来生成的代理类的 Class 文件。

-Dsun.misc.ProxyGenerator.saveGeneratedFiles=true

通过IDEA反编译这个Class

```java
package com.sun.proxy;
import com.yy.entity.User;
import com.yy.mapper.UserMapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
public final class $Proxy0 extends Proxy implements UserMapper {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;
    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final User selectById(int var1) throws  {
        try {
            return (User)super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("com.yy.mapper.UserMapper").getMethod("selectById", Integer.TYPE);
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

代码较多,我们拆开来看

```java
public final class $Proxy0 extends Proxy implements UserMapper {
    //首先，这个代理类的名字是很随意的，一个程序中如果有多个代理类要生成，「$Proxy + 数字」就是它们的类名。
//接着，你会注意到这个代理类继承 Proxy 类和我们指定的接口 UserMapper（之前如果指定多个接口，这里就会继承多个接口）。
    //然后你会发现，这个构造器需要一个 InvocationHandler 类型的参数，并且构造器的主体就是将这个 InvocationHandler 实例传递到父类 Proxy 的对应字段进行保存，这也是为什么所有的代理类都必须使用 Proxy 作为父类的一个原因，就是为了公用父类中的 InvocationHandler 字段。
    
    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }
```

```java
//这一块内容也算是代理类中较为重要的部分了，它将于虚拟机静态初始化这个代理类的时候执行。这一大段代码就是完成反射出所有接口中方法的功能，所有被反射出来的方法都对应一个 Method 类型的字段进行存储。
//除此之外，虚拟机还反射了 Object 中的三个常用方法，也就是说，代理类还会代理真实对象从 Object 那继承来的这三个方法。
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;
static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("com.yy.mapper.UserMapper").getMethod("selectById", Integer.TYPE);
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
```

```java
//从父类 Proxy 中取出构造实例化时存入的处理器类，并调用它的 invoke 方法。
//方法的参数基本一样，第一个参数是当前代理类实例（事实证明这个参数传过去并没什么用），第二个参数是 Method 方法实例，第三个参数是方法的形式参数集合，如果没有就是 null。
public final User selectById(int var1) throws  {
        try {
            return (User)super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }
```

JDK动态代理的不足之处

1. 虚拟机生成的代理类为了公用 Proxy 类中的 InvocationHandler 字段来存储自己的处理器类实例而继承了 Proxy 类，那说明了什么？
    Java 的单根继承告诉你，代理类不能再继承任何别的类了，那么被代理类父类中的方法自然就无从获取，即代理类无法代理真实类中父类的任何方法。

2. JDK 的动态代理机制是单一的，它只能代理被代理类的接口集合中的方法,如果是被代理类自己声明的方法,则不会被代理

3. newProxyInstance 返回的是代理类 「$Proxy0」 的一个实例，但是它是以 Object 类型进行返回的，而你又不能强转这个 Object 实例到 「$Proxy0」 类型。
虽然我们知道这个 Object 实例其实就是 「$Proxy0」 类型，但编译期是不存在这个 「$Proxy0」 类型的，编译器自然不会允许你强转为一个不存在的类型了。所以一般只会强转为该代理类实现的接口之一。
那么问题又来了，假如我们的被代理类实现了多个接口，请问你该强转为那个接口类型，现在假设被代理类实现了接口 A 和 B，那么最后的实例如果强转为 A ，自然被代理类所实现的接口 B 中所有的方法你都不能调用，反之亦然。