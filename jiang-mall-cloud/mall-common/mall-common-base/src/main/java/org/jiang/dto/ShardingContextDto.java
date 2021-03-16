package org.jiang.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShardingContextDto {
    /**
     * sharding total count
     */
    int shardingTotalCount;

    /**
     * sharding item
     */
    int shardingItem;

    public ShardingContextDto(final int shardingTotalCount,final int shardingItem) {
        this.shardingTotalCount = shardingTotalCount;
        this.shardingItem = shardingItem;
    }
}
