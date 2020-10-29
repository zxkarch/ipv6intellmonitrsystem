package saiernet.server.srnserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saiernet.server.srnserver.entity.ReqPack;
import saiernet.server.srnserver.entity.RespPack;
import saiernet.server.srnserver.entity.UserVO;
import saiernet.server.srnserver.entity.users;
import saiernet.server.srnserver.service.userservice;
//import utils.SysUtil;

@RestController
@RequestMapping(value = "/user")
public class initcontroller {
    @Autowired
    userservice userserviceobj;

    @PostMapping(value = "/login")
    public RespPack doLogin(@RequestBody ReqPack reqPack) {
        String username;
        String password;
        System.err.println(reqPack.getData());
        try {
            username = reqPack.getData().getString("username");
            password = reqPack.getData().getString("password");
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "参数异常");
        }

        if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
            ///throw UnifiedException.create(1, "用户名密码不可为空", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "用户名密码不可为空");
        }

        //业务
        users user = userserviceobj.getUserbyToken(username, password);
        if (user == null) {
            ///throw UnifiedException.create(2, "用户名或密码错误", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(2, "用户名或密码错误");
        }

        UserVO uservo=new UserVO(user.getUsername(),"");
        return RespPack.respPackSuc(uservo);
    }

    @PostMapping(value = "/register")
    public RespPack doRegister(@RequestBody ReqPack reqPack) {
        String username;
        String password;
        System.err.println(reqPack.getData());
        try {
            username = reqPack.getData().getString("username");
            password = reqPack.getData().getString("password");
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "参数异常");
        }

        if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
            ///throw UnifiedException.create(1, "用户名密码不可为空", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "用户名密码不可为空");
        }

        //业务
        users user = userserviceobj.getUserbyName(username);
        if (user != null) {
            ///throw UnifiedException.create(2, "用户名或密码错误", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(2, "用户名已注册");
        }

        int rid = userserviceobj.addUser(username,password);
        if (rid<=0){
            return RespPack.respPackFail(3, "未知用户添加异常");
        }

        return RespPack.respPackSuc(1);
    }
}