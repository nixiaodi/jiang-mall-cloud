package org.jiang.core.registry.zookeeper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.jiang.config.properties.ZookeeperProperties;
import org.jiang.core.registry.base.CoordinatorRegistryCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ZookeeperRegistryCenter implements CoordinatorRegistryCenter {
    @Getter(AccessLevel.PROTECTED)
    private ZookeeperProperties zkConfig;

    private final Map<String, TreeCache> caches = new HashMap<>();

    @Getter
    private CuratorFramework client;

    @Getter
    private DistributedAtomicInteger distributedAtomicInteger;

    public ZookeeperRegistryCenter(final ZookeeperProperties zkConfig) {
        this.zkConfig = zkConfig;
    }

    @Override
    public String getDirectly(String key) {
        return null;
    }

    @Override
    public List<String> getChildrenKeys(String key) {
        return null;
    }

    @Override
    public int getChildrenNumber(String key) {
        return 0;
    }

    @Override
    public void persistEphemeral(String key, String value) {

    }

    @Override
    public String persistSequential(String key, String value) {
        return null;
    }

    @Override
    public void persistEphemeralSequential(String key) {

    }

    @Override
    public void addCacheData(String cachePath) {

    }

    @Override
    public void evictCacheData(String cachePath) {

    }

    @Override
    public Object getRawCache(String cachePath) {
        return null;
    }

    @Override
    public void registerMq(String app, String host, String producerGroup, String consumerGroup, String nameSrvAddr) {

    }
}
