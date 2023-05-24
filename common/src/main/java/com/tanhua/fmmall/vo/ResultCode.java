package com.tanhua.fmmall.vo;

public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(101, "成功"),
    FAILURE(100,"失败"),

    /* 系统500错误*/
    SYSTEM_ERROR(500, "系统异常，请稍后重试"),


    /* 参数错误：1001-1999 */
    PARAM_IS_INVALID(101, "参数无效"),


    /* 用户错误：201-299*/
    USER_NOT_EXIST(200,"用户不存在"),
    USER_HAS_EXIST(210, "用户已存在"),
    USER_LOGIN_SUCCESS(201,"登录认证成功"),
    USER_LOGIN_NOT(203,"用户未登录,请先登录"),
    USER_LOGIN_FAIL(202,"账号或密码错误"),
    USER_LOGIN_OVERDUE(204,"用户登录过期"),



    /* 认证失败错误：30001-39999*/
    NO_TOKEN(30001,"无token，请重新登录"),
    TOKEN_OUT_TIME(30002,"token超时,请重新登录"),
    TOKEN_ILLEGAL(30003,"token 认证失败"),

    /* 文件失败错误：40001-49999*/
    EXCEL_NO_SHEET(40001,"Excel无Sheet");


    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
