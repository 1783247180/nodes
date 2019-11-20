https://www.cnblogs.com/EmilZs/p/9197020.html

### 一、mapper接口中的方法解析

mapper接口中的函数及方法

| 方法                                                         | 功能说明                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| int countByExample(UserExample example) thorws SQLException  | 按条件计数                                                   |
| int deleteByPrimaryKey(Integer id) thorws SQLException       | 按主键删除                                                   |
| int deleteByExample(UserExample example) thorws SQLException | 按条件查询                                                   |
| String/Integer insert(User record) thorws SQLException       | 插入数据（返回值为ID）                                       |
| User selectByPrimaryKey(Integer id) thorws SQLException      | 按主键查询                                                   |
| ListselectByExample(UserExample example) thorws SQLException | 按条件查询                                                   |
| ListselectByExampleWithBLOGs(UserExample example) thorws SQLException | 按条件查询（包括BLOB字段）。只有当数据表中的字段类型有为二进制的才会产生。 |
| int updateByPrimaryKey(User record) thorws SQLException      | 按主键更新                                                   |
| int updateByPrimaryKeySelective(User record) thorws SQLException | 按主键更新值不为null的字段                                   |
| int updateByExample(User record, UserExample example) thorws SQLException | 按条件更新                                                   |
| int updateByExampleSelective(User record, UserExample example) thorws SQLException | 按条件更新值不为null的字段                                   |

### 二、example实例解析

mybatis的逆向工程中会生成实例及实例对应的example，example用于添加条件，相当where后面的部分 
xxxExample example = new xxxExample(); 
Criteria criteria = new Example().createCriteria();

| 方法                                       | 说明                                          |
| ------------------------------------------ | --------------------------------------------- |
| example.setOrderByClause(“字段名 ASC”);    | 添加升序排列条件，DESC为降序                  |
| example.setDistinct(false)                 | 去除重复，boolean型，true为选择不重复的记录。 |
| criteria.andXxxIsNull                      | 添加字段xxx为null的条件                       |
| criteria.andXxxIsNotNull                   | 添加字段xxx不为null的条件                     |
| criteria.andXxxEqualTo(value)              | 添加xxx字段等于value条件                      |
| criteria.andXxxNotEqualTo(value)           | 添加xxx字段不等于value条件                    |
| criteria.andXxxGreaterThan(value)          | 添加xxx字段大于value条件                      |
| criteria.andXxxGreaterThanOrEqualTo(value) | 添加xxx字段大于等于value条件                  |
| criteria.andXxxLessThan(value)             | 添加xxx字段小于value条件                      |
| criteria.andXxxLessThanOrEqualTo(value)    | 添加xxx字段小于等于value条件                  |
| criteria.andXxxIn(List<？>)                | 添加xxx字段值在List<？>条件                   |
| criteria.andXxxNotIn(List<？>)             | 添加xxx字段值不在List<？>条件                 |
| criteria.andXxxLike(“%”+value+”%”)         | 添加xxx字段值为value的模糊查询条件            |
| criteria.andXxxNotLike(“%”+value+”%”)      | 添加xxx字段值不为value的模糊查询条件          |
| criteria.andXxxBetween(value1,value2)      | 添加xxx字段值在value1和value2之间条件         |
| criteria.andXxxNotBetween(value1,value2)   | 添加xxx字段值不在value1和value2之间条件       |

### 三、应用举例

#### 1.查询

① selectByPrimaryKey()

```java
 1 User user = XxxMapper.selectByPrimaryKey(100); //相当于select * from user where id = 100 
```

② selectByExample() 和 selectByExampleWithBLOGs()

```java
1 UserExample example = new UserExample();
2 Criteria criteria = example.createCriteria();
3 criteria.andUsernameEqualTo("wyw");
4 criteria.andUsernameIsNull();
5 example.setOrderByClause("username asc,email desc");
6 List<?>list = XxxMapper.selectByExample(example);
7 //相当于：select * from user where username = 'wyw' and  username is null order by username asc,email desc
```

注：在iBator逆向工程生成的文件XxxExample.java中包含一个static的内部类Criteria，Criteria中的方法是定义SQL 语句where后的查询条件。

#### 2.插入数据

①insert()

```java
1 User user = new User();
2 user.setId("dsfgsdfgdsfgds");
3 user.setUsername("admin");
4 user.setPassword("admin")
5 user.setEmail("wyw@163.com");
6 XxxMapper.insert(user);
7 //相当于：insert into user(ID,username,password,email) values ('dsfgsdfgdsfgds','admin','admin','wyw@126.com');
```

#### 3.更新数据

①updateByPrimaryKey()

```java
1 User user =new User();
2 user.setId("dsfgsdfgdsfgds");
3 user.setUsername("wyw");
4 user.setPassword("wyw");
5 user.setEmail("wyw@163.com");
6 XxxMapper.updateByPrimaryKey(user);
7 //相当于：update user set username='wyw', password='wyw', email='wyw@163.com' where id='dsfgsdfgdsfgds'
```

②updateByPrimaryKeySelective()

```java
1 User user = new User();
2 user.setId("dsfgsdfgdsfgds");
3 user.setPassword("wyw");
4 XxxMapper.updateByPrimaryKey(user);
5 //相当于：update user set password='wyw' where id='dsfgsdfgdsfgds'
```

③ updateByExample() 和 updateByExampleSelective()

```java
1 UserExample example = new UserExample();
2 Criteria criteria = example.createCriteria();
3 criteria.andUsernameEqualTo("admin");
4 User user = new User();
5 user.setPassword("wyw");
6 XxxMapper.updateByPrimaryKeySelective(user,example);
7 //相当于：update user set password='wyw' where username='admin'
```

updateByExample()更新所有的字段，包括字段为null的也更新，建议使用 updateByExampleSelective()更新想更新的字段

#### 4.删除数据

①deleteByPrimaryKey()

```java
 1 XxxMapper.deleteByPrimaryKey(1); //相当于：delete from user where id=1 
```

②deleteByExample()

```java
1 UserExample example = new UserExample();
2 Criteria criteria = example.createCriteria();
3 criteria.andUsernameEqualTo("admin");
4 XxxMapper.deleteByExample(example);
5 //相当于：delete from user where username='admin'
```

 

#### 5.查询数据数量

①countByExample()

```java
1 UserExample example = new UserExample();
2 Criteria criteria = example.createCriteria();
3 criteria.andUsernameEqualTo("wyw");
4 int count = XxxMapper.countByExample(example);
5 //相当于：select count(*) from user where username='wyw'
```