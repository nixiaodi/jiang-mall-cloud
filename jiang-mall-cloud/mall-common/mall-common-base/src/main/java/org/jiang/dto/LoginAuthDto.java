package org.jiang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "登录人信息")
public class LoginAuthDto implements Serializable {
    private static final long serialVersionUID = -1137852221455042256L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "组织ID")
    private Long groupId;

    @ApiModelProperty(value = "组织名称")
    private String groupName;

    public LoginAuthDto() {
    }

    public LoginAuthDto(Long userId, String loginName, String username) {
        this.userId = userId;
        this.loginName = loginName;
        this.username = username;
    }

    public LoginAuthDto(Long userId, String loginName, String username, Long groupId, String groupName) {
        this.userId = userId;
        this.loginName = loginName;
        this.username = username;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
