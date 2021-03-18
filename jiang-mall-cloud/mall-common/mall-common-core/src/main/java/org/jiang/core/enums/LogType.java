package org.jiang.core.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 日志类型
 * @author 蒋小胖
 */
public enum LogType {
    /**
     * 操作日志
     */
    OPERATION_LOG("10", "操作日志"),
    /**
     * 登录日志
     */
    LOGIN_LOG("20", "登录日志"),
    /**
     * 异常日志
     */
    EXCEPTION_LOG("30", "异常日志");

    /**
     * type
     */
    String type;
    /**
     * name
     */
    String name;

    LogType(String type,String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets name.
     * @param type the type
     * @return the name
     */
    public static String getName(String type) {
        for (LogType ele : LogType.values()) {
            if (type.equals(ele.getType())) {
                return ele.getName();
            }
        }
        return null;
    }

    /**
     * Get enum
     * @param type the type
     * @return the enum
     */
    public static LogType getLogType(String type) {
        for (LogType ele : LogType.values()) {
            if (type.equals(ele.getType())) {
                return ele;
            }
        }
        return LogType.OPERATION_LOG;
    }

    /**
     * Get list
     * @return the list
     */
    public static List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (LogType ele : LogType.values()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("key", ele.getType());
            map.put("value", ele.getName());
            list.add(map);
        }
        return list;
    }
}
