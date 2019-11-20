package com.yy;

import com.yy.mapper.UsersMapper;
import com.yy.pojo.Users;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan(value = "com.yy.mapper")
@EnableCaching
@SpringBootApplication
public class ClasssettingApplication {


    public static void main(String[] args) {
        SpringApplication.run(ClasssettingApplication.class, args);
    }

}
