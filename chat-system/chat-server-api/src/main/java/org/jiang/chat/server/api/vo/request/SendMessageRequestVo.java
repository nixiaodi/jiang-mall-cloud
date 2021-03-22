package org.jiang.chat.server.api.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author 蒋小胖
 */
public class SendMessageRequestVo extends BaseRequest {

    @NotNull(message = "message 不能为空")
    @ApiModelProperty(required = true,value = "message",example = "hello")
    private String message;

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true,value = "userId",example = "11")
    private Long userId;

    public SendMessageRequestVo() {

    }

    public SendMessageRequestVo(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendMessageRequestVo{" +
                "message='" + message + '\'' +
                ", userId=" + userId +
                '}';
    }
}
