package org.jiang.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 将字符串转为long
 */
@Slf4j
public class LongJsonDeserializer extends JsonDeserializer<Long> {

    /**
     * deserialize long
     */
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        String value = null;
        try {
            value = jsonParser.getText();
        } catch (IOException e) {
            log.error("deserialize={}",e.getMessage(),e);
        }
        try {
            return value == null ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("解析long错误",e);
            return null;
        }
    }
}
