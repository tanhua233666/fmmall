package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.entity.Users;
import com.tanhua.fmmall.service.UserService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(value = "提供用户的登录和注册接口",tags = "用户管理")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "string",name = "password",value = "用户登录密码",required = true)
    })
    @GetMapping("/login")
    public ResultVO login(@RequestParam("username") String name,@RequestParam(value = "password") String pwd){
        ResultVO resultVO = userService.checkLogin(name,pwd);

        return resultVO;
    }
   

    @ApiOperation("用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Object",name = "user",value = "用户注册对象",required = true)
    })
    @PostMapping("/regist")
    public ResultVO regist(@RequestBody Users user){
        return userService.userResgit(user.getUsername(),user.getPassword());
    }

    @ApiOperation("校验token是否过期")
    @GetMapping("/check")
    public ResultVO userTokenCheck(@RequestHeader("token") String token){
        return new ResultVO(ResultCode.SUCCESS,null);
    }

    @ApiOperation("获取用户信息接口")
    @ApiImplicitParam(dataType = "int",name = "userId",value = "用户id",required = true)
    @GetMapping("/userinfo")
    public ResultVO getUserInfo(int userId,@RequestHeader("token") String token){
        return userService.getUserInfo(userId);
    }

    @ApiOperation("用户信息设置接口")
    @ApiImplicitParam(dataType = "Object",name = "user",value = "用户信息对象",required = true)
    @PostMapping("/userinfo")
    public ResultVO setUserInfo(@RequestBody Users user,@RequestHeader("token") String token){
        System.out.println(user);
        return userService.setUserInfo(user);
    }
}
