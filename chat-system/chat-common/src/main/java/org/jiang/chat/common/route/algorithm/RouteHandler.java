package org.jiang.chat.common.route.algorithm;

import java.util.List;

/**
 * @author 蒋小胖
 */
public interface RouteHandler {

    /**
     * 在一批服务器中进行路由
     */
    String routeServer(List<String> values,String key);
}
