package com.junjie.blamepicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.junjie.blamepicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class BlamePictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlamePictureBackendApplication.class, args);
    }

}
