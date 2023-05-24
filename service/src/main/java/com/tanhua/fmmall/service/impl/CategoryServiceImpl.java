package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.CategoryMapper;
import com.tanhua.fmmall.service.CategoryService;
import com.tanhua.fmmall.vo.CategoryVO;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    // 查询分类列表（三个分类）
    @Override
    public ResultVO listCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectAllCategories2(0);
        return new ResultVO(ResultCode.SUCCESS,"success",categoryVOS);
    }

    // 查询一级分类（同时包含销量销量最高的六个商品）
    @Override
    public ResultVO listFirstCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectFirstLevelCategories();
        return new ResultVO(ResultCode.SUCCESS,"success",categoryVOS);
    }
}
