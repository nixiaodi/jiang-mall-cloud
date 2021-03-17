package org.jiang.core.registry.base;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ReliableMessageRegisterDto {
    private String consumerGroup;
    private String producerGroup;
    private String nameSrvAddr;

    /**
     * Sets consumer group.
     * @param consumerGroup the consumer group
     * @return the consumer group
     */
    public ReliableMessageRegisterDto setConsumerGroup(final String consumerGroup) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(consumerGroup), "init zk cid is null");
        this.consumerGroup = consumerGroup;
        return this;
    }

    /**
     * Sets producer group
     * @param producerGroup the producer group
     * @return the producer group
     */
    public ReliableMessageRegisterDto setProducerGroup(final String producerGroup) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(producerGroup), "init zk pid is null");
        this.producerGroup = producerGroup;
        return this;
    }

    /**
     * Sets nameSrv addr.
     * @param nameSrvAddr the namesrv addr
     * @return the nameSrv addr
     */
    public ReliableMessageRegisterDto setNamesrvAddr(final String nameSrvAddr) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(nameSrvAddr), "init ZK nameSrvAddr is null");
        this.nameSrvAddr = nameSrvAddr;
        return this;
    }
}
