package io.github.candy;

import com.github.candy.beans.MySchool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by candy on 2020/11/22.
 */
@SpringBootApplication
public class MyStartApplication implements CommandLineRunner {

    @Autowired
    private MySchool mySchool;

    public static void main(String[] args) {
        SpringApplication.run(MyStartApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(mySchool);
    }
}
