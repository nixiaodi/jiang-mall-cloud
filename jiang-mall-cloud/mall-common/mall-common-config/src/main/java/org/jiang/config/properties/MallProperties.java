package org.jiang.config.properties;

import io.swagger.models.Swagger;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.Data;
import org.jiang.constant.GlobalConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class MallProperties {
    private ReliableMessageProperties message = new ReliableMessageProperties();
    private AliyunProperties aliyun = new AliyunProperties();
    private AsyncTaskProperties task = new AsyncTaskProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private QiNiuProperties qiNiu = new QiNiuProperties();
    private GaodeMapProperties gaodeMap = new GaodeMapProperties();
    private JobProperties job = new JobProperties();
    private ZookeeperProperties zk = new ZookeeperProperties();
}
