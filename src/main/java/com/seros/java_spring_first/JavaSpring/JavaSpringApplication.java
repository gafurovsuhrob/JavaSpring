package com.seros.java_spring_first.JavaSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class JavaSpringApplication {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        System.out.println(encodedPassword);
        SpringApplication.run(JavaSpringApplication.class, args);
    }
}
