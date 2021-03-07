package com.jb.MyProject.exceptions;

public class UserIsNotExistException extends Exception {
    public UserIsNotExistException(String msg){
        super(msg);
    }
}
