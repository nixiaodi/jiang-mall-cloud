package org.jiang.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserTokenDto extends LoginAuthDto {
    private static final long serialVersionUID = 3136723742371575367L;

    private Long id;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 最近操作人
     */
    private String lastOperator;

    /**
     * 最后操作人ID
     */
    private Long lastOperatorId;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 登录人Ip地址
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 访问token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 访问token的生效时间(s)
     */
    private Integer accessTokenValidity;

    /**
     * 刷新token的生效时间(s)
     */
    private Integer refreshTokenValidity;

    /**
     * 0 在线; 1 已刷新; 2 离线
     */
    private Integer status;
}
