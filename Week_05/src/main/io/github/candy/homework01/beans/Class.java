package io.github.candy.homework01.beans;

import org.springframework.stereotype.Component;

/**
 * Created by candy on 2020/11/19.
 */
@Component("myClass")
public class Class {

    private String name;

    public void displayHomeWork(){
        System.out.println("上课中");
    }
}
