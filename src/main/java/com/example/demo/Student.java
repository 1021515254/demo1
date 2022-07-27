package com.example.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author <a href="mailto:lucky_zhang_yu@163.com">lucky</a>
 * @since 1.0.0
 */
@Data
public class Student implements Serializable {

    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
