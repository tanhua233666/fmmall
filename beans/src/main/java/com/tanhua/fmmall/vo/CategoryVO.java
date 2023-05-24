package com.tanhua.fmmall.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
*  封装首页类别商品推荐
*
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {

    private Integer categoryId;

    private String categoryName;

    private Integer categoryLevel;

    private Integer parentId;

    private String categoryIcon;

    private String categorySlogan;

    private String categoryPic;

    private String categoryBgColor;
    // 首页类别显示
    private List<CategoryVO> categories;
    // 首页分类商品推荐
    private List<ProductVO> products;

    @Override
    public String toString() {
        return "CategoryVO{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryLevel=" + categoryLevel +
                ", parentId=" + parentId +

                ", categories=" + categories +
                ", products=" + products +
                '}';
    }
}