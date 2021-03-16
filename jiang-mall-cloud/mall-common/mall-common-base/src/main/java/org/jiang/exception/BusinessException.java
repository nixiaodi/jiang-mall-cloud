package org.jiang.exception;

import lombok.extern.slf4j.Slf4j;
import org.jiang.enums.ErrorCode;

@Slf4j
public class BusinessException extends RuntimeException{

    /**
     * 异常码
     */
    protected int code;

    private static final long serialVersionUID = 3160241586346324994L;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message,Throwable cause) {
        super(message,cause);
    }

    public BusinessException(int code,String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code,String msgFormat,Object... args) {
        super(String.format(msgFormat,args));
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode,Object... args) {
        super(String.format(errorCode.getMsg(),args));
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
