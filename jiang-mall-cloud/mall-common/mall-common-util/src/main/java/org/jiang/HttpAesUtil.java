package org.jiang;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jiang.exception.HttpAesException;
import org.omg.CORBA.PUBLIC_MEMBER;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpAesUtil {

    private static final String CHAR_SET = "UTF-8";

    /**
     * 加密
     * @param contentParam 需要加密的内容
     * @param keyParam     加密密码
     * @param md5Key       是否对key进行md5加密
     * @param ivParam      加密向量
     * @return 加密后的字节数据 string
     */
    public static String encrypt(String contentParam,String keyParam,boolean md5Key,String ivParam) {
        try {
            byte[] content = contentParam.getBytes(CHAR_SET);
            byte[] key = keyParam.getBytes(CHAR_SET);
            byte[] iv = ivParam.getBytes(CHAR_SET);

            if (md5Key) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                key = md5.digest(key);
            }
            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            //使用CBC模式, 需要一个向量iv, 可增加加密算法的强度
            IvParameterSpec ivPs = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE,sKeySpec,ivPs);
            byte[] result = cipher.doFinal(content);
            return new BASE64Encoder().encode(result);
        } catch (Exception e) {
            log.error("加密失败",e);
            throw new HttpAesException("加密失败");
        }
    }

    /**
     * 解密
     *
     * @param contentParam 需要加密的内容
     * @param keyParam     加密密码
     * @param md5Key       是否对key进行md5加密
     * @param ivParam      加密向量
     * @return string      解密结果
     */
    public static String decrypt(String contentParam,String keyParam,boolean md5Key,String ivParam) {
        return null;
    }
}
