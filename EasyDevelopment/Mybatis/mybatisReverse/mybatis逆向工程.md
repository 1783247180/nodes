# MYBATIS逆向工程1

## ORACLE

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.yy</groupId>
  <artifactId>mybatis-reverse-oracle</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>mybatis-reverse-oracle</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
      <dependency>
          <groupId>org.mybatis.generator</groupId>
          <artifactId>mybatis-generator-core</artifactId>
          <version>1.3.7</version>
      </dependency>
      <!--mybatis包里含有所有mybatis的注解(在mapper接口中会用到)-->
      <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
  </dependencies>
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.7.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.0.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>

```

### 添加ODBC

在项目下建立lib文件夹,并且加入odbc的jar包,并且将lib作为项目文件夹

![1560133894419](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133894419.png)

![1560133946028](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133946028.png)

![1560133989836](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133989836.png)

### 添加文件generatorConfig.xml

oracle版:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <classPathEntry location="D:\Java\mybatisReverse\mybatsreverseoracle\lib\ojdbc8.jar" />
	<context id="caigouTables" targetRuntime="MyBatis3">
		<commentGenerator>
			<!-- 是否去除自动生成的注释 true：是 ： false:否 -->
			<property name="suppressAllComments" value="true" />
		</commentGenerator>
		<!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
		<jdbcConnection driverClass="oracle.jdbc.OracleDriver"
			connectionURL="jdbc:oracle:thin:@127.0.0.1:1521:orcl"
			userId="c##user"
			password="system">
		</jdbcConnection>
		<!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer true，把JDBC DECIMAL 和 
			NUMERIC 类型解析为java.math.BigDecimal -->
		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>
		<!-- 生成模型的包名和位置-->
		<javaModelGenerator targetPackage="com.yy.pojo" targetProject="src/main/java">
			<property name="enableSubPackages" value="true" />
			<property name="trimStrings" value="true" />
		</javaModelGenerator>
		<!-- 生成映射文件的包名和位置-->
		<sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources">
			<property name="enableSubPackages" value="true" />
		</sqlMapGenerator>
		<!-- 生成DAO的包名和位置-->
        <!-- 客户端代码，生成易于使用的针对Model对象和XML配置文件 的代码
                type="ANNOTATEDMAPPER",生成Java Model 和基于注解的Mapper对象annotatedmapper
                type="MIXEDMAPPER",生成Java Model 和相应基于注解和xml混合使用的Mapper对象
                type="XMLMAPPER",生成SQLMap XML文件和独立的Mapper接口
        -->
		<javaClientGenerator type="XMLMAPPER" targetPackage="com.yy.mapper"  targetProject="src/main/java">
			<property name="enableSubPackages" value="true" />
		</javaClientGenerator>
		<!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
		<!-- 系统用户
		这里不指定schema，逆向工程会查询c##user都有哪些schema(其实就是table)，对每个schema生成对象
		 -->
		<table schema="" tableName="countries" />
		<table schema="" tableName="departments" />
		<table schema="" tableName="employees" />
		<table schema="" tableName="jobs" />
		<table schema="" tableName="job_history" />
		<table schema="" tableName="locations" />
		<table schema="" tableName="regions" />
		<table schema="" tableName="sal_grades" />
		<table schema="" tableName="users" />
	</context>
</generatorConfiguration>
```

![1568088493465](D:\Java\mybatisReverse\img\1568088493465.png)

### 添加类GeneratorSqlmap

```java
package com.yy;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class GeneratorSqlmap {
    public void generator() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件,文件必须指定为当前项目下的generatorConfig.xml
        File configFile = new File("G:/xunlei/mybatisreverseoracle/src/main/resources/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }
    public static void main(String[] args) throws Exception {
        //运行main方法即可
        try {
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## MYSQL

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.yy</groupId>
  <artifactId>mybatis-reverse-oracle</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>mybatis-reverse-oracle</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
      <dependency>
          <groupId>org.mybatis.generator</groupId>
          <artifactId>mybatis-generator-core</artifactId>
          <version>1.3.7</version>
      </dependency>
      <!--mybatis包里含有所有mybatis的注解(在mapper接口中会用到)-->
      <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
      <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
            <scope>runtime</scope>
        </dependency>
  </dependencies>
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.7.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.0.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>

```

### 添加文件generatorConfig.xml

mysql版:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--MySQL连接驱动-->
    <classPathEntry location="D:\reposi\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar" />
    <!--数据库链接URL，用户名、密码 -->
    <context id="MySQL" targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://120.79.14.47:3306/LaborProject?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai&amp;useSSL=false"
                        userId="LaborProject"
                        password="Kermi">
        </jdbcConnection>
        <!--是否启用java.math.BigDecimal-->
        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="com.sz.pojo" targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- 生成DAO的包名和位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.sz.mapper"  targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
        <table tableName="article_image_table" domainObjectName="Image"/>
        <table tableName="article_table" domainObjectName="Article"/>
        <table tableName="class_stu_table" domainObjectName="Cla_stu"/>
        <table tableName="class_table" domainObjectName="TableClass"/>
        <table tableName="collection_table" domainObjectName="Collection"/>
        <table tableName="comment_article_table" domainObjectName="Comment"/>
        <table tableName="laboratory_member_table" domainObjectName="lab_member"/>
        <table tableName="laboratory_table" domainObjectName="Laboratory"/>
        <table tableName="options_table" domainObjectName="Options"/>
        <table tableName="partition_table" domainObjectName="Partition"/>
        <table tableName="permission_table" domainObjectName="Permission"/>
        <table tableName="role_name_table" domainObjectName="Rolename"/>
        <table tableName="t_comment_article_table" domainObjectName="TComment"/>
        <table tableName="teacher_student_table" domainObjectName="Tea_stu"/>
        <table tableName="teacher_table" domainObjectName="Teacher"/>
        <table tableName="teamwork_table" domainObjectName="Teamwork"/>
        <table tableName="university_table" domainObjectName="University"/>
        <table tableName="user_baseinfo_table" domainObjectName="User"/>
    </context>
