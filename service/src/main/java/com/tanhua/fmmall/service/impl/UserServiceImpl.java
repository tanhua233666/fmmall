package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.UsersMapper;
import com.tanhua.fmmall.entity.Users;
import com.tanhua.fmmall.service.UserService;
import com.tanhua.fmmall.utils.Base64Utils;
import com.tanhua.fmmall.utils.MD5Utils;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;

    @Transactional
    public ResultVO userResgit(String name, String pwd) {
        synchronized (this){
            //1.根据用户名查询是否被注册
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username",name);
            List<Users> users = usersMapper.selectByExample(example);

            //2.如果没有被注册则进行保存
            if(users.size() == 0){
                //密码加密
                String md5Pwd = MD5Utils.md5(pwd);
                Users user = new Users();
                user.setUsername(name);
                user.setPassword(md5Pwd);
                user.setUserRegtime(new Date());
                user.setUserModtime(new Date());
                user.setUserImg("img/default.png");
                int i = usersMapper.insertUseGeneratedKeys(user);
                if(1>0){
                    return new ResultVO(ResultCode.SUCCESS,user);
                }else{
                    return new ResultVO(ResultCode.FAILURE,null);
                }
            }else{
                return new ResultVO(ResultCode.USER_HAS_EXIST,null);
            }
        }
    }

    @Override
    public ResultVO checkLogin(String name, String pwd) {
        //1.根据账户查询用户信息
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",name);
        List<Users> users = usersMapper.selectByExample(example);

        //2.判断
        if(users.size() == 0){
            //用户名不存在
            return new ResultVO(ResultCode.USER_NOT_EXIST,null);
        }else{
            //3.对输入的密码进行加密
            String md5Pwd = MD5Utils.md5(pwd);
            //使用加密后的密码和user的密码进行匹配
            if(md5Pwd.equals(users.get(0).getPassword())){
                //验证成功
                JwtBuilder builder = Jwts.builder();
                HashMap<String,Object> map = new HashMap<>();


                String token = builder.setSubject(name) //token携带的data
                        .setIssuedAt(new Date()) //设置token的生成时间
                        .setId(users.get(0).getUserId() + "") //设置用户id为用户id
                        .setClaims(map) //map中可以存放用户的角色权限信息
                        .setExpiration(new Date(System.currentTimeMillis() + 24 * 3600000))  //设置token过期时间
                        .signWith(SignatureAlgorithm.HS256, "y1000z8g") //设置加密方式和密码
                        .compact();

                System.out.println("1");
                return new ResultVO(ResultCode.SUCCESS,token,users.get(0));
            }else{
                //密码错误
                return new ResultVO(ResultCode.USER_LOGIN_FAIL,null);
            }
        }

    }

    @Override
    public ResultVO getUserInfo(int userId){
        Users users = usersMapper.selectByPrimaryKey(userId);
        return new ResultVO(ResultCode.SUCCESS,users);
    }

    @Override
    public ResultVO setUserInfo(Users user){
        int i = usersMapper.updateByPrimaryKey(user);
        return new ResultVO(ResultCode.SUCCESS,i);
    }
}
