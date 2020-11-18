package io.github.candy.homework01;

import io.github.candy.homework01.beans.Class;
import io.github.candy.homework01.beans.School;
import io.github.candy.homework01.beans.Student;
import io.github.candy.homework01.config.ComponentConfig;
import io.github.candy.homework01.config.JavaConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by candy on 2020/11/18.
 */
public class HomeWork01 {



    public static void main(String[] args) {

        AnnotationConfigApplicationContext componentApplicationContext = new AnnotationConfigApplicationContext(ComponentConfig.class);
        Class myClass = (Class) componentApplicationContext.getBean("myClass");
        myClass.displayHomeWork();

        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);
        School compactDisc = (School)configApplicationContext.getBean("mySchool");
        compactDisc.play();

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Student student123 = (Student) context.getBean("student123");
        System.out.println(student123.toString());

        Student student100 = (Student) context.getBean("student100");
        System.out.println(student100.toString());

        School school = context.getBean(School.class);
        System.out.println(school);

    }
}
