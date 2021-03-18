package org.jiang.core.registry;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jiang.config.properties.AliyunProperties;
import org.jiang.config.properties.MallProperties;
import org.jiang.config.properties.ZookeeperProperties;
import org.jiang.core.generator.IncrementIdGenerator;
import org.jiang.core.registry.base.CoordinatorRegistryCenter;
import org.jiang.core.registry.base.RegisterDto;
import org.jiang.core.registry.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resources;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心工厂
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistryCenterFactory {

    private static final ConcurrentHashMap<HashCode, CoordinatorRegistryCenter> REG_CENTER_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 创建注册中心
     * @param zookeeperProperties
     * @return 注册中心对象
     */
    public static CoordinatorRegistryCenter createCoordinatorRegistryCenter(ZookeeperProperties zookeeperProperties) {
        Hasher hasher = Hashing.md5().newHasher().putString(zookeeperProperties.getZkAddressList(), Charsets.UTF_8);
        HashCode hashCode = hasher.hash();
        CoordinatorRegistryCenter registryCenter = REG_CENTER_REGISTRY.get(hashCode);
        if (Objects.nonNull(registryCenter)) {
            return registryCenter;
        }
        registryCenter = new ZookeeperRegistryCenter(zookeeperProperties);
        registryCenter.init();
        REG_CENTER_REGISTRY.put(hashCode, registryCenter);
        return registryCenter;
    }

    /**
     * startup
     * @param mallProperties the mall properties
     * @param host                the host
     * @param app                 the app
     */
    public static void startup(MallProperties mallProperties,String host,String app) {
        CoordinatorRegistryCenter registryCenter = createCoordinatorRegistryCenter(mallProperties.getZk());
        RegisterDto dto = new RegisterDto(app, host, registryCenter);
        Long serviceId = new IncrementIdGenerator(dto).nextId();
        IncrementIdGenerator.setServiceId(serviceId);
        registryMq(mallProperties,host,app);
    }

    /**
     * registry MQ
     */
    private static void registryMq(MallProperties mallProperties,String host,String app) {
        CoordinatorRegistryCenter registryCenter = createCoordinatorRegistryCenter(mallProperties.getZk());
        AliyunProperties.RocketMqProperties rocketMq = mallProperties.getAliyun().getRocketMq();
        String consumerGroup = rocketMq.isReliableMessageConsumer() ? rocketMq.getConsumerGroup() : null;
        String nameSrvAddr = rocketMq.getNameSrvAddr();
        String producerGroup = rocketMq.isReliableMessageProducer() ? rocketMq.getProducerGroup() : null;
        registryCenter.registerMq(app,host,producerGroup,consumerGroup,nameSrvAddr);
    }
}
