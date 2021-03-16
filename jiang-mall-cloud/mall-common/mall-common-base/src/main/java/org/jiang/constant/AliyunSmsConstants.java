package org.jiang.constant;

import com.google.common.collect.Lists;
import org.jiang.enums.ErrorCode;
import org.jiang.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class AliyunSmsConstants {

    /**
     * 短信模板
     */
    public enum SmsTemplate {
        /**
         * 通用模板(短信内容：验证码${code}, 您正在注册成为jiangMall用户, 感谢您的支持！)
         */
        UAC_PC_GLOBAL_TEMPLATE("UAC_PC_GLOBAL_TEMPLATE","SMS_105115057","code");

        private String busType;

        private String templateCode;

        private String smsParamName;

        SmsTemplate(String busType, String templateCode, String smsParamName) {
            this.busType = busType;
            this.templateCode = templateCode;
            this.smsParamName = smsParamName;
        }

        public static SmsTemplate getTemplate(String templateCode) {
            SmsTemplate smsTemplate = null;
            for (SmsTemplate el : SmsTemplate.values()) {
                if (templateCode.equals(el.getTemplateCode())) {
                    smsTemplate = el;
                    break;
                }
            }
            return smsTemplate;
        }

        public static List<SmsTemplate> getList() {
            return Arrays.asList(SmsTemplate.values());
        }

        public static List<String> getTemplateCodeList() {
            List<String> templateCodeList = Lists.newArrayList();
            List<SmsTemplate> smsTemplateList = getList();
            for (SmsTemplate smsTemplate : smsTemplateList) {
                if (!StringUtils.hasLength(smsTemplate.getTemplateCode())) {
                    continue;
                }
                templateCodeList.add(smsTemplate.getTemplateCode());
            }
            return templateCodeList;
        }

        public static boolean isSmsTemplate(String smsTemplateCode) {
            if (StringUtils.isEmpty(smsTemplateCode)) {
                throw new BusinessException(ErrorCode.UAC10011020);
            }

            List<String> templateCodeList = getTemplateCodeList();
            return templateCodeList.contains(smsTemplateCode);
        }

        /**
         * get bus type
         */
        public String getBusType() {
            return busType;
        }

        /**
         * get template code
         */
        public String getTemplateCode() {
            return templateCode;
        }

        /**
         * get param name
         */
        public String getSmsParamName() {
            return smsParamName;
        }
    }
}
