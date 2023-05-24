package com.tanhua.fmmall.controller;

import com.tanhua.fmmall.entity.UserAddr;
import com.tanhua.fmmall.service.UserAddrService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@Api(value = "提供收货地址接口",tags = "收货地址管理")
@RequestMapping("/useraddr")
public class UserAddrController {
    @Resource
    private UserAddrService userAddrService;

    @GetMapping("/list")
    @ApiOperation(value = "用户地址列表接口")
    @ApiImplicitParam(dataType = "int",name = "userId",value = "用户id",required = true)
    public ResultVO listAddr(Integer userId, @RequestHeader("token") String token){
        ResultVO resultVO = userAddrService.listAddrsByUid(userId);
        return resultVO;
    }

    @PostMapping("/addr")
    @ApiOperation(value = "用户新增地址接口")
    @ApiImplicitParam(dataType = "Object",name = "userAddr",value = "用户地址对象",required = true)
    public ResultVO addAddr(@RequestBody UserAddr userAddr, @RequestHeader("token") String token){
        boolean f = userAddrService.addAddr(userAddr);
        if(f){
            return new ResultVO(ResultCode.SUCCESS,null);
        }else {
            return new ResultVO(ResultCode.FAILURE,null);
        }
    }

    @PutMapping("/addr")
    @ApiOperation(value = "用户更新地址接口")
    @ApiImplicitParam(dataType = "Object",name = "userAddr",value = "用户地址对象",required = true)
    public ResultVO updateAddr(@RequestBody UserAddr userAddr, @RequestHeader("token") String token){
        boolean f = userAddrService.updateAddr(userAddr);
        if(f){
            return new ResultVO(ResultCode.SUCCESS,null);
        }else {
            return new ResultVO(ResultCode.FAILURE,null);
        }
    }

    @PutMapping("/addr/del")
    @ApiOperation(value = "用户删除地址接口")
    @ApiImplicitParam(dataType = "String",name = "addrId",value = "用户地址id",required = true)
    public ResultVO updateAddr(@RequestBody String addrId, @RequestHeader("token") String token){
        boolean f = userAddrService.delAddr(addrId);
        if(f){
            return new ResultVO(ResultCode.SUCCESS,null);
        }else {
            return new ResultVO(ResultCode.FAILURE,null);
        }
    }
}
