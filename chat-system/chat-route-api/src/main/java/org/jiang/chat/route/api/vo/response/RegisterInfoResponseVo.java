package org.jiang.chat.route.api.vo.response;

import java.io.Serializable;

/**
 * @author 蒋小胖
 */
public class RegisterInfoResponseVo implements Serializable {
    private Long userId ;
    private String userName ;

    public RegisterInfoResponseVo(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

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
        return "RegisterInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
