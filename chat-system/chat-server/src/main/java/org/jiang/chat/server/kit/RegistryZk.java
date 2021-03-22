package org.jiang.chat.server.kit;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.jiang.chat.server.config.AppConfiguration;
import org.jiang.chat.server.util.SpringBeanFactory;

@Slf4j
public class RegistryZk implements Runnable{

    private ZKit zKit;

    private AppConfiguration appConfiguration;

    private String ip;
    private int cimServerPort;
    private int httpPort;

    public RegistryZk(String ip, int cimServerPort, int httpPort) {
        this.ip = ip;
        this.cimServerPort = cimServerPort;
        this.httpPort = httpPort;
        zKit = SpringBeanFactory.getBean(ZKit.class);
        appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class);
    }

    @Override
    public void run() {
        // 创建父级节点
        zKit.createRootNode();

        // 是否要将自己注册到zk
        if (appConfiguration.isZkSwitch()) {
            String path = appConfiguration.getZkRoot() + "/ip-" + ip + ":" + cimServerPort + ":" + httpPort;
            zKit.createNode(path);
            log.info("Registry zookeeper success, msg=[{}]", path);
        }
    }
}
