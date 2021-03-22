package org.jiang.chat.common.core.proxy;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jiang.chat.common.enums.StatusEnum;
import org.jiang.chat.common.exception.ChatException;
import org.jiang.chat.common.utils.HttpClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 蒋小胖
 */
@Slf4j
public class ProxyManager<T> {
    private Class<T> clazz;
    private String url;
    private OkHttpClient okHttpClient;

    /**
     * 代理构造器
     * @param clazz Proxied interface
     * @param url server provider url
     * @param okHttpClient
     */
    public ProxyManager(Class<T> clazz, String url, OkHttpClient okHttpClient) {
        this.clazz = clazz;
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    public T getInstance() {
        return ((T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ProxyInvocation()));
    }

    private class ProxyInvocation implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            JSONObject jsonObject = new JSONObject();
            String serverUrl = url + "/" + method.getName();

            if (args != null && args.length > 1) {
                throw new ChatException(StatusEnum.VALIDATION_FAIL);
            }

            if (method.getParameterTypes().length > 0) {
                Object param = args[0];
                Class<?> parameterType = method.getParameterTypes()[0];
                for (Field field : parameterType.getDeclaredFields()) {
                    field.setAccessible(true);
                    jsonObject.put(field.getName(),field.get(param));
                }
            }

            return HttpClient.call(okHttpClient,jsonObject.toString(),serverUrl);
        }
    }
}
