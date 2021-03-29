package org.jiang.chat.route.service.impl;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jiang.chat.common.core.proxy.ProxyManager;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.model.CIMUserInfo;
import org.jiang.chat.common.utils.RouteInfoParseUtil;
import org.jiang.chat.route.api.vo.request.ChatRequestVo;
import org.jiang.chat.route.api.vo.request.LoginRequestVo;
import org.jiang.chat.route.api.vo.response.CIMServerResponseVo;
import org.jiang.chat.route.api.vo.response.RegisterInfoResponseVo;
import org.jiang.chat.route.constant.Constant;
import org.jiang.chat.route.service.AccountService;
import org.jiang.chat.route.service.UserInfoCacheService;
import org.jiang.chat.server.api.vo.ServerApi;
import org.jiang.chat.server.api.vo.request.SendMessageRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

@Service
@Slf4j
public class AccountServiceRedisImpl implements AccountService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public RegisterInfoResponseVo register(RegisterInfoResponseVo info) throws Exception {
        String key = Constant.ACCOUNT_PREFIX + info.getUserId();

        String name = redisTemplate.opsForValue().get(info.getUserName());
        if (Objects.isNull(name)) {
            // 为了方便查询,冗余一份
            redisTemplate.opsForValue().set(key,info.getUserName());
            redisTemplate.opsForValue().set(info.getUserName(),key);
        } else {
            long userId = Long.parseLong(name.split(":")[1]);
            info.setUserId(userId);
            info.setUserName(info.getUserName());
        }
        return info;
    }

    @Override
    public StatusEnum login(LoginRequestVo loginRequestVo) throws Exception {
        // 再去redis里查询
        String key = Constant.ACCOUNT_PREFIX + loginRequestVo.getUserId();
        String userName = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(userName)) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        if (!userName.equals(loginRequestVo.getUserName())) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        // 登录成功,保存登录状态
        boolean status = userInfoCacheService.saveAndCheckLoginStatus(loginRequestVo.getUserId());
        if (status == false) {
            // 重复登录
            return StatusEnum.REPEAT_LOGIN;
        }

        return StatusEnum.SUCCESS;
    }

    @Override
    public void saveRouteInfo(LoginRequestVo loginRequestVo, String msg) throws Exception {
        String key = Constant.ROUTE_PREFIX + loginRequestVo.getUserId();
        redisTemplate.opsForValue().set(key,msg);
    }

    @Override
    public Map<Long, CIMServerResponseVo> loadRouteRelated() {
        Map<Long,CIMServerResponseVo> routes = new HashMap<>();

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match(Constant.ROUTE_PREFIX + "*")
                .build();

        Cursor<byte[]> scan = connection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, CharsetUtil.UTF_8);
            log.info("key={}", key);
            parseServerInfo(routes,key);
        }
        try {
            scan.close();
        } catch (IOException e) {
            log.error("IOException", e);
        }
        return routes;
    }

    private void parseServerInfo(Map<Long,CIMServerResponseVo> routes,String key) {
        Long userId = Long.parseLong(key.split(":")[1]);
        String value = redisTemplate.opsForValue().get(key);
        CIMServerResponseVo cimServerResponseVo = new CIMServerResponseVo(RouteInfoParseUtil.parse(value));
        routes.put(userId, cimServerResponseVo);
    }

    @Override
    public CIMServerResponseVo loadRouteRelatedByUserId(Long userId) {
        String value = redisTemplate.opsForValue().get(Constant.ROUTE_PREFIX + userId);

        if (value == null) {
            throw new ChatException(StatusEnum.OFF_LINE);
        }

        CIMServerResponseVo cimServerResponseVo = new CIMServerResponseVo(RouteInfoParseUtil.parse(value));
        return cimServerResponseVo;
    }

    @Override
    public void pushMsg(CIMServerResponseVo cimServerResponseVo, Long sendUserId, ChatRequestVo groupRequestVo) throws Exception {
        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(sendUserId);

        String url = "http://" + cimServerResponseVo.getIp() + ":" + cimServerResponseVo.getHttpPort();

        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMessageRequestVo sendMessageRequestVo = new SendMessageRequestVo(cimUserInfo.getUserName() + ":" + groupRequestVo.getMessage(), groupRequestVo.getUserId());
        Response response = null;
        try {
            response = (Response) serverApi.sendMessage(sendMessageRequestVo);
        } catch (Exception e) {
            log.error("Exception", e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public void offline(Long userId) throws Exception {
        // TODO: 2019-01-21 改为一个原子命令，以防数据一致性

        // 删除路由
        redisTemplate.delete(Constant.ROUTE_PREFIX + userId);

        // 删除登录状态
        userInfoCacheService.removeLoginStatus(userId);
    }
}
