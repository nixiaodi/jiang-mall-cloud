package org.jiang.chat.common.route.algorithm.loop;

import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.route.algorithm.RouteHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 通过轮询进行路由选择
 * @author 蒋小胖
 */
public class LoopHandler implements RouteHandler {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values, String key) {
        if (values.size() == 0) {
            throw new ChatException(StatusEnum.SERVER_NOT_AVAILABLE);
        }

        Long position = index.incrementAndGet() % values.size();
        if (position < 0) {
            position = 0L;
        }
        return values.get(position.intValue());
    }
}
