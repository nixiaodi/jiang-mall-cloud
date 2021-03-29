package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.MsgLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
@Slf4j
public class QueryHistoryCommand implements InnerCommand {

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        String[] split = message.split(" ");
        if (split.length < 2) {
            return;
        }
        String res = msgLogger.query(split[1]);
        echoService.echo(res);
    }
}
