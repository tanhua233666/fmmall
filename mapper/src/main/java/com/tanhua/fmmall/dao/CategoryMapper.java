package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.Category;
import com.tanhua.fmmall.general.GeneralDao;
import com.tanhua.fmmall.vo.CategoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends GeneralDao<Category> {

    //1.连接查询
    public List<CategoryVO> selectAllCategories();

    //2.子查询
    public List<CategoryVO> selectAllCategories2(int parentId);

    // 查询一级类别
    public List<CategoryVO> selectFirstLevelCategories();
}