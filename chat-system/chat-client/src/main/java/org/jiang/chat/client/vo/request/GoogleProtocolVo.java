package org.jiang.chat.client.vo.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * google protocol 编解码发送
 */
public class GoogleProtocolVo {
    @NotNull(message = "requestId 不能为空")
    @ApiModelProperty(required = true, value = "requestId", example = "123")
    private Integer requestId ;

    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "GoogleProtocolVO{" +
                "requestId=" + requestId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
