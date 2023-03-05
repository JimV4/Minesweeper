package com.example.minedemo;

public class InvalidDescriptionException extends Exception{
    public InvalidDescriptionException (String errorMessage) {
        super(errorMessage);
    }
}
