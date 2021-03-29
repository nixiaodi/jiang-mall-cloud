package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.RouteRequest;
import org.jiang.chat.client.vo.response.OnlineUserResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PrintOnlineUserCommand implements InnerCommand {

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        try {
            List<OnlineUserResponseVo.DataBodyBean> onlineUsers = routeRequest.onlineUsers();
            echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for (OnlineUserResponseVo.DataBodyBean onlineUser : onlineUsers) {
                echoService.echo("userId={}=====userName={}",onlineUser.getUserId(),onlineUser.getUserName());
            }
            echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
}
