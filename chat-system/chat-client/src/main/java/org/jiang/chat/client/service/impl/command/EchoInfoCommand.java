package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.MsgHandler;
import org.jiang.chat.client.service.impl.ClientInfo;
import org.jiang.chat.common.data.construct.RingBufferWheel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EchoInfoCommand implements InnerCommand {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        echoService.echo("client info={}", clientInfo.get().getUserName());
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
