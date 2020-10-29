package com.example.saierclient.data;

import com.alibaba.fastjson.JSONObject;
import com.example.saierclient.data.model.LoggedInUser;
import com.example.saierclient.data.model.ResponseVO;
import com.example.saierclient.data.model.UserVO;
import com.example.saierclient.utils.MyHttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 用于处理用户登陆 w/ 登陆凭据并检索信息.
 */
public class LoginDataSource {
    private static Logger logger = LoggerFactory.getLogger(MyHttpClientUtils.class);

    public Result<LoggedInUser> login(String username, String password, String urlroot) {

        try {
            // TODO: handle loggedInUser authentication
            //String baseurl = this.getString(R.string.mess_1);
            //String url = "http://192.168.0.100:8080/user/login";
            String url = urlroot+"/user/login";
            UserVO uservo = new UserVO(username,password);
            JSONObject json = new JSONObject();
            json.put("version","2.0");
            json.put("data",uservo);
//            json.put("username",username);
//            json.put("password",password);
//       String post = "{\"phone\":\"15680659530\",\"password\":\"123456\"}";
            String post = json.toString();
            logger.debug(post);
            String respcontent = MyHttpClientUtils.createHttpsPostByjson(url,post,"application/json");

            ResponseVO<JSONObject> responvo = JSONObject.parseObject(respcontent, ResponseVO.class);
            if (responvo.getCode()==0) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(fakeUser);
            }else{
                return new Result.Failure(responvo.getDesc());
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("意外发生：", e));
        }
    }

    public Result<LoggedInUser> register(String username, String password, String urlroot) {

        try {
            // TODO: handle loggedInUser authentication
            //String baseurl = this.getString(R.string.mess_1);
            //String url = "http://192.168.0.100:8080/user/register";
            String url = urlroot+"/user/register";
            UserVO uservo = new UserVO(username,password);
            JSONObject json = new JSONObject();
            json.put("version","2.0");
            json.put("data",uservo);
//            json.put("username",username);
//            json.put("password",password);
//       String post = "{\"phone\":\"15680659530\",\"password\":\"123456\"}";
            String post = json.toString();
            logger.debug(post);
            String respcontent = MyHttpClientUtils.createHttpsPostByjson(url,post,"application/json");
            ResponseVO<JSONObject> responvo= JSONObject.parseObject(respcontent, ResponseVO.class);
            if (responvo.getCode()==0) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(fakeUser);
            }else{
                return new Result.Failure(responvo.getDesc());
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("意外发生：", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}