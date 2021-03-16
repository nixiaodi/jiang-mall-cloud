package org.jiang.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;

@Data
@ApiModel(value = "更新状态")
public class UpdateStatusDto implements Serializable {
    private static final long serialVersionUID = 1494899235149813850L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long id;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
}
