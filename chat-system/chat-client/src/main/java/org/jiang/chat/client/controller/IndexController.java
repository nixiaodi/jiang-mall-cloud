package org.jiang.chat.client.controller;

import io.swagger.annotations.ApiOperation;
import org.jiang.chat.client.client.CIMClient;
import org.jiang.chat.client.service.RouteRequest;
import org.jiang.chat.client.vo.request.GoogleProtocolVo;
import org.jiang.chat.client.vo.request.GroupRequestVo;
import org.jiang.chat.client.vo.request.SendMessageRequestVo;
import org.jiang.chat.client.vo.request.StringRequestVo;
import org.jiang.chat.client.vo.response.SendMessageResponseVo;
import org.jiang.chat.common.constant.Constants;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.response.BaseResponse;
import org.jiang.chat.common.response.NullBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    /**
     * 统计service
     */
    @Autowired
    private CounterService counterService;

    @Autowired
    private CIMClient cimClient;

    @Autowired
    private RouteRequest routeRequest;

    /**
     * 向服务端发送字符串
     * @param stringRequestVo
     * @return
     */
    @ApiOperation("客户端发送消息,字符串")
    @PostMapping("/sendStringMessage")
    public BaseResponse<NullBody> sendStringMessage(@RequestBody StringRequestVo stringRequestVo) {
        BaseResponse<NullBody> response = new BaseResponse<>();
        for (int i = 0; i < 100; i++) {
            cimClient.sendStringMsg(stringRequestVo.getMsg());
        }

        // 利用actuator来自增
        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);

        SendMessageResponseVo sendMessageResponseVo = new SendMessageResponseVo();
        sendMessageResponseVo.setMsg("OK");
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());
        return response;
    }

    /**
     * 向服务端发送消息 Google ProtoBuf
     * @param googleProtocolVo
     * @return
     */
    @ApiOperation("向服务端发送消息,Google ProtoBuf")
    @PostMapping("/sendProtoBufMessage")
    public BaseResponse<NullBody> sendProtocolBufMessage(@RequestBody GoogleProtocolVo googleProtocolVo) {
        BaseResponse<NullBody> response = new BaseResponse<>();

        for (int i = 0; i < 100; i++) {
            cimClient.sendGoogleProtocolMsg(googleProtocolVo);
        }

        // 利用 actuator 来自增
        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);;

        SendMessageResponseVo sendMessageResponseVo = new SendMessageResponseVo();
        sendMessageResponseVo.setMsg("OK");
        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());
        return response;
    }

    @ApiOperation("群发消息")
    @PostMapping("/sendGroupMessage")
    public BaseResponse<NullBody> sendGroupMessage(@RequestBody SendMessageRequestVo sendMessageRequestVo) throws Exception {
        BaseResponse<NullBody> response = new BaseResponse<>();

        GroupRequestVo groupRequestVo = new GroupRequestVo(sendMessageRequestVo.getUserId(), sendMessageRequestVo.getMsg());
        routeRequest.sendGroupMessage(groupRequestVo);

        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);

        response.setCode(StatusEnum.SUCCESS.getCode());
        response.setMessage(StatusEnum.SUCCESS.getMessage());
        return  response;
    }

}
