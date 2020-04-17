package cn.mike.stockservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.mike.stockservice.mapper")
public class StockserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockserviceApplication.class, args);
    }

}
