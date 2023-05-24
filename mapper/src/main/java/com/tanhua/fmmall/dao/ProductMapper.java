package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.Product;
import com.tanhua.fmmall.general.GeneralDao;
import com.tanhua.fmmall.vo.ProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends GeneralDao<Product> {

    public List<ProductVO> selectRecommendProducts();

    //查询指定一级类别下销量最高的6个商品
    public List<ProductVO> selectTopSixByCategory(int cid);

    //按照三级分类id分页查询分类下的商品信息
    public List<ProductVO> selectProductByCategoryId(@Param("cid") int cid,
                                                     @Param("start") int start,
                                                     @Param(("limit")) int limit);

    //根据类别id查询当前分类下的所有品牌
    public  List<String> selectBrandByCategoryId(int cid);


    //根据关键字模糊查询商品信息
    public List<ProductVO> selectProductByKeyword(@Param("kw") String kw,
                                                     @Param("start") int start,
                                                     @Param(("limit")) int limit);

    //根据关键字查询商品对应品牌
    public  List<String> selectBrandByKeyword(String kw);
}