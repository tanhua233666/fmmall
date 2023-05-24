package com.tanhua.fmmall.service;

import com.tanhua.fmmall.vo.ResultVO;

public interface ProductService {
    public ResultVO listRecommendProducts();

    public ResultVO getProductBasicInfo(String productId);

    public ResultVO getProductParamsById(String productId);

    public ResultVO getProductByCategoryId(int categoryId,int pageNum,int limit);

    public ResultVO listBrands(int categoryId);

    public ResultVO getProductByKeyword(String kSw,int pageNum,int limit);

    public ResultVO listBrands(String kw);
}
