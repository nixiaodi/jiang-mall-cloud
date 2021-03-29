package org.jiang.chat.route.service.impl;

import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.route.constant.Constant;
import org.jiang.chat.route.service.UserInfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserInfoCacheServiceImpl implements UserInfoCacheService {

    /**
     * todo 本地缓存，为了防止内存撑爆，后期可换为 LRU
     */
    private final static Map<Long,CIMUserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public CIMUserInfo loadUserInfoByUserId(Long userId) {
        // 优先从本地获取缓存
        CIMUserInfo cimUserInfo = USER_INFO_MAP.get(userId);
        if (Objects.nonNull(cimUserInfo)) {
            return cimUserInfo;
        }

        // load from redis
        String sendUserName = redisTemplate.opsForValue().get(Constant.ACCOUNT_PREFIX + userId);
        if (sendUserName != null) {
            cimUserInfo = new CIMUserInfo(userId, sendUserName);
            USER_INFO_MAP.put(userId,cimUserInfo);
        }
        return cimUserInfo;
    }

    @Override
    public boolean saveAndCheckLoginStatus(Long userId) throws Exception {
        Long add = redisTemplate.opsForSet().add(Constant.LOGIN_STATUS_PREFIX, userId.toString());
        if (add == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void removeLoginStatus(Long userId) throws Exception {
        redisTemplate.opsForSet().remove(Constant.LOGIN_STATUS_PREFIX,userId.toString());
    }

    @Override
    public Set<CIMUserInfo> onlineUser() {
        Set<CIMUserInfo> set = null;
        Set<String> members = redisTemplate.opsForSet().members(Constant.LOGIN_STATUS_PREFIX);
        for (String member : members) {
            if (set == null) {
                set = new HashSet<>();
            }
            CIMUserInfo cimUserInfo = loadUserInfoByUserId(Long.parseLong(member));
            set.add(cimUserInfo);
        }
        return set;
    }
}
