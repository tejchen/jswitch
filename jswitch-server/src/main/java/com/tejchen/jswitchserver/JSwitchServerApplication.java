package com.tejchen.jswitchserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.tejchen.jswitchserver.mapper")
@EnableAspectJAutoProxy
public class JSwitchServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JSwitchServerApplication.class, args);
	}

}
