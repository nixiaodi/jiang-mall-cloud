package org.jiang.chat.common.route.algorithm.consistenthash;

import com.google.common.base.Charsets;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 一致性hash算法抽象类
 * @author 蒋小胖
 */
public abstract class AbstractConsistentHash {

    protected static final String MD5 = "MD5";

    /**
     * 新增节点
     */
    protected abstract void add(long key,String value);

    /**
     * 排序节点，数据结构自身支持排序可以不用重写
     */
    protected void sort() {

    }

    /**
     * 根据当前的key通过一致性hash算法的规则取出一个节点
     */
    protected abstract String getFirstNodeValue(String value);

    /**
     * 传入节点列表以及客户端信息获取一个服务节点
     */
    public String process(List<String> values,String key) {
        for (String value : values) {
            add(hash(value),value);
        }
        sort();

        return getFirstNodeValue(key);
    }

    /**
     * hash运算
     */
    public Long hash(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();
        byte[] keyBytes = null;
        try {
            keyBytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unknown string :" + value, e);
        }

        md5.update(keyBytes);
        byte[] digest = md5.digest();

        // hash code, Truncate to 32-bits
        long hashcode = ((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF);

        long truncateHashCode = hashcode & 0xffffffffL;
        return truncateHashCode;
    }
}
