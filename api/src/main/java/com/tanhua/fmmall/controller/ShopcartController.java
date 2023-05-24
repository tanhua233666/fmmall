package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.entity.ShoppingCart;
import com.tanhua.fmmall.service.ShoppingCartService;
import com.tanhua.fmmall.utils.Base64Utils;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shopcart")
@CrossOrigin
@Api(value = "提供购物车业务接口",tags = "购物车管理")
public class ShopcartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    @ApiOperation(value = "购物车列表接口")
    @ApiImplicitParam(dataType = "int",name = "userId",value = "用户ID",required = true)
    public ResultVO listCarts(int userId,@RequestHeader("token") String token){
        ResultVO resultVO = shoppingCartService.showShoppingCartList(userId);
        return resultVO;
    }

    @PostMapping("/add")
    @ApiOperation(value = "商品加购接口")
    @ApiImplicitParam(dataType = "ShoppingCart",name = "cart",value = "购物车商品",required = true)
    public ResultVO addShoppingCart(@RequestBody ShoppingCart cart,@RequestHeader("token") String token){
        ResultVO resultVO = shoppingCartService.addShoppingCart(cart);
        return resultVO;
    }

    @PostMapping("/del")
    @ApiOperation(value = "购物车商品删除接口")
    @ApiImplicitParam(dataType = "int[]",name = "cartIds",value = "购物车商品id",required = true)
    public ResultVO addShoppingCart(@RequestBody int[] cartIds,@RequestHeader("token") String token){
        ResultVO resultVO = shoppingCartService.delShoppingCart(cartIds);
        return resultVO;
    }

    @PutMapping("/update/{cid}/{cnum}")
    @ApiOperation(value = "修改购物车商品数量接口")
    @ApiImplicitParam(dataType = "int",name = "cartId",value = "商品ID",required = true)
    public ResultVO updateCartNum(@PathVariable("cid") Integer cartId,@PathVariable("cnum") Integer cartNum,@RequestHeader("token") String token){
        ResultVO resultVO = shoppingCartService.updateCartNumByCartId(cartId, cartNum);
        return resultVO;
    }

    @GetMapping("listbycids")
    @ApiOperation(value = "购物车商品选择查询接口")
    @ApiImplicitParam(dataType = "string",name = "cids",value = "加购商品ID集",required = true)
    public ResultVO listByCids(String cids,@RequestHeader("token") String token){
        ResultVO resultVO = shoppingCartService.listShoppingCartsByCids(cids);
        return resultVO;
    }

}
