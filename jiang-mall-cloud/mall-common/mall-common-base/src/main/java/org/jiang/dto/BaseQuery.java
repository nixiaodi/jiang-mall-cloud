package org.jiang.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 3319698607712846427L;

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 每条页数
     */
    private Integer pageSize = 10;

    /**
     * 排序
     */
    private String orderBy;
}
