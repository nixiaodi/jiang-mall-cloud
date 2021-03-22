package org.jiang.chat.server;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.server.config.AppConfiguration;
import org.jiang.chat.server.kit.RegistryZk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@SpringBootApplication
@Slf4j
public class CIMServerApplication implements CommandLineRunner {

    @Autowired
    private AppConfiguration appConfiguration;

    @Value("${server.port}")
    private int httpPort;

    public static void main(String[] args) {
        SpringApplication.run(CIMServerApplication.class,args);
        log.info("Start cim server success!!!");
    }

    @Override
    public void run(String... args) throws Exception {
        // 获得本机IP
        String addr = InetAddress.getLocalHost().getHostAddress();
        Thread thread = new Thread(new RegistryZk(addr, appConfiguration.getCimServerPort(), httpPort));
        thread.setName("registry-zk");
        thread.start();
    }
}
