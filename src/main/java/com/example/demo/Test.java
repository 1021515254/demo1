package com.example.demo;

import java.io.*;
import java.util.Properties;

/**
 * TODO
 *
 * @author <a href="mailto:lucky_zhang_yu@163.com">lucky</a>
 * @since 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src\\main\\resources\\Baby.properties"));
            System.out.println(properties.getProperty("testTxt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
