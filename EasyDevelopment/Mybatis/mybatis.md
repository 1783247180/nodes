# Mybatis基础

增删改是有必要commit,查询无所谓



# mybatis配置文件详解(mybatis.cfg.xml)

## environments

### environment

1. DataSource

   pooled-->连接池

2. TransactionManager

   JDBC-->以JDBC的方式对数据库提交与回滚事务

## properties

property

属性优先级问题

## typeAliases

别名(不建议使用)

```java
<typeAliases>
    <typeAlias alias="User" type="org.apache.ibatis.submitted.complex_property.User"/>
  </typeAliases>
```



## typeHandlers

一般情况下我们不需要额外添加

## settings

默认配置都是友好的

## mappers

mapper的数量与javabean一致

```java
<mappers>
    <mapper resource="com.sz.mapper/GirlMapper.xml"/>
  </mappers>
  //下面是指定Mapper接口的包
  <mappers>
    <package name="com.mapper"/>
  </mappers>
```

# mybatis参数问题(eclipse)

```java
<mapper namespace="com.sz.mapper.GirlMapper">//namespace属性必须存在不可省略。

<insert id="insert" parameterType="com.sz.pojo.Girl">
    insert into girl(name,flower,birthday) values(#{name},#{flower},#{birthday})
</insert>

 <!-- resultMap是接收select标签查询到的结果 -->
<resultMap type="entity.Message" id="MessageResult">
<!--id是主键,其他的都用result-->
<!-- column是数据库中的对应字段, property是javabean中的对应的值,jdbcType是对应的数据类型-->
    <id column="id" jdbcType="INTEGER" property="id"/>
    <result column="username" jdbcType="VARCHAR" property="username"/>
    <result column="password" jdbcType="VARCHAR" property="password"/>
  </resultMap>
  
  <select id="queryMessageList" parameterType="entity.Message" resultMap="MessageResult">
    select id,username,password from t_user 
    <where>
    <!-- &quot;是"的转义符
        &amp;是&的转义符,但是&&可以用and代替 -->
    <!-- 第一个and会自动省略 -->
    <if test="password != null and !&quot;&quot;.equals(password)">
    and password like '%' #{password} '%'
    </if>
    </where>
  </select>
  
  <delete id="deleteOne" parameterType="int">
  	delete from t_user where ID = #{_parameter}
  </delete>
</mapper>
```

mapper拥有5种子标签select insert update delete resultMap

所有标签的parameterType都是传进去参数的类型,如果parameterType的类型是String或者基本数据类型,则里面的sql语句的参数使用_parameter,如果是javabean类型则直接使用javabean中的字段名

如果namespace名不同，则id可以一样，调用的时候，namespace名.id名；若namespace名相同，则id不能一样。

# mybatis参数问题(IDEA)

```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.sz.mapper.GirlMapper">//namespace属性必须是注册的Mapper。
<insert id="insert">
    insert into girl(name,flower,birthday) values(#{name},#{flower},#{birthday})
</insert>
<select id="queryById" resultType="com.sz.pojo.Girl">
    select * from girl where id=#{id}
</select>
</mapper>
```

mapper拥有4种子标签select insert update delete ,insert update delete只需要指定id属性,select只需要指定id,resultType属性,对于parameterType,这是入参类型,绝大部分情况下能够自我推断

# 传参问题

## 单个基本数据类型与String

如果仅仅是简单的一个单值传入,那么#{}表达式里面随便写什么都可以,只有一个参数,mybatis没有入参绑定的烦恼

## 多个基本数据类型或String入参问题

1. 默认xml中的#{}里面放param1,param2或者arg0,arg1默认配置很不友好,不推荐使用

2. 注解模式

   ```xml
   GirlMapper中:
   Girl queryByNameFlower(@Param("name") String name,@Param("flower") String flower);
   GirlMapper.xml中:
   <select id="queryByNameFlower" resultType="com.sz.pojo.Girl">
    select * from girl where name=#{name} and flower=#{flower}
   </select>
   ```

   

