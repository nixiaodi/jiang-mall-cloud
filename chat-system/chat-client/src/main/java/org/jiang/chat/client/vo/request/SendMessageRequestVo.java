package org.jiang.chat.client.vo.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class SendMessageRequestVo {
    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg ;

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "userId", example = "11")
    private Long userId ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
