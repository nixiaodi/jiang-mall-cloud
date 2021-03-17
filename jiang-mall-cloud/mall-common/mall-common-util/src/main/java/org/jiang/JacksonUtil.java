package org.jiang;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {

    private static ObjectMapper defaultMapper;
    private static ObjectMapper formattedMapper;

    static {
        // 设置默认的ObjectMapper
        defaultMapper = new ObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        defaultMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        // 设置格式化ObjectMapper
        formattedMapper = new ObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        formattedMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 所有日期格式统一为固定格式
        formattedMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置序列化日期时不输出时间戳
        formattedMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
    }

    /**
     * 将对象转化为json
     */
    public static String toJson(Object obj) throws IOException {
        Preconditions.checkArgument(obj != null,"this argument is required; it must not be null");
        return defaultMapper.writeValueAsString(obj);
    }

    /**
     * json数据通过指定class转化为bean
     * @param valueType class类型
     * @throws IOException
     */
    public static <T> T parseJson(String jsonValue,Class<T> valueType) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue),"this argument is required; it must not be null");
        return defaultMapper.readValue(jsonValue,valueType);
    }

    /**
     * json数据通过指定JavaType转化为bean
     * @param valueType JavaType类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseJson(String jsonValue, JavaType valueType) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) defaultMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据通过指定TypeReference转化为bean
     * @param valueTypeRef TypeReference类型
     */
    public static <T> T parseJson(String jsonValue, TypeReference<T> valueTypeRef) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) defaultMapper.readValue(jsonValue, valueTypeRef);
    }

    /**
     * 将对象转化为json数据(时间转换格式： "yyyy-MM-dd HH:mm:ss")
     */
    public static String toJsonWithFormat(Object obj) throws IOException {
        Preconditions.checkArgument(obj != null, "this argument is required; it must not be null");
        return formattedMapper.writeValueAsString(obj);
    }

    /**
     * json数据通过class转化为对象(时间转换格式： "yyyy-MM-dd HH:mm:ss")
     */
    public static <T> T parseJsonWithFormat(String jsonValue, Class<T> valueType) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return formattedMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(JavaType)
     * @param valueType the value type
     */
    public static <T> T parseJsonWithFormat(String jsonValue, JavaType valueType) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) formattedMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(TypeReference)
     */
    public static <T> T parseJsonWithFormat(String jsonValue, TypeReference<T> valueTypeRef) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "jsonValue is not null");
        return (T) formattedMapper.readValue(jsonValue, valueTypeRef);
    }

}
