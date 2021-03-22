package org.jiang.chat.common.route.algorithm.consistenthash;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TreeMap实现类
 * @author 蒋小胖
 */
@Slf4j
public class TreeMapConsistentHash extends AbstartConstentHash{

    private TreeMap<Long,String> treeMap = new TreeMap<>();

    /**
     * 虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 2;

    /**
     *
     * @param key
     * @param value
     */
    @Override
    protected void add(long key, String value) {
        // fix https://github.com/crossoverJie/cim/issues/79
        treeMap.clear();
        for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
            Long hash = super.hash("vir" + key + i);
            treeMap.put(hash,value);
        }
        treeMap.put(key,value);
    }

    @Override
    protected String getFirstNodeValue(String value) {
        Long hash = super.hash(value);
        log.info("value=" + value + " hash=" + hash);
        SortedMap<Long, String> last = treeMap.tailMap(hash);
        if (!last.isEmpty()) {
            return last.get(last.firstKey());
        }
        if (treeMap.size() == 0) {
            throw new ChatException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        return treeMap.firstEntry().getValue();
    }
}
