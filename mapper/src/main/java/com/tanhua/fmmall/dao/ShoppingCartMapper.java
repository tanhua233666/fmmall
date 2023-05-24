package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.ShoppingCart;
import com.tanhua.fmmall.general.GeneralDao;
import com.tanhua.fmmall.vo.ShoppingCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartMapper extends GeneralDao<ShoppingCart> {

    public List<ShoppingCartVO> selectShoppingCartByUserId(int userId);

    public int updateCartNumByCartId(@Param("cartId") int cartId,
                                     @Param("cartNum") int cartNum);

    public List<ShoppingCartVO> selectShoppingCartsByCids(List<Integer> cids);

}