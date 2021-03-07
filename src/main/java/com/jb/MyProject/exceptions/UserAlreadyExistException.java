package com.example.MyProject.exceptions;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
