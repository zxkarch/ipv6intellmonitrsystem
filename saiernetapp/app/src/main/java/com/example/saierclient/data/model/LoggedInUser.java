package com.example.saierclient.data.model;

/**
 * 数据声明类，捕获从LoginRepository中检索已登录用户的登陆信息
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}