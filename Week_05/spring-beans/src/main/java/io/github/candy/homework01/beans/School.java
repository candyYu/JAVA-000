package io.github.candy.homework01.beans;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Created by candy on 2020/11/19.
 */
@Data
public class School {

    private String name;

    private List<Student> students;

    public void play(){
        System.out.println("学校正常运行中");
    }
}
