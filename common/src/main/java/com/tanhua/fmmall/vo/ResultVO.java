package com.tanhua.fmmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ResultVO对象",description = "封装接口返回给前端的数据")
public class ResultVO {

    @ApiModelProperty(value = "响应状态码",dataType = "int")
    private int code;       //响应给前端的状态码
    @ApiModelProperty("响应提示信息")
    private String msg;     //传递的提示信息
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("响应数据")
    private Object data;    //响应的数据


    public ResultVO(ResultCode resultCode,Object data){
        this.code = resultCode.code();
        this.msg = resultCode.message();
        this.data = data;
    }

    public ResultVO(int code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(ResultCode resultCode,String token,Object data){
        this.code = resultCode.code();
        this.msg = resultCode.message();
        this.token = token;
        this.data = data;
    }


    /**
     * 把ResultCode枚举转换为ResResult
     */
    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

}
