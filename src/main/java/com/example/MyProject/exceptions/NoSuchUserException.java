package com.example.MyProject.exceptions;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(String msg) {
        super(msg);
    }
}
