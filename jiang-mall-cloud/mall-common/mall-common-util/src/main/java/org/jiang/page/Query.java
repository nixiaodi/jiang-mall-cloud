package org.jiang.page;

import java.io.Serializable;

public class Query implements Serializable {
    private static final long serialVersionUID = 8933019121780323520L;

    /**
     * 当前页
     */
    private int pageNum = 1;
    /**
     * 每页的数量
     */
    private int pageSize = 20;
}
