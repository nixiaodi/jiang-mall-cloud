package org.jiang.chat.client.service.impl.command;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;

import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmojiCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
        if (message.split(" ").length <= 1) {
            echoService.echo("incorrect command, :emoji [option]");
            return;
        }
        String value = message.split(" ")[1];
        if (value != null) {
            Integer index = Integer.parseInt(value);
            List<Emoji> all = (List<Emoji>) EmojiManager.getAll();
            all = all.subList(5 * index,5 * index + 5);

            for (Emoji emoji : all) {
                echoService.echo(EmojiParser.parseToAliases(emoji.getUnicode()) + "--->" + emoji.getUnicode());
            }
        }
    }
}
