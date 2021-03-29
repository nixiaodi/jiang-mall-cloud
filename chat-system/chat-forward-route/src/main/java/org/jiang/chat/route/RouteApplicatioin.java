package org.jiang.chat.route;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.route.kit.ServerListListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RouteApplicatioin implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RouteApplicatioin.class,args);
        log.info("Start cim route success!!!");
    }

    @Override
    public void run(String... args) throws Exception {

        // 监听服务
        Thread thread = new Thread(new ServerListListener());
        thread.setName("zk-listener");
        thread.start();
    }
}
