package org.jiang.chat.client.scan;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.config.AppConfiguration;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.MsgHandler;
import org.jiang.chat.client.service.MsgLogger;
import org.jiang.chat.client.util.SpringBeanFactory;

import java.util.Scanner;

@Slf4j
public class Scan implements Runnable{

    /**
     * 系统参数
     */
    private AppConfiguration appConfiguration;

    private MsgHandler msgHandler;

    private MsgLogger msgLogger;

    private EchoService echoService;

    public Scan() {
        this.appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class);
        this.msgHandler = SpringBeanFactory.getBean(MsgHandler.class) ;
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class) ;
        this.echoService = SpringBeanFactory.getBean(EchoService.class);
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String msg = sc.nextLine();

            // 检查消息
            if (msgHandler.checkMessage(msg)) {
                continue;
            }

            // 系统内置命令
            if (msgHandler.innerCommand(msg)) {
                continue;
            }

            // 真正发送消息
            msgHandler.sendMessage(msg);

            // 写入聊天记录
            msgLogger.log(msg);

            echoService.echo(EmojiParser.parseToUnicode(msg));
        }
    }
}
