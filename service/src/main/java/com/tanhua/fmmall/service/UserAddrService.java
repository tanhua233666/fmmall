package com.tanhua.fmmall.service;

import com.tanhua.fmmall.entity.UserAddr;
import com.tanhua.fmmall.vo.ResultVO;

public interface UserAddrService {
    public ResultVO listAddrsByUid(int userId);

    public boolean updateAddr(UserAddr userAddr);

    public boolean setCommonAddrByUid(int userId,String addrId);

    public boolean addAddr(UserAddr userAddr);

    public boolean delAddr(String addrId);
}
