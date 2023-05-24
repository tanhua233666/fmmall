package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.service.CategoryService;
import com.tanhua.fmmall.service.IndexImgService;
import com.tanhua.fmmall.service.ProductService;
import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/index")
@Api(value = "提供首页数据显示所需的接口",tags = "首页管理")
public class IndexController {
    @Resource
    private IndexImgService indexImgService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    @GetMapping("/indeximg")
    @ApiOperation("首页轮播图接口")
    public ResultVO listIndexImgs(){
        return indexImgService.listIndexImgs();

    }

    @GetMapping("/category-list")
    @ApiOperation("商品分类查询接口")
    public ResultVO listCategory(){
        return categoryService.listCategories();
    }

    @GetMapping("/list-recommends")
    @ApiOperation("新品推荐商品接口")
    public ResultVO listRecommendProducts(){
        return productService.listRecommendProducts();
    }

    @GetMapping("/category-recommends")
    @ApiOperation("分类推荐商品接口")
    public ResultVO listRecommendProductsByCategory(){
        return categoryService.listFirstCategories();
    }
}
