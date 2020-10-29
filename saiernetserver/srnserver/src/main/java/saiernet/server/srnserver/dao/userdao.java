package saiernet.server.srnserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import saiernet.server.srnserver.entity.users;

@Repository
public interface userdao {
    List<users> selectUsersQuery();
    users getUserbyToken(@Param("username") String username, @Param("password") String password);
    users getUserbyName(@Param("username") String username);
	int addUser(@Param("username") String username, @Param("password") String password);

}