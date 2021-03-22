package org.jiang.chat.server.endpoint;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.jiang.chat.server.util.SessionSocketHolder;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

import java.util.Map;

/**
 * 自定义监控端点
 */
public class CustomerEndpoint extends AbstractEndpoint<Map<Long, NioSocketChannel>> {

    /**
     * 监控端点的访问地址
     * @param id
     */
    public CustomerEndpoint(String id) {
        //false 表示不是敏感端点
        super(id,false);
    }

    @Override
    public Map<Long, NioSocketChannel> invoke() {
        return SessionSocketHolder.getRelationShip();
    }
}
