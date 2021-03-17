package org.jiang;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * url 转码、解码
 */
@Slf4j
public class UrlUtil {
    private static final String GBK = "GBK";

    /**
     * url 解码
     */
    public static String getURLDecodeString(String encodedUrl) {
        String result = "";
        if (encodedUrl == null) {
            return "";
        }
        try {
            result = URLDecoder.decode(encodedUrl,GBK);
        } catch (UnsupportedEncodingException e) {
            log.error("URL解码失败,message={}",e.getMessage(),e);
        }
        return result;
    }

    /**
     * url 转码
     */
    public static String getURLEncoderString(String url) {
        String result = "";
        if (null == url) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(url, GBK);
        } catch (UnsupportedEncodingException e) {
            log.error("URL转码失败,message={}", e.getMessage(), e);
        }
        return result;
    }
}
