package org.jiang.chat.common.utils;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import io.netty.channel.Channel;

import java.util.Objects;

/**
 * @author 蒋小胖
 */
public class NettyAttributeUtil {
    private static final AttributeKey<String> ATTR_KEY_READER_TIME = AttributeKey.valueOf("readerTime");

    public static void updateReaderTime(Channel channel,Long time) {
        channel.attr(ATTR_KEY_READER_TIME).set(time.toString());
    }

    public static Long getReaderTime(Channel channel) {
        String value = getAttribute(channel, ATTR_KEY_READER_TIME);
        if (Objects.nonNull(value)) {
            return Long.valueOf(value);
        }
        return null;
    }

    public static String getAttribute(Channel channel,AttributeKey<String> key) {
        Attribute<String> attr = channel.attr(key);
        return attr.get();
    }
}
