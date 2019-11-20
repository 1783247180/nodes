一般情况下使用jest操作ES

ES的基本原理

![1560949852035](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560949852035.png)

存在索引(对应数据库) 类型(对应表名) 文档(表中的记录)属性(列)

以 员工文档 的形式存储为例：一个文档代表一个员工数据。存储数据到 ElasticSearch 的行为叫做 索引 ，但在索引一个文档之前，需要确定将文档存储在哪里。
一个 ElasticSearch 集群可以 包含多个 索引 ，相应的每个索引可以包含多个 类型 。 这些不同的类型存储着多个 文档 ，每个文档又有 多个 属性 。把数据放进ES的索引中就可以在代码的查询语句查询到了

访问https://www.elastic.co/guide/cn/elasticsearch/guide/current/_phrase_search.html看查询表达式

尚硅谷视频12.21

## 基本步骤

1. 虚拟机中启动ES(如果未安装看SpringBoot.md的docker模块)

   ![1560949409114](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560949409114.png)

   可以看出来ES的容器ID为4d1a60fb7c34

   ![1560949444335](C:\Users\www17\AppData\Roaming\Typora\typora-user-images\1560949444335.png)

   

2. 添加依赖

```xml
<dependency>
            <groupId>io.searchbox</groupId>
            <artifactId>jest</artifactId>
            <version>5.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

3. application.properties中添加

   ```properties
   spring.elasticsearch.jest.uris=http://192.168.40.133:9200
   ```

4. Article类中

   ```java
   package com.atguigu.elastic.bean;
   import io.searchbox.annotations.JestId;
   public class Article {
       @JestId
       private Integer id;
       private String author;
       private String title;
       private String content;
       public Integer getId() {
           return id;
       }
       public void setId(Integer id) {
           this.id = id;
       }
       public String getAuthor() {
           return author;
       }
       public void setAuthor(String author) {
           this.author = author;
       }
       public String getTitle() {
           return title;
       }
       public void setTitle(String title) {
           this.title = title;
       }
       public String getContent() {
           return content;
       }
       public void setContent(String content) {
           this.content = content;
       }
   }
   ```

   

5. test

   ```java
   package com.atguigu.elastic;
   import com.atguigu.elastic.bean.Article;
   import io.searchbox.client.JestClient;
   import io.searchbox.core.Index;
   import io.searchbox.core.Search;
   import io.searchbox.core.SearchResult;
   import org.junit.Test;
   import org.junit.runner.RunWith;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   import org.springframework.test.context.junit4.SpringRunner;
   import java.io.IOException;
   @RunWith(SpringRunner.class)
   @SpringBootTest
   public class Springboot03ElasticApplicationTests {
       @Autowired
   JestClient jestClient;
       @Test
       public void contextLoads() {
           //给ES中保存一个文档
           Article article = new Article();
           article.setId(1);
           article.setTitle("hxx");
           article.setAuthor("zs");
           article.setContent("hello");
           //构建一个索引(atguigu),类型(news)
           Index build = new Index.Builder(article).
                   index("atguigu").type("news").build();
           try {
               //执行
               jestClient.execute(build);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   @Test
       public void search(){
           //搜索,下面的content是article里面有content字段的值是hello所以会把上面加进去的记录查到
           //访问http://192.168.40.133:9200/atguigu/news/1就可以看到
           //查询语句的语法查看https://www.elastic.co/guide/cn/elasticsearch/guide/current/_phrase_search.html
           String json="{\n" +
                   "    \"query\" : {\n" +
                   "        \"match_phrase\" : {\n" +
                   "            \"content\" : \"hello\"\n" +
                   "        }\n" +
                   "    }\n" +
                   "}";
           //Builder里面就是查询表达式,索引(atguigu),类型(news)
       Search search = new Search.Builder(json).addIndex("atguigu").addType("news").build();
       try {
           SearchResult execute = jestClient.execute(search);
           System.out.println("----"+execute.getJsonString());
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   }
   
   ```

   