</generatorConfiguration>
```



### 添加类GeneratorSqlmap

```java
package com.yy;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class GeneratorSqlmap {
    public void generator() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件,文件必须指定为当前项目下的generatorConfig.xml
        File configFile = new File("G:/xunlei/mybatisreverseoracle/src/main/resources/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }
    public static void main(String[] args) throws Exception {
        //运行main方法即可
        try {
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

# MYBATIS逆向工程2(优先)

## MYSQL

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>edu.hunau.cxb</groupId>
    <artifactId>mybatis-reverse</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <!--配置项目的字符编码-->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!--配置使用jdk的版本-->
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.7</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
            <scope>runtime</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.7</version>
            </plugin>
        </plugins>
    </build>
</project>
```



### 添加文件generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--MySQL连接驱动-->
    <classPathEntry location="D:\reposi\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar" />
    <!--数据库链接URL，用户名、密码 -->
    <context id="MySQL" targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://127.0.0.1:3306/shopping?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai&amp;useSSL=false"
                        userId="root"
                        password="root">
        </jdbcConnection>
        <!--是否启用java.math.BigDecimal-->
        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="edu.hunau.cxb.pojo" targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="edu.hunau.cxb.mapper"  targetProject="src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- 生成DAO的包名和位置-->
        <javaClientGenerator type="ANNOTATEDMAPPER" targetPackage="edu.hunau.cxb.mapper"  targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
        <table tableName="car" domainObjectName="Car" />
        <table tableName="goods" domainObjectName="Goods" />
        <table tableName="producer" domainObjectName="Product" />
        <table tableName="user" domainObjectName="User" />
    </context>
</generatorConfiguration>
```



### 操作介绍

![1568081777334](D:\Java\mybatisReverse\img\1568081777334.png)

## ORCLE

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.yy</groupId>
  <artifactId>mybats-reverse-oracle</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>mybats-reverse-oracle</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mybatis.generator</groupId>
      <artifactId>mybatis-generator-core</artifactId>
      <version>1.3.7</version>
    </dependency>
    <!--mybatis包里含有所有mybatis的注解(在mapper接口中会用到)-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.4.6</version>
    </dependency>
  </dependencies>
  <build>
      <plugins>
          <plugin>
              <groupId>org.mybatis.generator</groupId>
              <artifactId>mybatis-generator-maven-plugin</artifactId>
              <version>1.3.7</version>
          </plugin>
      </plugins>
  </build>
</project>
```



### 添加ODBC

在项目下建立lib文件夹,并且加入odbc的jar包,并且将lib作为项目文件夹

![1560133894419](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133894419.png)

![1560133946028](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133946028.png)

![1560133989836](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560133989836.png)

### generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <classPathEntry location="D:\Java\mybatisReverse\mybatsreverseoracle\lib\ojdbc8.jar" />
    <context id="caigouTables" targetRuntime="MyBatis3">
        <commentGenerator>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="oracle.jdbc.OracleDriver"
                        connectionURL="jdbc:oracle:thin:@127.0.0.1:1521:orcl"
                        userId="c##user"
                        password="system">
        </jdbcConnection>
        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer true，把JDBC DECIMAL 和
            NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="com.yy.pojo" targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- 生成DAO的包名和位置-->
        <!-- 客户端代码，生成易于使用的针对Model对象和XML配置文件 的代码
                type="ANNOTATEDMAPPER",生成Java Model 和基于注解的Mapper对象annotatedmapper
                type="MIXEDMAPPER",生成Java Model 和相应基于注解和xml混合使用的Mapper对象
                type="XMLMAPPER",生成SQLMap XML文件和独立的Mapper接口
        -->
        <javaClientGenerator type="ANNOTATEDMAPPER" targetPackage="com.yy.mapper"  targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
        <!-- 系统用户
        这里不指定schema，逆向工程会查询c##user都有哪些schema，对每个schema生成对象
         -->
        <table schema="" tableName="countries" />
        <table schema="" tableName="departments" />
        <table schema="" tableName="employees" />
        <table schema="" tableName="jobs" />
        <table schema="" tableName="job_history" />
        <table schema="" tableName="locations" />
        <table schema="" tableName="regions" />
        <table schema="" tableName="sal_grades" />
        <table schema="" tableName="users" />
    </context>
</generatorConfiguration>
```

![1568088493465](D:\Java\mybatisReverse\img\1568088493465.png)

### 操作介绍

![1568081777334](D:\Java\mybatisReverse\img\1568081777334.png)

