package org.jiang.chat.common.route.algorithm.consistenthash;

import org.jiang.chat.common.route.algorithm.RouteHandler;

import java.util.List;

/**
 * 通过一致hash进行路由选择
 * @author 蒋小胖
 */
public class ConsistentHashHandler implements RouteHandler {
    private AbstartConstentHash hash;

    public void setHash(AbstartConstentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values,key);
    }
}
