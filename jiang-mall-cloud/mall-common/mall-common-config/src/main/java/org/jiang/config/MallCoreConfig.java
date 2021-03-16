package org.jiang.config;

import org.jiang.config.properties.MallProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MallProperties.class)
public class MallCoreConfig {
}
