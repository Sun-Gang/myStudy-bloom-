package com.bloomfinance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.bloomfinance.**.repository")
public class BloomFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloomFinanceApplication.class, args);
    }
}