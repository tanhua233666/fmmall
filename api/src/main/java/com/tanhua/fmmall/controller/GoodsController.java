package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/goods")
@Api(value = "提供商品添加、删除、修改及查询的接口",tags = "商品管理")
public class GoodsController {

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResultVO addGoods(){
        return null;
    }
//
//    @DeleteMapping("/{id}")
//    public ResultVO deleteGoods(@PathVariable("id") int goodsId){
//        System.out.println("delete" + goodsId);
//        return new ResultVO(100,"delete success",null);
//    }
//
//    @GetMapping("/{id}")
//    public ResultVO checkGoods(@PathVariable("id") int goodsId){
//        System.out.println("get" + goodsId);
//        return new ResultVO(100,"delete success",null);
//    }
}
