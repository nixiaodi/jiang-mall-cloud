package org.jiang.chat.client.thread;

/**
 * something about of client runtime sign
 * @author 蒋小胖
 */
public class ContextHolder {

    private static final ThreadLocal<Boolean> IS_RECONNECT = new ThreadLocal<>();

    public static Boolean getReconnect() {
        return IS_RECONNECT.get();
    }

    public static void setReconnect(boolean reconnect) {
        IS_RECONNECT.set(reconnect);
    }

    public static void clear() {
        IS_RECONNECT.remove();
    }
}
