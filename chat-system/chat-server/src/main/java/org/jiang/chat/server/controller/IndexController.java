package org.jiang.chat.server.controller;

import io.swagger.annotations.ApiOperation;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.response.BaseResponse;
import org.jiang.chat.route.api.vo.response.SendMessageResponseVo;
import org.jiang.chat.server.api.vo.ServerApi;
import org.jiang.chat.server.api.vo.request.SendMessageRequestVo;
import org.jiang.chat.server.server.CIMServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController implements ServerApi {

    @Autowired
    private CIMServer cimServer;

    /**
     * 统计service
     */
    @Autowired
    private CounterService counterService;

    /**
     * @param requestVo
     * @return
     * @throws Exception
     */
    @Override
    @ApiOperation("push message to client")
    @PostMapping("/sendMessage")
    public BaseResponse<SendMessageResponseVo> sendMessage(@RequestBody SendMessageRequestVo requestVo) throws Exception {
        BaseResponse<SendMessageResponseVo> response  = new BaseResponse<>();
        cimServer.sendMessage(requestVo);

        counterService.increment(Constants.COUNTER_SERVER_PUSH_COUNT);

        SendMessageResponseVo responseVo = new SendMessageResponseVo();
        responseVo.setMessage("ok");
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setDataBody(responseVo);
        return response;
    }
}
