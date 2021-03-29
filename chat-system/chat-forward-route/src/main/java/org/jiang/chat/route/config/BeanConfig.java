package org.jiang.chat.route.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.jiang.chat.common.route.algorithm.RouteHandler;
import org.jiang.chat.common.route.algorithm.consistenthash.AbstractConsistentHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


@Configuration
@Slf4j
public class BeanConfig {

    private static final String CONSISTENT_HASH = "ConsistentHash";

    private static final String METHOD_SET_HASH = "setHash";

    @Autowired
    private AppConfiguration appConfiguration;

    @Bean
    public ZkClient buildZkClient() {
        return new ZkClient(appConfiguration.getZkAddr(),appConfiguration.getZkConnectTimeout());
    }

    @Bean
    public LoadingCache<String,String> buildCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return null;
                    }
                });
    }

    @Bean
    public RedisTemplate<java.lang.String, java.lang.String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return builder.build();
    }

    @Bean
    public RouteHandler buildRouteHandler() throws Exception {
        String routeWay = appConfiguration.getRouteWay();
        RouteHandler routeHandler = (RouteHandler) Class.forName(routeWay).newInstance();
        log.info("Current route algorithm is [{}]", routeHandler.getClass().getSimpleName());
        if (routeWay.contains(CONSISTENT_HASH)) {
            // 一致性hash算法
            Method method = Class.forName(routeWay).getMethod(METHOD_SET_HASH, AbstractConsistentHash.class);
            AbstractConsistentHash consistentHash = (AbstractConsistentHash) Class.forName(appConfiguration.getConsistentHashWay()).newInstance();
            method.invoke(routeHandler,consistentHash);
        }
        return routeHandler;
    }

}
