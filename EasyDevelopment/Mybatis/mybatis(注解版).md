## 介绍

主要讲解查询,其他部分看https://blog.csdn.net/winter_chen001/article/details/78623700

## 单表查询

返回结果绑定
对于增、删、改操作相对变化较小。而对于“查”操作，我们往往需要进行多表关联，汇总计算等操作，那么对于查询的结果往往就不再是简单的实体对象了，往往需要返回一个与数据库实体不同的包装类，那么对于这类情况，就可以通过@Results和@Result注解来进行绑定，具体如下:

```java

@Results(id = "userMap",value={
    @Result(id=true,property = "sid",column = "SID"),
    @Result(property="name",column="NAME"),
    @Result(property="password",column="PASSWORD"),
    @Result(property="phone",column="PHONE")
})
@Select("select SID,NAME,PASSWORD,PHONE from user")
List<User> findAll();

@Select("select SID,NAME,PASSWORD,PHONE from user where sid=#{id}")
@ResultMap("userMap")
User findById(Integer integer);
```

在上面代码中，@Result中的property属性对应User对象中的成员名，column对应SELECT出的字段名。

## 多表查询

### 多对一(一对一)

可以这么记忆：
你所需要对应的表是很多个注解用many
对应一个的话就用one

demo功能阐述
查询用户账户的时候 返回用户的信息
因为多个账户或者一个账户才对应一个用户

```java
public interface IAccountDao {
    /**
     * 查询所有账户，并且获取每个账户所属的用户信息
     * @return
     */
    @Select("select * from account")
    @Results(id="accountMap",value = {
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "money",property = "money"),
            //FetchType(加载时机)  EAGER(立即加载)LAZY(延时加载)
            @Result(property = "user",column = "uid",one=@One(select="com.itheima.dao.IUserDao.findById",fetchType= FetchType.EAGER))
    })
    List<Account> findAll();
}
```

### 一对多(多对多)

demo功能阐述
查询用户信息的时候 返回用户的账户信息
因为一个用户对应多个用户的账户信息

使用的是延迟加载

IUserDao接口中使用注解

```java
public interface IUserDao {
/**
 * 查询所有用户
 * @return
 */
@Select("select * from user")
@Results(id="userMap",value={
        @Result(id=true,column = "id",property = "userId"),
        @Result(column = "username",property = "userName"),
        @Result(column = "address",property = "userAddress"),
        @Result(column = "sex",property = "userSex"),
        @Result(column = "birthday",property = "userBirthday"),
        @Result(property = "accounts",column = "id",
                many = @Many(select = "com.itheima.dao.IAccountDao.findAccountByUid",
                            fetchType = FetchType.LAZY))
})
List<User> findAll();
/**
 * 根据id查询用户
 * @param userId
 * @return
 */
@Select("select * from user  where id=#{id} ")
@ResultMap("userMap")
User findById(Integer userId);
/**
 * 根据用户名称模糊查询
 * @param username
 * @return
 */
@Select("select * from user where username like #{username} ")
@ResultMap("userMap")
List<User> findUserByName(String username);
}
```



## 测试类

```java
public void m2() {
        SqlSession sqlSession = MybatisUtil.getSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserExample userExample=new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
    //sql语句where后面的条件
        criteria.andUserpassword1EqualTo("123456");
        List<User> users = mapper.selectByExample(userExample);
        for (User u:
             users) {
            System.out.println("----"+u);
        }
        sqlSession.commit();
        sqlSession.close();
    }
```

