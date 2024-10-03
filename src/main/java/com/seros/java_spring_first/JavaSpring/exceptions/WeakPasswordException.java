package com.seros.java_spring_first.JavaSpring.exceptions;


public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException(String message) {
        super(message);
    }
}
