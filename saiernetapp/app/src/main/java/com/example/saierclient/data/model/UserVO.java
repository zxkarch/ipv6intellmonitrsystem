package com.example.saierclient.data.model;

public class UserVO {
    private String username;
    private String password;

    public UserVO(String un, String pwd){
        this.username=un;
        this.password=pwd;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}