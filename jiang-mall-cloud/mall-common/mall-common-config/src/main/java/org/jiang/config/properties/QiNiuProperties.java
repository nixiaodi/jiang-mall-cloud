package org.jiang.config.properties;

import lombok.Data;

public class QiNiuProperties {

    private QiNiuKeyProperties key = new QiNiuKeyProperties();
    private QiNiuOssProperties oss = new QiNiuOssProperties();

    @Data
    public class QiNiuKeyProperties {
        private String accessKey;
        private String secretKey;
    }

    @Data
    public class QiNiuOssProperties {
        private String privateHost;
        private String publicHost;
        private Long fileMaxSize;
    }
}