## 单个Javabean

默认通过javabean里面的属性的名称去引用,通过get方法去找这个值

## MAP

```java
GrilMapper中:
Girl queryByNameFlowerMap(Map<String,Object> map);
GrilMapper.xml中:
<select id="queryByNameFlowerMap" resultType="com.sz.pojo.Girl">
        select * from girl where name=#{username} and flower=#{loveflower}
</select>
Test01中:
SqlSession sqlSession= MybatisUtil.getSession();
        GirlMapper mapper=sqlSession.getMapper(GirlMapper.class);
        Map<String,Object> map=new HashMap<>();
        map.put("username","如花");
        map.put("loveflower","hhhh");
        Girl g=mapper.queryByNameFlowerMap(map);
        System.out.println(g.getBirthday());
        sqlSession.commit();
        sqlSession.close();
```



## 多个Javabean

# 动态SQL

## OGNL

- if
- choose (when, otherwise)choose里面的when,自动匹配第一个满足的when
- trim (where, set)
- foreach

http://www.mybatis.org/mybatis-3/zh/dynamic-sql.html

bind标签(解决模糊查询)

```xml
<select id="queryByName" resultType="com.sz.pojo.Girl">
        <bind name="bin" value="'%'+name+'%'"></bind>
        select * from girl
        <where>
            name like #{bin}
        </where>
  </select>
```

sql标签(减少重复代码)

```xml
<sql id="baseColumn">
       name,flower,birthday
    </sql>
    <select id="queryAll" resultType="com.sz.pojo.Girl">
        select
        <include refid="baseColumn"></include>
        from girl
    </select>
```

## Mybatis配置log4j

1. 添加依赖

```xml
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

2. 在resources里面新建一个log4j.properties

```properties
# Global logging configuration
log4j.rootLogger=ERROR, stdout
# MyBatis logging configuration...,com.sz.mapper是GirlMapper的包名
log4j.logger.com.sz.mapper=DEBUG
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

# 缓存

一级缓存(存储作用域为 Session):

MyBatis中一级缓存是默认开启的，即在查询中(一次SqlSession中)。只要当SqlSession不关闭，那么你的操作会默认存储使用一级缓存。

二级缓存(存储作用域为 Mapper(Namespace)):

```xml
mybatis.cfg.xml中:
<!--开启二级缓存-->
<settings>
    <setting name="cacheEnabled" value="true"/>
</settings>
GirlMapper.xml中:
<mapper namespace="com.sz.mapper.GirlMapper">
<cache/>
</mapper>
```

如果开启了二级缓存,先去二级缓存中尝试命中,如果也无法命中,尝试去一级缓存中尝试命中,还不命中,再去数据库里面查询

二级缓存例子

```java
SqlSession sqlSession= MybatisUtil.getSession();
        SqlSession sqlSession2= MybatisUtil.getSession();
        GirlMapper mapper=sqlSession.getMapper(
                GirlMapper.class
        );
        GirlMapper mapper2=sqlSession2.getMapper(
                GirlMapper.class
        );
        //使用二级缓存时，Girl类必须实现一个Serializable接口===> Girl implements Serializable
        List<Girl> list = mapper.queryByName("花");
        for (Girl g: list) {System.out.println(g.getName());}
        sqlSession.commit();
        /*一定要提交事务之后二级缓存才会起作用
        因为，二级缓存是从cache（mapper.xml中定义的cache）中取得
        如果session不commit，那么，数据就不会放入cache中*/
        mapper2.queryByName("花");
        //由于使用的是两个不同的SqlSession对象，所以即使查询条件相同，一级缓存也不会开启使用
        sqlSession2.commit();
        sqlSession.close();
        sqlSession2.close();
```



放入二级缓存区的对象必须序列化

缓存失效方式:

1. 如果查询之后进行增删改的行为,将导致缓存失效
2. 强制清空缓存,SqlSession.clearCache();

# 对应关系

## 一对一

比如girl表对于girldetail表

