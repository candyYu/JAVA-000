package io.github.candy.homework01.config;

import io.github.candy.homework01.beans.School;
import io.github.candy.homework01.beans.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by candy on 2020/11/19.
 */
@Configuration
public class JavaConfig {

    @Bean
    public School mySchool() {
        return  new School();
    }

    @Bean
    public Student myStudent() {
        return  new Student();
    }
}
