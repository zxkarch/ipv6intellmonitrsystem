package com.example.saierclient.data;

import com.example.saierclient.data.model.LoggedInUser;

/**
 * 该类：1.从远程数据源请求身份验证和用户信息；
 * 2.维持内存缓存中登陆状态和用户凭据信息.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // 若用户凭据信息在本地存储，建议加密处理
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // 私有构造函数：单例访问
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // 若用户凭据信息在本地存储，建议加密处理
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password, String urlroot) {
        // 处理登陆数据
        Result<LoggedInUser> result = dataSource.login(username, password, urlroot);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public Result<LoggedInUser> register(String username, String password, String urlroot) {
        // 处理登陆数据
        Result<LoggedInUser> result = dataSource.register(username, password, urlroot);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}