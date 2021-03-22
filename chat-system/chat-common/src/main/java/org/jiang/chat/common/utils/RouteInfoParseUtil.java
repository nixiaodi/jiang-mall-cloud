package org.jiang.chat.common.utils;

import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.model.RouteInfo;

/**
 * @author 蒋小胖
 */
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info) {
        try {
            String[] serverInfo = info.split(":");
            RouteInfo routeInfo = new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]), Integer.parseInt(serverInfo[2]));
            return routeInfo;
        } catch (Exception e) {
            throw  new ChatException(StatusEnum.VALIDATION_FAIL);
        }
    }
}
