package org.jiang.chat.route.service;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.model.RouteInfo;
import org.jiang.chat.route.cache.ServerCache;
import org.jiang.chat.route.kit.NetAddressIsReachable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonBizService {

    @Autowired
    private ServerCache serverCache;

    /**
     * check ip and port
     * @param routeInfo
     */
    public void checkServerAvailable(RouteInfo routeInfo) {
        boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getCimServerPort(), 1000);
        if (!reachable) {
            log.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getCimServerPort());

            // rebuild cache
            serverCache.rebuildCacheList();

            throw new ChatException(StatusEnum.SERVER_NOT_AVAILABLE);
        }
    }
}
