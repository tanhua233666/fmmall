package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.OrdersPayMapper;
import com.tanhua.fmmall.entity.OrdersPay;
import com.tanhua.fmmall.service.OrderPayService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@Service
public class OrderPayServiceImpl implements OrderPayService {
    @Resource
    private OrdersPayMapper ordersPayMapper;

    @Override
    public ResultVO setOrderPayUrl(String orderId, String payUrl,Double total) {
        String orderPayId = String.valueOf(new Random().nextInt(79999999) + new Random().nextInt(20000001));

        OrdersPay ordersPay = new OrdersPay(orderPayId, orderId, payUrl,total,"0");
        int i = ordersPayMapper.addOrderPay(ordersPay);
        System.out.println(ordersPay);
        return new ResultVO(ResultCode.SUCCESS,ordersPay);
    }

    @Override
    public ResultVO getOrderPayUrl(String orderId) {
        OrdersPay orderPay = ordersPayMapper.getOrderPayUrl(orderId);
        return new ResultVO(ResultCode.SUCCESS,orderPay);
    }
}
