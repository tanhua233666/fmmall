package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.OrderItemMapper;
import com.tanhua.fmmall.dao.OrdersMapper;
import com.tanhua.fmmall.dao.ProductSkuMapper;
import com.tanhua.fmmall.dao.ShoppingCartMapper;
import com.tanhua.fmmall.entity.OrderItem;
import com.tanhua.fmmall.entity.Orders;
import com.tanhua.fmmall.entity.ProductSku;
import com.tanhua.fmmall.service.OrderService;
import com.tanhua.fmmall.utils.PageHelper;
import com.tanhua.fmmall.vo.OrdersVO;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import com.tanhua.fmmall.vo.ShoppingCartVO;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private ProductSkuMapper productSkuMapper;

    @Transactional
    public ResultVO addOrder(String cids, Orders order) throws SQLException {
        //1.校验库存：根据cids查询当前订单中关联的购物车记录（包括库存）
        String[] arr = cids.split(",");
        List<Integer> cidsList = new ArrayList<>();
        for(int i=0;i< arr.length;i++){
            cidsList.add(Integer.parseInt(arr[i]));
        }
        List<ShoppingCartVO> shoppingCartVOS = shoppingCartMapper.selectShoppingCartsByCids(cidsList);

        boolean f = true;
        String untitled = "";
        for(int i =0;i < shoppingCartVOS.size() ;i++){
            if(Integer.parseInt(shoppingCartVOS.get(i).getCartNum()) > shoppingCartVOS.get(i).getSkuStock()){
                f = false;
            }
            //获取所有商品名称，以,分隔拼接字符串
            if(i < shoppingCartVOS.size() -1){
                untitled = untitled + shoppingCartVOS.get(i).getProductName() + ",";
            }else {
                untitled = untitled + shoppingCartVOS.get(i).getProductName() ;
            }
        }
        if(f){
            // 2.保存订单
            order.setUntitled(untitled);
            order.setCreateTime(new Date());
            order.setStatus("1");

            //生成订单编号
            String orderId = UUID.randomUUID().toString().replace("-", "");
            order.setOrderId(orderId);

            //调用接口保存订单
            int i = ordersMapper.insert(order);


            if(i>0){
                //3.生成商品快照
                for(ShoppingCartVO sc : shoppingCartVOS){
                    String itemId = System.currentTimeMillis() + "" + (new Random().nextInt(89999)+10001);
                    int cnum = Integer.parseInt(sc.getCartNum());
                    OrderItem orderItem = new OrderItem(itemId, orderId, sc.getProductId(), sc.getProductName(), sc.getProductImg(), sc.getSkuId(), sc.getSkuName(),
                            new BigDecimal(sc.getSellPrice()), cnum, new BigDecimal(sc.getSellPrice() * cnum), new Date(), new Date(), 0);
                    orderItemMapper.insert(orderItem);
                }

                //4.扣减库存
                for(ShoppingCartVO sc : shoppingCartVOS){
                    String skuId = sc.getSkuId();
                    int newStock = sc.getSkuStock() - Integer.parseInt(sc.getCartNum());

                    ProductSku productSku = new ProductSku();
                    productSku.setStock(newStock);
                    productSku.setSkuId(skuId);
                    productSkuMapper.updateByPrimaryKeySelective(productSku);
                }

                //5.删除购物车
                for (int cid:cidsList){
                    shoppingCartMapper.deleteByPrimaryKey(cid);
                }
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",orderId);
            map.put("productNames",untitled);
            return new ResultVO(101,"下单成功",map);
        }else {
            //库存不足
            return new ResultVO(100,"库存不足，下单失败",null);
        }

    }

    @Override
    public int delOrder(String orderId) {
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setDeleteStatus(1);
        int i = ordersMapper.updateByPrimaryKeySelective(order);
        return i;
    }

    @Override
    public int updateOrderStatus(String orderId, String status) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setStatus(status);
        int i = ordersMapper.updateByPrimaryKeySelective(orders);

        return i;
    }

    @Override
    public ResultVO getOrderById(String orderId) {
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        return new ResultVO(ResultCode.SUCCESS,orders.getStatus());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void closeOrder(String orderId,int lose_type) {
        synchronized (this) {
            //b.修改当前订单：status=6 已关闭 close_type=1 超时未支付
            Orders cancleOrder = new Orders();
            cancleOrder.setOrderId(orderId);
            cancleOrder.setStatus("6");
            cancleOrder.setCloseType(lose_type);
            ordersMapper.updateByPrimaryKeySelective(cancleOrder);

            //c.还原库存：现根据当前订单编号查询商品快照(sku_id buy_count) -- 修改product_sku
            Example orderItemExample = new Example(OrderItem.class);
            Example.Criteria orderItemExampleCriteria = orderItemExample.createCriteria();
            orderItemExampleCriteria.andEqualTo("orderId", orderId);
            List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem orderItem = orderItems.get(i);
                //修改库存
                ProductSku productSku = productSkuMapper.selectByPrimaryKey(orderItem.getSkuId());
                productSku.setStock(productSku.getStock() + orderItem.getBuyCounts());
                productSkuMapper.updateByPrimaryKey(productSku);
            }
        }
    }

    @Override
    public ResultVO listOrders(String userId, String status, int pageNum, int limit) {
        //1.分页查询
        int start = (pageNum-1)*limit;
        List<OrdersVO> ordersVOS = ordersMapper.selectOrders(userId, status, start, limit);

        //2.查询总记录数
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        if (status != null && !"".equals(status)){
            criteria.andEqualTo("status",status);
        }
        Example example1 = new Example(Orders.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.orEqualTo("deleteStatus",0);
        criteria1.orIsNull("deleteStatus");

        example.and(criteria1);
        int count = ordersMapper.selectCountByExample(example);

        //3.计算总页数
        int pageCount = count%limit == 0 ? count/limit : count/limit+1;

        //4.封装数据返回
        PageHelper<OrdersVO> pageHelper = new PageHelper<>(count, pageCount, ordersVOS);
        return new ResultVO(ResultCode.SUCCESS,pageHelper);
    }

    @Override
    public void submitOrderItemIsComment(String itemId){
        OrderItem orderItem = new OrderItem(itemId, 1);
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }
}
