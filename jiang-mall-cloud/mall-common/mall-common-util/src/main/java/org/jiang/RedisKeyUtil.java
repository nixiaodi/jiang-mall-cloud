package org.jiang;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import rx.BackpressureOverflow;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyUtil {

    /**
     * RESET_PWD_TOKEN_KEY constant
     */
    private static final String RESET_PWD_TOKEN_KEY = "jiang:resetPwd";
    private static final String ACTIVE_USER = "jiang:activeUser";
    private static final String SEND_SMS_COUNT = "jiang:sms:count";
    private static final String SEND_EMAIL_CODE = "jiang:email:code";
    private static final String ACCESS_TOKEN = "jiang:token:accessToken";
    private static final String UPLOAD_FILE_SIZE = "jiang:file:upload_file_size";
    private static final Integer REF_NO_MAX_LENGTH = 100;


    /**
     * get reset pwd token key
     */
    public static String getResetPwdTokenKey(String resetPwdKey) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(resetPwdKey), "参数不能为空");
        return RESET_PWD_TOKEN_KEY + ":" + resetPwdKey;
    }

    /**
     * Gets send email code key.
     */
    public static String getSendEmailCodeKey(String loginName, String email) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(loginName), "Email不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(email), "用户名不能为空");
        return SEND_EMAIL_CODE + ":" + loginName + email;

    }

    /**
     * Gets active user key.
     */
    public static String getActiveUserKey(String activeToken) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(activeToken), "参数不能为空");
        return ACTIVE_USER + ":" + activeToken;

    }

    /**
     * Gets send sms count key.
     * @param ipAddr the ip addr
     * @param type   mobile;ip;total
     */
    public static String getSendSmsCountKey(String ipAddr, String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "请不要篡改IP地址");
        return SEND_SMS_COUNT + ":" + type + ":" + ipAddr;

    }

    /**
     * Gets send sms rate key.
     * @param ipAddr the ip addr
     */
    public static String getSendSmsRateKey(String ipAddr) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "请不要篡改IP地址");
        return SEND_SMS_COUNT + ":" + ipAddr;

    }

    public static String getAccessTokenKey(String token) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(token), "非法请求token参数不存在");
        return ACCESS_TOKEN + ":" + token;
    }

    public static String createMqKey(String topic,String tag,String refNo,String body) {
        String temp = refNo;
        Preconditions.checkArgument(StringUtils.isNotEmpty(topic), "topic is null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(tag), "tag is null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(refNo), "refNo is null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(body), "body is null");

        if (refNo.length() > REF_NO_MAX_LENGTH) {
            temp = StringUtils.substring(refNo,0,REF_NO_MAX_LENGTH);
        }
        return topic + "_" + tag + "_" + temp + "_" + body.hashCode();
    }

    public static String getFileSizeKey() {
        return UPLOAD_FILE_SIZE;
    }
}
