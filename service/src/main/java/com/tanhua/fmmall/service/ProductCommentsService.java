package com.tanhua.fmmall.service;

import com.tanhua.fmmall.vo.ResultVO;

public interface ProductCommentsService {
    //商品评论分页查询
    public ResultVO ListCommentsByProductId(String productId,int pageNum,int limit);

    //商品评论统计信息
    public ResultVO getCommentsCountByProductId(String productId);

    //商品评论添加
    public ResultVO addComments(String productId,String productName,String userId,int isAnonymous,int commType,String commContent);
}
