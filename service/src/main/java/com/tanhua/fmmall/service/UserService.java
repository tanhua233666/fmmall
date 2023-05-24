package com.tanhua.fmmall.service;

import com.tanhua.fmmall.entity.Users;
import com.tanhua.fmmall.vo.ResultVO;

public interface UserService {

    //用户注册
    public ResultVO userResgit(String name, String pwd);
    //用户登录
    public ResultVO checkLogin(String name, String pwd);

    public ResultVO getUserInfo(int userId);

    public ResultVO setUserInfo(Users user);
}
