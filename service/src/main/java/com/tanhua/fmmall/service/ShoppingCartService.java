package com.tanhua.fmmall.service;

import com.tanhua.fmmall.entity.ShoppingCart;
import com.tanhua.fmmall.vo.ResultVO;

import java.util.List;

public interface ShoppingCartService {
    public ResultVO addShoppingCart(ShoppingCart cart);

    public ResultVO delShoppingCart(int[] cartId);

    public ResultVO showShoppingCartList(int userId);

    public ResultVO updateCartNumByCartId(int cartId,int cartNum);

    public ResultVO listShoppingCartsByCids(String cids);
}
