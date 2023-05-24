package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.ProductCommentsMapper;
import com.tanhua.fmmall.entity.ProductComments;
import com.tanhua.fmmall.service.ProductCommentsService;
import com.tanhua.fmmall.utils.PageHelper;
import com.tanhua.fmmall.vo.ProductCommentsVO;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ProductCommentsServiceImpl implements ProductCommentsService {
    @Resource
    private ProductCommentsMapper productCommentsMapper;

    @Override
    public ResultVO ListCommentsByProductId(String productId,int pageNum,int limit) {
//        List<ProductCommentsVO> productCommentsVOS = productCommentsMapper.selectCommentsByProductId(productId);
       // 根据商品id查询总记录数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        int count = productCommentsMapper.selectCountByExample(example);

        // 计算总页数（需确定每页显示数量pageSize = limit)
        int pageCount = count/limit == 0 ? count/limit : count/limit + 1;

        int start = (pageNum-1) * limit;
        // 查询当前页的数据，评论需要用户信息，联表查询
        List<ProductCommentsVO> list = productCommentsMapper.selectCommentsByProductId(productId, start, limit);


        return new ResultVO(ResultCode.SUCCESS,"success",new PageHelper<ProductCommentsVO>(count,pageCount,list));
    }

    @Override
    public ResultVO getCommentsCountByProductId(String productId) {
        // 1.查询当前商品评价的总数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        int total = productCommentsMapper.selectCountByExample(example);

        // 2.查询好评数
        criteria.andEqualTo("commType",1);
        int goodTotal = productCommentsMapper.selectCountByExample(example);

        // 3.查询中评数
        Example example1 = new Example(ProductComments.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("productId",productId);
        criteria1.andEqualTo("commType",0);
        int midTotal = productCommentsMapper.selectCountByExample(example1);

        // 4.查询差评数
        Example example2 = new Example(ProductComments.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("productId",productId);
        criteria2.andEqualTo("commType",-1);
        int badTotal = productCommentsMapper.selectCountByExample(example2);

        // 5.计算好评率
        double percent = (Double.parseDouble("" + goodTotal) / Double.parseDouble("" + total)) * 100;
        String percentValue = (percent + "").substring(0,(percent + "").lastIndexOf(".") + 2);

        HashMap<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("goodTotal",goodTotal);
        map.put("midTotal",midTotal);
        map.put("badTotal",badTotal);
        map.put("percent",percentValue);

        return new ResultVO(ResultCode.SUCCESS,"success",map);
    }

    @Override
    public ResultVO addComments(String productId, String productName, String userId,int isAnonymous, int commType, String commContent) {
        String commId = System.currentTimeMillis() + "" + (new Random().nextInt(79999)+20001);
        ProductComments productComments = new ProductComments(commId,productId,productName,userId,isAnonymous,commType,commContent,new Date(),1);
        int count = productCommentsMapper.insert(productComments);
        if(count > 0) {
            return new ResultVO(ResultCode.SUCCESS,null);
        }else {
            return new ResultVO(ResultCode.FAILURE,null);
        }
    }
}
