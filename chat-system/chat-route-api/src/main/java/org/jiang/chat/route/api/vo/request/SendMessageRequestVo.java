package org.jiang.chat.route.api.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.jiang.chat.common.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author 蒋小胖
 */
public class SendMessageRequestVo extends BaseRequest {
    @NotNull(message = "message 不能为空")
    @ApiModelProperty(required = true, value = "message", example = "hello")
    private String message ;

    @NotNull(message = "id 不能为空")
    @ApiModelProperty(required = true, value = "id", example = "11")
    private long id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
