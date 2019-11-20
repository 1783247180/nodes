package com.yy.sb_redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.yy.sb_redis.mapper")
@SpringBootApplication
@EnableCaching
public class SbRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbRedisApplication.class, args);
    }

}
