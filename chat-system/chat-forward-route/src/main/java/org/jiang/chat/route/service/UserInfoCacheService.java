package org.jiang.chat.route.service;

import org.jiang.chat.common.model.CIMUserInfo;

import java.util.Set;

public interface UserInfoCacheService {

    /**
     * 通过 userId 获取用户信息
     * @param userId
     * @return
     */
    CIMUserInfo loadUserInfoByUserId(Long userId);

    /**
     * 检查和保存用户登录情况
     * @param userId
     * @return
     * @throws Exception
     */
    boolean saveAndCheckLoginStatus(Long userId) throws Exception;

    /**
     * 清除用户登录状态
     * @param userId
     * @throws Exception
     */
    void removeLoginStatus(Long userId) throws Exception;

    /**
     * query all online user
     * @return
     */
    Set<CIMUserInfo> onlineUser();
}
