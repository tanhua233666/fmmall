package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.UserAddrMapper;
import com.tanhua.fmmall.entity.UserAddr;
import com.tanhua.fmmall.service.UserAddrService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserAddrServiceImpl implements UserAddrService {
    @Resource
    private UserAddrMapper userAddrMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVO listAddrsByUid(int userId) {
        Example example = new Example(UserAddr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("status",1);
        List<UserAddr> userAddrs = userAddrMapper.selectByExample(example);
        return new ResultVO(ResultCode.SUCCESS,userAddrs);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean addAddr(UserAddr userAddr) {
        Integer date = Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10));
        userAddr.setCreateTime(new Date());
        userAddr.setUpdateTime(new Date());
        userAddr.setAddrId(userAddr.getUserId() + date + new Random().nextInt(999));

        int i = userAddrMapper.insertSelective(userAddr);
        if(i > 0){
            if(1 == userAddr.getCommonAddr()){
                this.setCommonAddrByUid(Integer.parseInt(userAddr.getUserId()),userAddr.getAddrId());
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean updateAddr(UserAddr userAddr) {
        userAddr.setUpdateTime(new Date());
        int i = userAddrMapper.updateByPrimaryKeySelective(userAddr);
        if(i > 0){
            if(1 == userAddr.getCommonAddr()){
                this.setCommonAddrByUid(Integer.parseInt(userAddr.getUserId()),userAddr.getAddrId());
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean delAddr(String addrId) {
        String[] str = addrId.split("addrId=");
        UserAddr userAddr = new UserAddr();
        userAddr.setUpdateTime(new Date());
        if(str.length > 1){
            userAddr.setAddrId(str[1]);
        }else {
            userAddr.setAddrId(addrId);
        }
        userAddr.setStatus(0);
        int i = userAddrMapper.updateByPrimaryKeySelective(userAddr);
        if(i > 0){
            return true;
        }else {
            return false;
        }

    }

    //根据用户id循环设置默认地址
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean setCommonAddrByUid(int userId,String addrId) {
        Example example = new Example(UserAddr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        List<UserAddr> userAddrs = userAddrMapper.selectByExample(example);

        for(UserAddr userAddr : userAddrs){
            if(addrId.equals(userAddr.getAddrId())){
                userAddr.setCommonAddr(1);
            }else {
                userAddr.setCommonAddr(0);
            }
            userAddrMapper.updateByPrimaryKeySelective(userAddr);
        }
        return true;
    }
}
