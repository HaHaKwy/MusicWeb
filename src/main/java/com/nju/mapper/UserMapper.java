package com.nju.mapper;

import com.nju.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //通过用户名和密码查找
    User selectByName(@Param("username") String username);

    //插入用户
    void addUser(@Param("username") String username,@Param("password") String password,@Param("email") String email);

}
