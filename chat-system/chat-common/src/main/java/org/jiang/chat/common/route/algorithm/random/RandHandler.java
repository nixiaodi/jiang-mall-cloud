package org.jiang.chat.common.route.algorithm.random;

import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.route.algorithm.RouteHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 通过随机进行路由选择
 * @author 蒋小胖
 */
public class RandHandler implements RouteHandler {
    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new ChatException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        int offset = ThreadLocalRandom.current().nextInt(size);

        return values.get(offset);
    }
}
