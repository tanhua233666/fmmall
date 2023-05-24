package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.service.OrderService;
import com.tanhua.fmmall.service.ProductCommentsService;
import com.tanhua.fmmall.service.ProductService;
import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/product")
@Api(value = "提升商品信息相关接口",tags = "商品管理")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductCommentsService productCommentsService;

    @Resource
    private OrderService orderService;

    // 查询商品详情信息
    @GetMapping("/detail-info/{pid}")
    @ApiOperation("商品基本信息查询接口")
    public ResultVO getProductBasicInfo(@PathVariable("pid") String pid){
        ResultVO productBasicInfo = productService.getProductBasicInfo(pid);
        return productBasicInfo;
    }

    // 查询商品参数信息
    @ApiOperation("商品参数信息接口")
    @GetMapping("/detail-params/{pid}")
    public ResultVO getProductParamsById(@PathVariable("pid") String pid){
        return productService.getProductParamsById(pid);
    }

    @ApiOperation("商品评论添加接口")
    @GetMapping("/add-comments/")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "itemId",value = "商品快照id",required = true),
            @ApiImplicitParam(dataType = "string",name = "productId",value = "商品id",required = true),
            @ApiImplicitParam(dataType = "string",name = "productName",value = "商品名称",required = false),
            @ApiImplicitParam(dataType = "string",name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(dataType = "int",name = "isAnonymous",value = "是否匿名",required = false),
            @ApiImplicitParam(dataType = "int",name = "commType",value = "评论类型",required = true),
            @ApiImplicitParam(dataType = "string",name = "commContent",value = "评论内容",required = true)
    })
    public ResultVO addComments(String itemId,String productId, String productName, String userId,int isAnonymous, int commType, String commContent) {
        orderService.submitOrderItemIsComment(itemId);
        return productCommentsService.addComments(productId,productName,userId,isAnonymous,commType,commContent);
    }

    @ApiOperation("商品分页评论信息接口")
    @GetMapping("/detail-comments/{pid}")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int",name = "pageNum",value = "当前页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit",value = "显示条数",required = true)
    })
    public ResultVO getProductCommentsById(@PathVariable("pid") String pid,int pageNum,int limit){
        return productCommentsService.ListCommentsByProductId(pid,pageNum,limit);
    }

    @ApiOperation("商品评论统计接口")
    @GetMapping("/detail-comments-count/{pid}")
    public ResultVO getCommentsCountByProductId(@PathVariable("pid") String pid){
        return productCommentsService.getCommentsCountByProductId(pid);
    }

    @ApiOperation("商品类别查询信息接口")
    @GetMapping("/listbycid/{cid}")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int",name = "pageNum",value = "当前页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit",value = "显示条数",required = true)
    })
    public ResultVO getProductCommentsById(@PathVariable("cid") int cid,int pageNum,int limit){
        return productService.getProductByCategoryId(cid,pageNum,limit);
    }

    @ApiOperation("类别查询品牌接口")
    @GetMapping("/listbrands/{cid}")
    public ResultVO getBrandsByCategoryId(@PathVariable("cid") int cid){
        return productService.listBrands(cid);
    }

    @ApiOperation("关键字查询品牌接口")
    @GetMapping("/listbrands-keyword")
    @ApiImplicitParam(dataType = "string",name = "kw",value = "搜索关键字",required = true)
    public ResultVO getBrandsByKeyword(String kw){
        return productService.listBrands(kw);
    }

    @ApiOperation("关键字查询商品信息接口")
    @GetMapping("/listbykeyword")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string",name = "kw",value = "搜索关键字",required = true),
            @ApiImplicitParam(dataType = "int",name = "pageNum",value = "当前页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit",value = "显示条数",required = true)
    })
    public ResultVO searchProducts(String kw,int pageNum,int limit){
        return productService.getProductByKeyword(kw,pageNum,limit);
    }
}
