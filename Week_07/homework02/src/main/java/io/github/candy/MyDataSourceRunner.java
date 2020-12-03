package io.github.candy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by candy on 2020/12/3.
 */
@Component
@Slf4j
public class MyDataSourceRunner  implements CommandLineRunner {

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        orderService.readFromSlave();
    }



}
