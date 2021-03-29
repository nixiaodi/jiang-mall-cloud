package org.jiang.chat.client.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;

public class P2PRequestVo extends BaseRequest {
    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "消息发送者的 userId", example = "1545574049323")
    private Long userId ;


    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "消息接收者的 userId", example = "1545574049323")
    private Long receiveUserId ;




    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg ;

    public P2PRequestVo() {
    }

    public P2PRequestVo (Long userId, Long receiveUserId, String msg) {
        this.userId = userId;
        this.receiveUserId = receiveUserId;
        this.msg = msg;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
