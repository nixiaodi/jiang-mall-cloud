package org.jiang.chat.common.route.algorithm.consistenthash;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.data.construct.SortArrayMap;

/**
 * 自定义map排序
 * @author 蒋小胖
 */
@Slf4j
public class SortArrayMapConsistentHash extends AbstartConstentHash{

    private SortArrayMap sortArrayMap = new SortArrayMap();

    /**
     * 虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 2 ;

    @Override
    protected void add(long key, String value) {
        // fix https://github.com/crossoverJie/cim/issues/79
        sortArrayMap.clear();
        for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
            Long hash = super.hash("vir" + key + i);
            sortArrayMap.add(hash,value);
        }
        sortArrayMap.add(key, value);
    }

    @Override
    protected void sort() {
        sortArrayMap.sort();
    }

    @Override
    protected String getFirstNodeValue(String value) {
        long hash = super.hash(value);
        log.info("value=" + value + " hash=" + hash);
        return sortArrayMap.firstNodeValue(hash);
    }
}
