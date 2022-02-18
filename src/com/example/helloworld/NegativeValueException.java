package com.example.helloworld;

public class NegativeValueException extends Exception{
    public NegativeValueException(String errorMessage) {
        super(errorMessage);
    }
}
