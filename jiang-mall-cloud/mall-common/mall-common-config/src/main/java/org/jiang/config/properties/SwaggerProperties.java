package org.jiang.config.properties;

import lombok.Data;

@Data
public class SwaggerProperties {
    private String title;

    private String description;

    private String version = "1.0";

    private String license = "Apache License 2.0";

    private String licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0";

    private String contactName = "蒋多多";

    private String contactUrl = "http://www.jiangxiaopang.cn";

    private String contactEmail = "19939600096@163.com";
}
