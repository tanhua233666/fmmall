package com.tanhua.fmmall.service;

import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.vo.ResultVO;

import java.sql.SQLException;

public interface OrderService {
    public ResultVO addOrder(String cids, Orders order) throws SQLException;

    public int delOrder(String orderId);

    public int updateOrderStatus(String orderId,String status);

    public ResultVO getOrderById(String orderId);

    public void closeOrder(String orderId,int lose_type);

    public ResultVO listOrders(String userId,String status,int pageNum,int limit);

    public void submitOrderItemIsComment(String itemId);
}
