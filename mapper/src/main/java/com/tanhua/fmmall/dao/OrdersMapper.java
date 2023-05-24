package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.general.GeneralDao;
import com.tanhua.fmmall.vo.OrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper extends GeneralDao<Orders> {

    public List<OrdersVO> selectOrders(@Param("userId") String userId,
                                       @Param("status") String status,
                                       @Param("start") int start,
                                       @Param("limit") int limit);
}