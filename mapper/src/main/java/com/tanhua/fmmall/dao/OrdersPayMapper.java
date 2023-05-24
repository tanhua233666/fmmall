package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.entity.OrdersPay;
import com.tanhua.fmmall.general.GeneralDao;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersPayMapper extends GeneralDao<Orders> {
    public int addOrderPay(OrdersPay ordersPay);

    public OrdersPay getOrderPayUrl(String orderId);
}