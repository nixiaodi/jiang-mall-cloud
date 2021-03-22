package org.jiang.chat.route.api.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;
import java.security.MessageDigest;

/**
 * @author 蒋小胖
 */
public class P2PRequestVo extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "消息发送者的 userId", example = "1545574049323")
    private Long userId ;


    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "消息接收者的 userId", example = "1545574049323")
    private Long receiveUserId ;

    @NotNull(message = "message 不能为空")
    @ApiModelProperty(required = true, value = "message", example = "hello")
    private String message ;

    public P2PRequestVo(Long userId, Long receiveUserId, String message) {
        this.userId = userId;
        this.receiveUserId = receiveUserId;
        this.message = message;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
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
        return "GroupReqVO{" +
                "userId=" + userId +
                ", msg='" + message + '\'' +
                "} " + super.toString();
    }
}
