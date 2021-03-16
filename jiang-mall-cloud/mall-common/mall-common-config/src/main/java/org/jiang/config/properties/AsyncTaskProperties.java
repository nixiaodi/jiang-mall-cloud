package org.jiang.config.properties;

import lombok.Data;

@Data
public class AsyncTaskProperties {

    private int corePoolSize = 50;

    private int maxPoolSize = 100;

    private int queueCapacity = 10000;

    private int keepAliveSeconds = 3000;

    private String threadNamePrefix = "jiang-task-executor-";
}
