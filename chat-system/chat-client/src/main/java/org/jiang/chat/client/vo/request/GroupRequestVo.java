package org.jiang.chat.client.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 群发请求
 */
public class GroupRequestVo extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "消息发送者的 userId", example = "1545574049323")
    private Long userId ;


    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg ;

    public GroupRequestVo (Long userId, String msg) {
        this.userId = userId;
        this.msg = msg;
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
