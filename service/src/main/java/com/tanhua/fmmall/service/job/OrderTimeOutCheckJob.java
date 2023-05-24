package com.tanhua.fmmall.service.job;

import com.github.wxpay.sdk.WXPay;
import com.tanhua.fmmall.dao.OrderItemMapper;
import com.tanhua.fmmall.dao.OrdersMapper;
import com.tanhua.fmmall.dao.ProductSkuMapper;
import com.tanhua.fmmall.entity.OrderItem;
import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.entity.ProductSku;
import com.tanhua.fmmall.service.OrderService;
import com.tanhua.fmmall.service.config.MyPayConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderTimeOutCheckJob {
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderService orderService;

    private WXPay wxPay = new WXPay(new MyPayConfig());

    @Scheduled(cron = "0/30 * * * * ?")
    public void checkAndClose(){
        System.out.println("...............");
        //1.查询超过30分钟未支付的订单
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1);

        Date time = new Date(System.currentTimeMillis() - 30*60*1000);
        criteria.andLessThan("createTime",time);

        List<Orders> ordersList = ordersMapper.selectByExample(example);

        //2.访问微信平台接口确定当前订单最终支付状态

        try{
            for(int i=0;i < ordersList.size();i++){
                Orders order = ordersList.get(i);
                HashMap<String,String> map = new HashMap<>();
                map.put("out_trade_no",order.getOrderId());
                //向微信查询订单信息
                Map<String, String> resp = wxPay.orderQuery(map);

                if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){
                    //如果订单已经被支付 status=2
                    Orders updateOrder = new Orders();
                    updateOrder.setOrderId(order.getOrderId());
                    updateOrder.setStatus("2");
                    ordersMapper.updateByPrimaryKeySelective(updateOrder);
                }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                    //3.取消订单
                    //a.向微信平台发起关闭支付链接请求
                    Map<String, String> closeOrderMsgMap = wxPay.closeOrder(map);
                    System.out.println(closeOrderMsgMap);

                   //b.关闭订单
                    orderService.closeOrder(order.getOrderId(),1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
