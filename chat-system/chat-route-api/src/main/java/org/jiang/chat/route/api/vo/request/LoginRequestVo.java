package org.jiang.chat.route.api.vo.request;

import org.jiang.chat.common.request.BaseRequest;

/**
 * @author 蒋小胖
 */
public class LoginRequestVo extends BaseRequest {
    private Long userId ;
    private String userName ;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
