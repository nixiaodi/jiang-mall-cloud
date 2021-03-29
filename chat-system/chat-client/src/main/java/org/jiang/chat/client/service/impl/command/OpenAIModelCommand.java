package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.MsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpenAIModelCommand implements InnerCommand {

    @Autowired
    private MsgHandler msgHandler;

    @Override
    public void process(String message) {
        msgHandler.openAIModel();
        System.out.println("\033[31;4m" + "Hello,我是估值两亿的 AI 机器人!" + "\033[0m");
    }
}
