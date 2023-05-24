package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.ProductSku;
import com.tanhua.fmmall.general.GeneralDao;

import java.util.List;

public interface ProductSkuMapper extends GeneralDao<ProductSku> {
    //根据商品id查询价格最低的套餐
    public List<ProductSku> selectLowerestPriceByProductId(String productId);
}