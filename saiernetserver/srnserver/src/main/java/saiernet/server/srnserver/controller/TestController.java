package saiernet.server.srnserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saiernet.server.srnserver.dao.userdao;
import saiernet.server.srnserver.entity.users;

@RestController
public class TestController {
    @Autowired
    private userdao userdaoobj;

    @RequestMapping("/")
    public List<users> HomePage(){
        System.err.println(userdaoobj.selectUsersQuery());
        return userdaoobj.selectUsersQuery();
    }
}