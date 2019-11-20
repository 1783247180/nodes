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
        //构建一个索引功能,类型
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
}
