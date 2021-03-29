package org.jiang.chat.client.service.impl.command;

import com.vdurmont.emoji.EmojiParser;
import org.jiang.chat.client.handler.MsgHandleCaller;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.MsgHandler;
import org.jiang.chat.common.data.construct.RingBufferWheel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelayMsgCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private RingBufferWheel ringBufferWheel;

    @Override
    public void process(String message) {
        if (message.split(" ").length <= 2) {
            echoService.echo("incorrect command, :delay [msg] [delayTime]");
            return;
        }

        String msg = message.split(" ")[1];
        Integer delayTime = Integer.valueOf(msg.split(" ")[2]);
        RingBufferWheel.Task task = new DelayMsgJob(msg);
        task.setKey(delayTime);
        ringBufferWheel.addTask(task);
        echoService.echo(EmojiParser.parseToUnicode(message));
    }


    private class DelayMsgJob extends RingBufferWheel.Task {

        private String msg;

        public DelayMsgJob(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            msgHandler.sendMessage(msg);
        }
    }
}
