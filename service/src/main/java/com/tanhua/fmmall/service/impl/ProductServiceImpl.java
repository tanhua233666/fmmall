package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.ProductImgMapper;
import com.tanhua.fmmall.dao.ProductMapper;
import com.tanhua.fmmall.dao.ProductParamsMapper;
import com.tanhua.fmmall.dao.ProductSkuMapper;
import com.tanhua.fmmall.entity.Product;
import com.tanhua.fmmall.entity.ProductImg;
import com.tanhua.fmmall.entity.ProductParams;
import com.tanhua.fmmall.entity.ProductSku;
import com.tanhua.fmmall.service.ProductService;
import com.tanhua.fmmall.utils.PageHelper;
import com.tanhua.fmmall.vo.ProductVO;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductImgMapper productImgMapper;

    @Resource
    private ProductSkuMapper productSkuMapper;

    @Resource
    private ProductParamsMapper productParamsMapper;

    @Override
    public ResultVO listRecommendProducts() {
        List<ProductVO> productVOS = productMapper.selectRecommendProducts();
        return new ResultVO(ResultCode.SUCCESS,"success",productVOS);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResultVO getProductBasicInfo(String productId) {
        // 1.商品基本信息
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        criteria.andEqualTo("productStatus",1); // 状态1，上架商品

        List<Product> products = productMapper.selectByExample(example);
        if(products.size()>0){
            // 2.商品图片
            Example example1 = new Example(ProductImg.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("itemId",productId);
            List<ProductImg> productImgs = productImgMapper.selectByExample(example1);

            // 3.商品套餐
            Example example2 = new Example(ProductSku.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("productId",productId);
            criteria2.andEqualTo("status",1);
            List<ProductSku> productSkus = productSkuMapper.selectByExample(example2);

            HashMap<String,Object> basicInfo = new HashMap<>();
            basicInfo.put("product",products.get(0));
            basicInfo.put("productImgs",productImgs);
            basicInfo.put("productSkus",productSkus);

            return new ResultVO(ResultCode.SUCCESS,"success",basicInfo);
        }else {
            return new ResultVO(ResultCode.FAILURE,"查询商品不存在",null);
        }

    }

    @Override
    public ResultVO getProductParamsById(String productId) {
        Example example = new Example(ProductParams.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);
        List<ProductParams> productParams = productParamsMapper.selectByExample(example);
        if (productParams.size() > 0){
            return new ResultVO(ResultCode.SUCCESS,"success",productParams.get(0));
        }else {
            return new ResultVO(ResultCode.FAILURE,"三无产品",null);
        }
    }

    @Override
    public ResultVO getProductByCategoryId(int categoryId, int pageNum, int limit) {
        //1.查询分页数据
        int start = (pageNum-1)*limit;
        List<ProductVO> productVOS = productMapper.selectProductByCategoryId(categoryId, start, limit);
        //2.查询当前类别下的总记录数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        int count = productMapper.selectCountByExample(example);
        //3.计算总页数
        int pageCount = count%limit == 0 ? count/limit : count/limit+1;
        //4.封装返回数据
        PageHelper<ProductVO> pageHelper = new PageHelper<>(count, pageCount, productVOS);
        return new ResultVO(ResultCode.SUCCESS,pageHelper);
    }

    @Override
    public ResultVO listBrands(int categoryId) {
        List<String> brands = productMapper.selectBrandByCategoryId(categoryId);
        return new ResultVO(ResultCode.SUCCESS,brands);
    }

    @Override
    public ResultVO listBrands(String kw) {
        kw = "%" + kw + "%";
        List<String> brands = productMapper.selectBrandByKeyword(kw);
        return new ResultVO(ResultCode.SUCCESS,brands);
    }

    @Override
    public ResultVO getProductByKeyword(String kw, int pageNum, int limit) {
        //1.查询搜索结果
        kw = "%" + kw + "%";
        int start = (pageNum-1)*limit;
        List<ProductVO> productVOS = productMapper.selectProductByKeyword(kw, start, limit);
        //2.查询总记录数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("productName",kw);
        int count = productMapper.selectCountByExample(example);
        //3.计算总页数
        int pageCount = count%limit == 0? count/limit : count/limit + 1;


        //4.封装返回数据
        PageHelper<ProductVO> pageHelper = new PageHelper<>(count, pageCount, productVOS);
        ResultVO resultVO = new ResultVO(ResultCode.SUCCESS, pageHelper);
        return resultVO;
    }
}
