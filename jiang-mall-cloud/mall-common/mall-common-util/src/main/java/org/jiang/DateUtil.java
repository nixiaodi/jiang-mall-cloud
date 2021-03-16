package org.jiang;

import com.xiaoleilu.hutool.date.DateField;
import com.xiaoleilu.hutool.date.DateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    /**
     * 获取系统之前一定间隔的时间
     */
    public static String getBeforeTime(int minute) {
        DateTime newDate = com.xiaoleilu.hutool.date.DateUtil.offset(new Date(), DateField.MINUTE, -minute);
        return com.xiaoleilu.hutool.date.DateUtil.formatDate(newDate);
    }
}
