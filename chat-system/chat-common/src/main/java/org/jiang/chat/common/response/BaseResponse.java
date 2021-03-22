package org.jiang.chat.common.response;

import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.utils.StringUtil;

import java.io.Serializable;

/**
 * @author 蒋小胖
 */
public class BaseResponse<T> implements Serializable {

    private String code;
    private String message;

    /**
     * 请求号
     */
    private String requestNo;

    /**
     * 响应体
     */
    private T dataBody;

    public BaseResponse(T dataBody) {
        this.dataBody = dataBody;
    }

    public BaseResponse(String code,String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(String code,String message,T dataBody) {
        this.code = code;
        this.message = message;
        this.dataBody = dataBody;
    }

    public BaseResponse(String code, String message, String requestNo, T dataBody) {
        this.code = code;
        this.message = message;
        this.requestNo = requestNo;
        this.dataBody = dataBody;
    }

    public static <T> BaseResponse<T> create(T t) {
        return new BaseResponse<T>(t);
    }

    public static <T> BaseResponse<T> create(T t, StatusEnum statusEnum) {
        return new BaseResponse<>(statusEnum.getCode(),statusEnum.getMessage(),t);
    }

    public static <T> BaseResponse<T> createSuccess(T t, String message){
        return new BaseResponse<T>(StatusEnum.SUCCESS.getCode(), StringUtil.isNullOrEmpty(message) ? StatusEnum.SUCCESS.getMessage() : message, t);
    }

    public static <T> BaseResponse<T> createFail(T t, String message){
        return new BaseResponse<T>(StatusEnum.FAIL.getCode(), StringUtil.isNullOrEmpty(message) ? StatusEnum.FAIL.getMessage() : message, t);
    }

    public static <T> BaseResponse<T> create(T t, StatusEnum statusEnum, String message){

        return new BaseResponse<T>(statusEnum.getCode(), message, t);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public T getDataBody() {
        return dataBody;
    }

    public void setDataBody(T dataBody) {
        this.dataBody = dataBody;
    }
}

