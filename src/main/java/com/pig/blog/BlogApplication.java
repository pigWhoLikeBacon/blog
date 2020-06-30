package com.pig.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class BlogApplication {

    public final static int imageSize = 9;

    public final static int articleSize = 6;

    public final static int commentSize = 10;

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
