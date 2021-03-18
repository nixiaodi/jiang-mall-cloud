package org.jiang.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.jiang.config.properties.ZookeeperProperties;
import org.jiang.constant.GlobalConstant;
import org.jiang.core.registry.base.CoordinatorRegistryCenter;
import org.jiang.core.registry.base.ReliableMessageRegisterDto;
import org.jiang.core.registry.exception.RegistryExceptionHandler;
import org.springframework.util.CollectionUtils;

import javax.security.auth.login.FailedLoginException;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper的注册中心
 */
@Slf4j
public class ZookeeperRegistryCenter implements CoordinatorRegistryCenter {
    @Getter(AccessLevel.PROTECTED)
    private ZookeeperProperties zkConfig;

    /**
     * key 和 TreeCache 之间的映射关系
     */
    private final Map<String, TreeCache> caches = new HashMap<>();

    @Getter
    private CuratorFramework client;

    @Getter
    private DistributedAtomicInteger distributedAtomicInteger;

    /**
     * Instantiates a new Zookeeper registry center.
     * @param zkConfig
     */
    public ZookeeperRegistryCenter(final ZookeeperProperties zkConfig) {
        this.zkConfig = zkConfig;
    }


    @Override
    public void init() {
        log.debug("Elastic job: zookeeper registry center init, server lists is: {}",zkConfig.getZkAddressList());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getZkAddressList())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(),zkConfig.getMaxRetries(),zkConfig.getMaxSleepTimeMilliseconds()));
        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest",zkConfig.getDigest().getBytes(Charsets.UTF_8))
                    .aclProvider(new ACLProvider() {
                        @Override
                        public List<ACL> getDefaultAcl() {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }

                        @Override
                        public List<ACL> getAclForPath(String path) {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                    });
        }
        client = builder.build();
        client.start();
        try {
            if (client.blockUntilConnected(zkConfig.getMaxSleepTimeMilliseconds() * zkConfig.getMaxRetries(), TimeUnit.MILLISECONDS)) {
                client.close();
                throw new KeeperException.OperationTimeoutException();
            }
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }

    }

    /**
     * close
     */
    @Override
    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /**
     * 等待500MS，cache先关闭再关闭客户端，否则会抛异常
     * 因为异步处理，导致可能会先关闭client而cache还未关闭结束
     * 等待Curator新版本解决这个bug
     */
    public void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 先判断TreeCache中是否存在，如果没有再直接取
     * @param key 键
     */
    @Override
    public String get(String key) {
        TreeCache cache = findTreeCache(key);
        if (Objects.isNull(cache)) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (!Objects.isNull(resultInCache)) {
            return Objects.isNull(resultInCache.getData()) ? null : new String(resultInCache.getData());
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * decide key exist
     */
    @Override
    public boolean isExisted(String key) {
        try {
            return !Objects.isNull(client.checkExists().forPath(key));
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public void persist(String key) {
        persist(key,null);
    }

    @Override
    public void persist(String key, String value) {
        try {
            if (!isExisted(key)) {
                if (null == value) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key,value.getBytes("UTF-8"));
                }
            } else {
                if (!Objects.isNull(value)) {
                    update(key,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * transaction update
     */
    @Override
    public void update(String key, String value) {
        try {
            client.inTransaction().check().forPath(key)
                    .and().setData().forPath(key,value.getBytes(Charsets.UTF_8))
                    .and().commit();
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
    }

    /**
     * get registry center time
     */
    @Override
    public long getRegistryCenterTime(String key) {
        long result = 0L;
        try {
            persist(key,"");
            result = client.checkExists().forPath(key).getMtime();
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
        Preconditions.checkState(0L != result,"Cannot get registry center time.");
        return result;
    }

    @Override
    public Object getRawClient() {
        return client;
    }

    /**
     * 自增
     */
    @Override
    public void increment(String path, RetryNTimes retryNTimes) {
        try {
            distributedAtomicInteger = new DistributedAtomicInteger(client,path,retryNTimes);
            distributedAtomicInteger.increment();
        } catch (Exception e) {
            log.error("increment={}",e.getMessage(),e);
        }
    }

    /**
     * 获取原子当前value
     */
    @Override
    public AtomicValue<Integer> getAtomicValue(String path, RetryNTimes retryNTimes) {
        try {
            distributedAtomicInteger = new DistributedAtomicInteger(client,path,retryNTimes);
            return distributedAtomicInteger.get();
        } catch (final Exception e) {
            log.error("getAtomicValue={}",e.getMessage(),e);
        }
        return null;
    }

    /**
     * get directly
     */
    @Override
    public String getDirectly(String key) {
        try {
            return new String(client.getData().forPath(key),Charsets.UTF_8);
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
            return null;
        }
    }

    /**
     * get children keys
     */
    @Override
    public List<String> getChildrenKeys(String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            result.sort(Comparator.reverseOrder());
            return result;
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }

    /**
     * get children number
     */
    @Override
    public int getChildrenNumber(String key) {
        Stat stat = null;
        try {
            stat = client.checkExists().forPath(key);
        } catch (Exception e) {
            RegistryExceptionHandler.handleException(e);
        }
        return Objects.isNull(stat) ? 0 : stat.getNumChildren();
    }

    /**
     * temporary persist
     */
    @Override
    public void persistEphemeral(String key, String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
    }

    /**
     * sequential persist
     */
    @Override
    public String persistSequential(String key, String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
        return null;
    }

    /**
     * sequential temporary persist
     * @param key 键
     */
    @Override
    public void persistEphemeralSequential(String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
    }

    /**
     * 为当前路径添加TreeCache并加入到path和TreeCache映射中
     */
    @Override
    public void addCacheData(String cachePath) {
        TreeCache treeCache = new TreeCache(client, cachePath);
        try {
            treeCache.start();
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegistryExceptionHandler.handleException(e);
        }
        caches.put(cachePath + "/",treeCache);
    }

    /**
     * delete TreeCache
     * @param cachePath 需释放缓存的路径
     */
    @Override
    public void evictCacheData(String cachePath) {
        TreeCache treeCache = caches.remove(cachePath + "/");
        if (!Objects.isNull(treeCache)) {
            treeCache.close();
        }
    }

    /**
     * get raw cache
     * @param cachePath 缓存的节点路径
     * @return
     */
    @Override
    public Object getRawCache(String cachePath) {
        return caches.get(cachePath + "/");
    }

    /**
     * registry MQ
     * @param app           the app
     * @param host          the host
     * @param producerGroup the producer group
     * @param consumerGroup the consumer group
     * @param nameSrvAddr   the namesrv addr
     */
    @Override
    public void registerMq(String app, String host, String producerGroup, String consumerGroup, String nameSrvAddr) {
        // 注册生产者
        final String producerRootPath = GlobalConstant.ZK_REGISTRY_PRODUCER_ROOT_PATH;
        final String consumerRootPath = GlobalConstant.ZK_REGISTRY_CONSUMER_ROOT_PATH;

        ReliableMessageRegisterDto dto;
        if (StringUtils.isNotEmpty(producerGroup)) {
            dto = new ReliableMessageRegisterDto().setProducerGroup(producerGroup);
            String producerJson = JSON.toJSONString(dto);
            this.persist(producerRootPath,producerJson);
            this.persistEphemeral(producerRootPath + GlobalConstant.Symbol.SLASH + host, DateUtil.now());
        }
        // 注册消费者
        if (StringUtils.isNotEmpty(consumerGroup)) {
            dto = new ReliableMessageRegisterDto().setConsumerGroup(consumerGroup);
            String producerJson = JSON.toJSONString(dto);
            this.persist(consumerRootPath,producerJson);
            this.persistEphemeral(consumerRootPath + GlobalConstant.Symbol.SLASH + host,DateUtil.now());
        }
    }
}
