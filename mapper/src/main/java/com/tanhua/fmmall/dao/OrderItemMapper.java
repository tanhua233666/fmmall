package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.OrderItem;
import com.tanhua.fmmall.general.GeneralDao;

import java.util.List;

public interface OrderItemMapper extends GeneralDao<OrderItem> {
    public List<OrderItem> listOrderItemsByOrderId(String orderId);
}