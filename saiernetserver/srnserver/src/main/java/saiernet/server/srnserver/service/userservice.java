package saiernet.server.srnserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saiernet.server.srnserver.dao.userdao;
import saiernet.server.srnserver.entity.users;

@Service
public class userservice {
    @Autowired
    userdao userdaoobj;

    public users getUserbyToken(String username, String password){
        if (username==null||username.length()<=0||password==null||password.length()<=0)
            return null;
        return userdaoobj.getUserbyToken(username, password);
    }

    public users getUserbyName(String username){
        if (username==null||username.length()<=0)
            return null;
        return userdaoobj.getUserbyName(username);
    }

	public int addUser(String username, String password) {
        if (username==null||username.length()<=0||password==null||password.length()<=0)
            return -1;
		return userdaoobj.addUser(username, password);
	}
}