```xml
<resultMap id="SelectMap" type="com.sz.pojo.GirlDetail">
<id column="id" jdbcType="BIGINT" property="id"></id>
        <result column="gid" jdbcType="BIGINT" property="gid"></result>
        <result column="phone" jdbcType="VARCHAR" property="phone" ></result>
        <result column="address" jdbcType="VARCHAR" property="address" ></result>
        <association property="girl" javaType="com.sz.pojo.Girl">
            <id column="id" jdbcType="BIGINT" property="id"></id>
            <result column="name" jdbcType="VARCHAR" property="name"></result>
            <result column="flower" jdbcType="VARCHAR" property="flower" ></result>
            <result column="birthday" jdbcType="DATE" property="birthday" ></result>
        </association>
    </resultMap>
    <select id="queryByIdAndGid" resultMap="SelectMap">
       select * from girldetail as b ,girl as a where a.id=#{id} and b.gid =#{id}
    </select>
```

有个问题就是girl与girldetail表具有同名字段id,那么查询的id是哪个的呢?

在sql中,哪个表名在前面就是哪个表的id值,这里是girldetail在前面

解决方案

```xml
<resultMap id="SelectMap" type="com.sz.pojo.GirlDetail">
<id column="gd" jdbcType="BIGINT" property="id"></id>
        <result column="gid" jdbcType="BIGINT" property="gid"></result>
        <result column="phone" jdbcType="VARCHAR" property="phone" ></result>
        <result column="address" jdbcType="VARCHAR" property="address" ></result>
        <association property="girl" javaType="com.sz.pojo.Girl">
            <id column="g" jdbcType="BIGINT" property="id"></id>
            <result column="name" jdbcType="VARCHAR" property="name"></result>
            <result column="flower" jdbcType="VARCHAR" property="flower" ></result>
            <result column="birthday" jdbcType="DATE" property="birthday" ></result>
        </association>
    </resultMap>
    <select id="queryByIdAndGid" resultMap="SelectMap">
    SELECT b.id AS gd,a.id AS g FROM girldetail AS b ,girl AS a WHERE a.id=#{id} and b.gid =#{id}
    </select>
```

注意resultMap可以继承

## 分步查询



## 一对多

```xml
<resultMap id="BaseMap" type="com.sz.pojo.Girl">
        <id column="id" jdbcType="BIGINT" property="id"></id>
        <result column="name" jdbcType="VARCHAR" property="name"></result>
        <result column="flower" jdbcType="VARCHAR" property="flower" ></result>
        <result column="birthday" jdbcType="DATE" property="birthday" ></result>
    </resultMap>
    <resultMap id="AllMap" extends="BaseMap" type="com.sz.pojo.All">
        <collection property="blogList" ofType="com.sz.pojo.Blog">
            <result column="title" jdbcType="VARCHAR" property="title"></result>
            <result column="summary" jdbcType="VARCHAR" property="summary" ></result>
            <result column="content" jdbcType="VARCHAR" property="content" ></result>
            <collection property="commentList" ofType="com.sz.pojo.Comment">
                <result column="contents" jdbcType="VARCHAR" property="contents" ></result>
            </collection>
        </collection>
    </resultMap>
    <select id="queryAllData" resultMap="AllMap">
        SELECT * FROM girl AS t1,blog AS t2,COMMENT AS t3 WHERE t1.id=t2.u_id AND t2.id=t3.b_id;
    </select>
```

javabean的设计:

```java
public class All extends Girl{
private List<Blog> blogList;
}
public class Blog {
    private int id;
    private String title;
    private String summary;
    private String content;
    private int u_id;
private List<Comment> commentList;
}
public class Comment {
    private int id;
    private String contents;
    private int b_id;
}
```

## 数据库表设计与javabean设计

不管是一对一还是一对多的关系，外键约束都是在子表里面添加，想删除主表一条数据时，必须要把子表里对应的约束数据先删掉

如果是一对一:在子表的javabean中加一个父表的javabean的对象作为属性

如果是一对多:在父表的javabean中加一个集合，里面放子表对应的javabean的对象作为属性