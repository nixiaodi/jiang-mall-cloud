package org.jiang.chat.client.service.impl.command;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.EchoService;
import org.jiang.chat.client.service.InnerCommand;
import org.jiang.chat.client.service.RouteRequest;
import org.jiang.chat.client.vo.response.OnlineUserResponseVo;
import org.jiang.chat.common.data.construct.TrieTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PrefixSearchCommand implements InnerCommand {

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String message) {
      try {
          List<OnlineUserResponseVo.DataBodyBean> onlineUsers = routeRequest.onlineUsers();
          TrieTree trieTree = new TrieTree();
          for (OnlineUserResponseVo.DataBodyBean onlineUser : onlineUsers) {
              trieTree.insert(onlineUser.getUserName());
          }

          String[] split = message.split(" ");
          String key = split[1];
          List<String> list = trieTree.prefixSearch(key);

          for (String res : list) {
              res = res.replace(key, "\033[31;4m" + key + "\033[0m");
              echoService.echo(res);
          }
      } catch (Exception e) {
          log.error("Exception", e);
      }
    }
}
