package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.ProductImg;
import com.tanhua.fmmall.general.GeneralDao;

import java.util.List;

public interface ProductImgMapper extends GeneralDao<ProductImg> {
    public List<ProductImg> selectProductImgByProductId(int productId);
}