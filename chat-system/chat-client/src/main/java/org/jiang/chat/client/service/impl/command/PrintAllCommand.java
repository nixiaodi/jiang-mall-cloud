package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.common.enums.SystemCommandEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PrintAllCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        Map<String, String> allStatusCode = SystemCommandEnum.getAllStatusCode();
        echoService.echo("====================================");
        for (Map.Entry<String, String> entry : allStatusCode.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            echoService.echo(key + "----->" + value);
        }
        echoService.echo("====================================");
    }
}
