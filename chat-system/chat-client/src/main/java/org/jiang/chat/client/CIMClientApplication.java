package org.jiang.chat.client;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.scan.Scan;
import org.jiang.chat.client.service.impl.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CIMClientApplication implements CommandLineRunner {

    @Autowired
    private ClientInfo clientInfo;

    public static void main(String[] args) {
        SpringApplication.run(CIMClientApplication.class,args);
        log.info("启动 Client 服务成功");
    }

    @Override
    public void run(String... args) {
        Scan scan = new Scan();
        Thread thread = new Thread(scan);
        thread.setName("scan-thread");
        thread.start();
        clientInfo.saveStartDate();
    }
}
