package org.jiang.chat.route.kit;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.route.config.AppConfiguration;
import org.jiang.chat.route.util.SpringBeanFactory;

@Slf4j
public class ServerListListener implements Runnable {

    private Zkit zkit;

    private AppConfiguration appConfiguration;

    public ServerListListener() {
        appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class) ;
        zkit = SpringBeanFactory.getBean(Zkit.class) ;
    }

    @Override
    public void run() {
        // 注册监听服务
        zkit.subscribeEvent(appConfiguration.getZkRoot());
    }
}
