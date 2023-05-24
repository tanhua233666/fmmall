package com.tanhua.fmmall.service;

import com.tanhua.fmmall.vo.ResultVO;

public interface OrderPayService {
    public ResultVO setOrderPayUrl(String orderId,String payUrl,Double total);

    public ResultVO getOrderPayUrl(String orderId);
}
