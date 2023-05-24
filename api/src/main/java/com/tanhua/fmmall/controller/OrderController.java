package com.tanhua.fmmall.controller;

import com.github.wxpay.sdk.WXPay;
import com.tanhua.fmmall.service.OrderPayService;
import com.tanhua.fmmall.service.config.MyPayConfig;
import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.service.OrderService;
import com.tanhua.fmmall.utils.ApiDefinition;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
@Api(value = "订单相关接口",tags = "订单管理")
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private OrderPayService orderPayService;

    @PostMapping("/add")
    @ApiOperation("订单添加")
    public ResultVO add(String cids, @RequestBody Orders order){
        ResultVO resultVO ;
        try{
             resultVO = orderService.addOrder(cids, order);
            HashMap<String, String> map = (HashMap<String, String>) resultVO.getData();
            String orderId = map.get("orderId");
            String productNames = map.get("productNames");
            String money = String.valueOf(order.getTotalAmount().multiply(new BigDecimal("100")));
            int totalMoney = (int)Double.parseDouble(money);
            if(orderId != null){
                //设置当前订单信息
                HashMap<String,String> data = new HashMap<>();
                data.put("body",productNames); //商品描述
                data.put("out_trade_no",orderId); //使用用户订单编号作为支付交易号
                data.put("fee_type","CNY"); //支付币种
                data.put("total_fee",String.valueOf(totalMoney)); //支付金额
                data.put("trade_type","NATIVE"); //交易类型
                data.put("notify_url", ApiDefinition.WXPAYCALLBACK + "/pay/callback"); //通知返回地址

                 //微信支付：申请支付链接
                 WXPay wxPay = new WXPay(new MyPayConfig());

                 //发送请求，获取响应
                 Map<String, String> resp = wxPay.unifiedOrder(data);

                 map.put("payUrl",resp.get("code_url"));
                 orderPayService.setOrderPayUrl(orderId,resp.get("code_url"),Double.parseDouble(money)/100);
                 resultVO.setData(map);
             }else {
                return new ResultVO(100,"订单添加失败",null);
             }

        }catch (SQLException e){
            e.printStackTrace();
            resultVO = new ResultVO(100,"订单添加失败",null);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO = new ResultVO(100,"订单支付失败",null);
        }
        return resultVO;
    }

    @ApiOperation(value = "获取订单信息接口")
    @GetMapping("/info/{oid}")
    public ResultVO getOrderStatus(@PathVariable("oid") String orderId,@RequestHeader("token") String token){
        return orderService.getOrderById(orderId);
    }

    @ApiOperation(value = "删除订单接口")
    @GetMapping("/delete/{oid}")
    public ResultVO delOrder(@PathVariable("oid") String orderId,@RequestHeader("token") String token){
        int i = orderService.delOrder(orderId);
        return new ResultVO(ResultCode.SUCCESS,i);
    }

    @ApiOperation(value = "订单列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "userId",value = "用户ID",required = true),
            @ApiImplicitParam(dataType = "string",name = "status",value = "订单状态",required = false),
            @ApiImplicitParam(dataType = "int",name = "pageNum",value = "页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit",value = "页数",required = true),
    })
    @GetMapping("/list")
    public ResultVO listOrders(String userId,String status,int pageNum,int limit,@RequestHeader("token") String token){
        ResultVO resultVO = orderService.listOrders(userId, status, pageNum, limit);
        return resultVO;
    }

    @ApiOperation(value = "修改订单状态接口")
    @GetMapping("/status/{oid}")
    @ApiImplicitParam(dataType = "string",name = "status",value = "订单状态",required = true)
    public ResultVO updateOrderStatus(@PathVariable("oid") String orderId,String status,@RequestHeader("token") String token){
        int i = orderService.updateOrderStatus(orderId, status);
        return new ResultVO(ResultCode.SUCCESS,i);
    }

    @ApiOperation(value = "订单取消接口")
    @ApiImplicitParam(dataType = "int",name = "closeType",value = "取消原因 1-超时未支付 2-退款关闭 4-买家取消",required = true)
    @GetMapping("/close/{oid}")
    public void closeOrder(@PathVariable("oid") String orderId,int closeType,@RequestHeader("token") String token){
        orderService.closeOrder(orderId,closeType);
    }

    @ApiOperation(value = "获取订单支付链接接口")
    @GetMapping("/payurl/{oid}")
    public ResultVO closeOrder(@PathVariable("oid") String orderId,@RequestHeader("token") String token){
        ResultVO orderPayUrl = orderPayService.getOrderPayUrl(orderId);
        return orderPayUrl;
    }

}
