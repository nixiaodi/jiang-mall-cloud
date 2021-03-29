package org.jiang.chat.route.api.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * google protocol 编解码发送
 * @author 蒋小胖
 */
public class ChatRequestVo extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "userId", example = "1545574049323")
    private Long userId ;


    @NotNull(message = "message 不能为空")
    @ApiModelProperty(required = true, value = "message", example = "hello")
    private String message ;

    public ChatRequestVo(Long userId) {
    }

    public ChatRequestVo(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatRequestVo{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                '}';
    }
}
