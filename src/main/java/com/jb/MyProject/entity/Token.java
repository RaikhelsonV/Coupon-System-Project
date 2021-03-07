package com.jb.MyProject.entity;

import java.util.UUID;

public class Token {
    private static final int LENGTH_TOKEN = 15;
    private String token = "";

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, LENGTH_TOKEN);
    }

}
