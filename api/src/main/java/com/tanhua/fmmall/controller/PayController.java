package com.tanhua.fmmall.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.tanhua.fmmall.service.OrderPayService;
import com.tanhua.fmmall.service.OrderService;
import com.tanhua.fmmall.websocket.WebSocketServer;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private OrderService orderService;
    @Resource
    private OrderPayService orderPayService;

    /**
     *  回调接口：当用户支付成功后，微信支付平台会请求这个接口，将支付状态的数据传递过来
     * */
    @ApiOperation(value = "微信回调接口",notes = "仅供支付回调使用")
    @PostMapping("/callback")
    public String getPayMessage(HttpServletRequest request) throws Exception {
        //1.接收微信支付平台传递的数据(使用request的输入流接收
        ServletInputStream is = request.getInputStream();
        byte[] bs = new byte[1024];
        int len = -1;
        StringBuilder builder = new StringBuilder();
        while((len = is.read(bs) ) != -1){
            builder.append(new String(bs,0,len));
        }
        String s = builder.toString();

        //使用帮助类将xml接口的字符串转换成map
        Map<String, String> map = WXPayUtil.xmlToMap(s);
        if(map!=null &&  "success".equalsIgnoreCase(map.get("return_code"))){
            //2.修改订单状态为“待发货”
            String orderId = map.get("out_trade_no");
            int i = orderService.updateOrderStatus(orderId,"2");

            //3.向前端推送消息
            WebSocketServer.sendMsg(orderId,"1");

            //修改支付表状态
            //4.响应微信支付平台
            if(i > 0){
                HashMap<String,String> resp = new HashMap<>();
                resp.put("return_code","success");
                resp.put("return_msg","OK");
                resp.put("app_id",map.get("app_id"));
                resp.put("result_code","success");
                return WXPayUtil.mapToXml(resp);
            }

        }else {
            //支付失败
        }
        System.out.println(map);

        return null;
    }
}
