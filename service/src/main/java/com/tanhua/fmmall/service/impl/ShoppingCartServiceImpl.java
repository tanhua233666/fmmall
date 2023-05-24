package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.ShoppingCartMapper;
import com.tanhua.fmmall.entity.ShoppingCart;
import com.tanhua.fmmall.service.ShoppingCartService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import com.tanhua.fmmall.vo.ShoppingCartVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public ResultVO addShoppingCart(ShoppingCart cart) {
        cart.setCartTime(sdf.format(new Date()));
        int i = shoppingCartMapper.insert(cart);
        if(i > 0){
            return new ResultVO(ResultCode.SUCCESS,"success",null);
        }
        return new ResultVO(ResultCode.FAILURE,"failure",null);
    }

    @Override
    public ResultVO delShoppingCart(int[] cartIds) {
        int i = 0;
        for(int c : cartIds){
            i = shoppingCartMapper.deleteByPrimaryKey(c);
        }
        if(i > 0){
            return new ResultVO(ResultCode.SUCCESS,"success",null);
        }
        return new ResultVO(ResultCode.FAILURE,"failure",null);
    }

    @Override
    public ResultVO showShoppingCartList(int userId) {
        List<ShoppingCartVO> shoppingCartVOS = shoppingCartMapper.selectShoppingCartByUserId(userId);
        return new ResultVO(ResultCode.SUCCESS,shoppingCartVOS);
    }

    @Override
    public ResultVO updateCartNumByCartId(int cartId,int cartNum) {
        int i = shoppingCartMapper.updateCartNumByCartId(cartId,cartNum);
        if(i >0){
            return new ResultVO(ResultCode.SUCCESS,null);
        }
        return new ResultVO(ResultCode.FAILURE,null);
    }

    @Override
    public ResultVO listShoppingCartsByCids(String cids) {
        String[] arr = cids.split(",");
        List<Integer> cartIds = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            cartIds.add(Integer.parseInt(arr[i]));
        }

        List<ShoppingCartVO> list = shoppingCartMapper.selectShoppingCartsByCids(cartIds);
        return new ResultVO(ResultCode.SUCCESS,list);
    }
}
