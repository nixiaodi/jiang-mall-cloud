package org.jiang.chat.server.api.vo;

import org.jiang.chat.server.api.vo.request.SendMessageRequestVo;

import java.util.Objects;

/**
 *
 * @author 蒋小胖
 */
public interface ServerApi {

    /**
     * push message to client
     * @param requestVo
     * @return
     * @throws Exception
     */
    Object sendMessage(SendMessageRequestVo requestVo) throws Exception;
}
