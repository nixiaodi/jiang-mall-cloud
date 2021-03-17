package org.jiang;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalMap {

    /**
     * thread local context
     */
    private static final ThreadLocal<Map<String,Object>> THREAD_CONTEXT = new MapThreadLocal();

    /**
     * 获取thread context map实例
     */
    public static Map<String, Object> getContextMap() {
        return THREAD_CONTEXT.get();
    }

    /**
     * put
     */
    public static void put(String key, Object value) {
        getContextMap().put(key,value);
    }

    /**
     * get
     */
    public static Object get(String key) {
        return getContextMap().get(key);
    }

    /**
     * remove
     */
    public static Object remove(String key) {
        return getContextMap().remove(key);
    }

    /**
     * 清理线程所有被hold住的对象，便于重用
     */
    public static void remove() {
        getContextMap().clear();
    }


    private  static class MapThreadLocal extends ThreadLocal<Map<String,Object>> {

        /**
         * initial value map
         * @return
         */
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String,Object>(8) {
                private static final long serialVersionUID = 3637958959138295593L;

                @Override
                public Object put(String key, Object value) {
                    return super.put(key, value);
                }
            };
        }
    }
}
