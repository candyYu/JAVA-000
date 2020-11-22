package com.github.candy.beans;

import java.util.List;

/**
 * Created by candy on 2020/11/21.
 */
public class MySchool {

    private List<MyClass> myClasses;

    public MySchool(List<MyClass> myClasses) {
        this.myClasses = myClasses;
    }

    @Override
    public String toString() {
        return "MyClass::" + myClasses.toString();
    }
}
