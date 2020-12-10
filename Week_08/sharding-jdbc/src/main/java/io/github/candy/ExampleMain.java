package io.github.candy;

import io.github.candy.service.OrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

/**
 * Created by candy on 2020/12/10.
 */

@MapperScan(basePackages = "io.github.candy.mapper")
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class ExampleMain  implements CommandLineRunner {

    @Autowired
    private OrderService orderService;

    public static void main(final String[] args)  {
        SpringApplication.run(ExampleMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try{
            orderService.initEnvironment();
            orderService.processSuccess();
        }finally {
            orderService.cleanEnvironment();
        }
    }
}
