package org.jiang.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MqMessageDto extends BaseVo{
    private static final long serialVersionUID = 5440371083922622116L;

    /**
     * messageKey
     */
    private String messageKey;

    /**
     * topic
     */
    private String messageTopic;

    /**
     * tag
     */
    private String messageTag;

    /**
     * messageBody
     */
    private String messageBody;

    /**
     * 消息类型: 10 - 生产者; 20 - 消费者
     */
    private Integer messageType;

    /**
     * 顺序类型: 0 - 有序; 1 - 无序
     */
    private Integer orderType;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 延时级别
     */
    private Integer delayLevel;
}
