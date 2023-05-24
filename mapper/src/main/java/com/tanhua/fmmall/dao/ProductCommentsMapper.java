package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.ProductComments;
import com.tanhua.fmmall.general.GeneralDao;
import com.tanhua.fmmall.vo.ProductCommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCommentsMapper extends GeneralDao<ProductComments> {

    /**
    *  根据商品id分页查询评论信息
     * @param productId 商品id
     * @param start 起始索引
     * @param limit 查询条数
     * @return
    * */
    public List<ProductCommentsVO> selectCommentsByProductId(@Param("productId") String productId,
                                                             @Param("start") int start,
                                                             @Param("limit") int limit);
}