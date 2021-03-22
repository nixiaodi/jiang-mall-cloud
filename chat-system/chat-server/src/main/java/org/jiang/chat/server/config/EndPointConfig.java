package org.jiang.chat.server.config;

import org.jiang.chat.server.endpoint.CustomerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndPointConfig {

    @Value("${monitor.channel.map.key}")
    private String channelMap;

    @Bean
    public CustomerEndpoint buildEndPoint() {
        CustomerEndpoint customerEndpoint = new CustomerEndpoint(channelMap);
        return customerEndpoint;
    }
}
