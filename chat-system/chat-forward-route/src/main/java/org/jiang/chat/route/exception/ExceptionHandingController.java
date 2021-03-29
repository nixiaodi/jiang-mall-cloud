package org.jiang.chat.route.exception;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.response.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 蒋小胖
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandingController {

    @ExceptionHandler(ChatException.class)
    @ResponseBody
    public BaseResponse handleAllException(ChatException e) {
        log.error("exception",e);
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
