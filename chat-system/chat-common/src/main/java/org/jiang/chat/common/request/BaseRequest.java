package org.jiang.chat.common.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 蒋小胖
 */
public class BaseRequest {

    @ApiModelProperty(required = false,value = "唯一请求号",example = "1234567890")
    private String requestNo;

    @ApiModelProperty(required = false,value = "当前请求的时间戳",example = "0")
    private int timeStamp;

    public BaseRequest() {
        this.setTimeStamp((int)(System.currentTimeMillis() / 1000));
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "requestNo='" + requestNo + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
