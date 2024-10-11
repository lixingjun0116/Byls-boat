package com.byls.boat.util;


import com.byls.boat.common.ResponseResult;
import com.byls.boat.enums.MessageEnum;

/**
 * @ClassName: ResponseUtil
 * @Description: 响应工具类
 */
public class ResponseUtil {
    /**
     * @param <T>
     * @param t
     * @return ResponseResult<T> 返回类型
     * @Title: successResponse
     * @Description: 带参数的返回结果
     */
    public static <T> ResponseResult<T> successResponse(T t) {
        ResponseResult<T> result = new ResponseResult<T>(t);
        result.setSuccess(true);
        result.setCode(MessageEnum.SUCCESS.getCode());
        result.setMsg(MessageEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * @return ResponseResult<T> 返回类型
     * &#064;Title:  failResponse  &#064;Description:  有参数的返回失败操作
     */
    public static <T> ResponseResult<T> failResponse(T t) {
        ResponseResult<T> result = new ResponseResult<T>(t);
        result.setSuccess(false);
        result.setCode(MessageEnum.FAIL.getCode());
        result.setMsg(MessageEnum.FAIL.getMessage());
        return result;
    }

    /**
     * @return ResponseResult<String> 返回类型
     * &#064;Title:  successResponse
     * &#064;Description:  无参数的返回结果
     */
    public static ResponseResult<String> successResponse() {
        ResponseResult<String> result = new ResponseResult<String>();
        result.setSuccess(true);
        result.setCode(MessageEnum.SUCCESS.getCode());
        result.setMsg(MessageEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * @return ResponseResult<String> 返回类型
     * &#064;Title:  failResponse  &#064;Description:无参数的  返回失败操作
     */
    public static ResponseResult<String> failResponse() {
        ResponseResult<String> result = new ResponseResult<String>();
        result.setSuccess(false);
        result.setCode(MessageEnum.FAIL.getCode());
        result.setMsg(MessageEnum.FAIL.getMessage());
        return result;
    }

    /**
     * @return ResponseResult<String> 返回类型
     * &#064;Title:  failResponse
     * &#064;Description:无参数的  返回失败操作
     */
    public static ResponseResult<String> failResponse(String mesg) {
        ResponseResult<String> result = new ResponseResult<String>();
        result.setSuccess(false);
        result.setCode(MessageEnum.FAIL.getCode());
        result.setMsg(mesg);
        return result;
    }

    /**
     * @return ResponseResult<?> 返回类型
     * &#064;Title:  failResponse
     * &#064;Description:  返回失败操作
     */
    public static ResponseResult<String> failResponse(String code, String mesg) {
        ResponseResult<String> result = new ResponseResult<String>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(mesg);
        return result;
    }

    /**
     * @return Boolean 返回类型
     * &#064;Title:  isSuccess
     * &#064;Description:  是否成功
     */
    public static <T> Boolean isSuccess(ResponseResult<T> result) {
        return MessageEnum.SUCCESS.getCode().equals(result.getCode());
    }

    /**
     * @param <T>
     * @param result
     * @return Boolean 返回类型
     * @Title: isFail
     * @Description: 是否失败
     */
    public static <T> Boolean isFail(ResponseResult<T> result) {
        return MessageEnum.FAIL.getCode().equals(result.getCode());
    }
}
