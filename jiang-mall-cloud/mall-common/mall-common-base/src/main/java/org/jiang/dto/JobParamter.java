package org.jiang.dto;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

public class JobParamter implements Serializable {
    private static final long serialVersionUID = -610797345091216847L;

    /**
     * 每次查询数据量
     */
    private int fetchNum;

    /**
     * 取模条件
     */
    private String condition;

    /**
     * 任务类型
     */
    private String taskType;

    public int getFetchNum() {
        Preconditions.checkArgument(fetchNum != 0);
        return fetchNum;
    }

    public void setFetchNum(int fetchNum) {
        this.fetchNum = fetchNum;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobTaskParameter{");
        sb.append("fetchNum=").append(fetchNum);
        sb.append(", condition='").append(condition).append('\'');
        sb.append(", taskType='").append(taskType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
