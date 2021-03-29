package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.MsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CloseAIModelCommand implements InnerCommand {

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        msgHandler.closeAIModel();
        echoService.echo("\033[31;4m" + "｡ﾟ(ﾟ´ω`ﾟ)ﾟ｡  AI 下线了！" + "\033[0m");
    }
}
