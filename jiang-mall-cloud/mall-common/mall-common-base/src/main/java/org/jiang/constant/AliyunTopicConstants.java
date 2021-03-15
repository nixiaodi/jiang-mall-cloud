package org.jiang.constant;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunTopicConstants {

    /**
     * MQ Uac topic
     */
    public enum MqTopicEnum {

        /**
         * 发送短信
         */
        SEND_SMS_TOPIC("SEND_SMS_TOPIC", "发送短信"),
        /**
         * 发送邮件
         */
        SEND_EMAIL_TOPIC("SEND_EMAIL_TOPIC", "发送邮件"),
        /**
         * tpc主题
         */
        TPC_TOPIC("TPC_TOPIC", "TPC_TOPIC"),
        /**
         * opc主题
         */
        OPC_TOPIC("OPC_TOPIC", "OPC_TOPIC"),
        /**
         * mdc主题
         */
        MDC_TOPIC("MDC_TOPIC", "MDC_TOPIC");

        MqTopicEnum(String topic,String topicName) {
            this.topic = topic;
			this.topicName = topicName;
        };

        /**
         * 主题
         */
        String topic;

        /**
         * 主题名称
         */
        String topicName;

        /**
         * Gets topic
         */
        public String getTopic() {
            return topic;
        }
    }

    /**
     * UAC tag MQ
     */
    public enum MqTagEnum {

        /**
         * 获取注册验证码
         */
        REGISTER_USER_AUTH_CODE("REGISTER_USER_AUTH_CODE",MqTopicEnum.SEND_SMS_TOPIC.getTopic(),"注册获取验证码"),
        /**
         * 修改密码获取验证码
         */
        MODIFY_PASSWORD_AUTH_CODE("MODIFY_PASSWORD_AUTH_CODE",MqTopicEnum.SEND_SMS_TOPIC.getTopic(),"修改密码获取验证码"),
        /**
         * 忘记密码获取验证码.
         */
        FORGOT_PASSWORD_AUTH_CODE("FORGOT_PASSWORD_AUTH_CODE", MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(), "忘记密码获取验证码"),

        /**
         * 激活用户.
         */
        ACTIVE_USER("ACTIVE_USER", MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(), "激活用户"),
        /**
         * 激活用户成功.
         */
        ACTIVE_USER_SUCCESS("ACTIVE_USER_SUCCESS", MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(), "激活用户成功"),
        /**
         * 重置密码
         */
        RESET_LOGIN_PWD("RESET_LOGIN_PWD", MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(), "重置密码"),

        /**
         * 重置密码
         */
        RESET_USER_EMAIL("RESET_LOGIN_PWD", MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(), "重置密码"),

        /**
         * 删除生产者历史消息
         */
        DELETE_PRODUCER_MESSAGE("DELETE_PRODUCER_MESSAGE", MqTopicEnum.TPC_TOPIC.getTopic(), "删除生产者历史消息"),
        /**
         * 删除消费者历史消息
         */
        DELETE_CONSUMER_MESSAGE("DELETE_CONSUMER_MESSAGE", MqTopicEnum.TPC_TOPIC.getTopic(), "删除消费者历史消息"),

        /**
         * 发送异常日志.
         */
        SEND_DINGTALK_MESSAGE("SEND_EXCEPTION_LOG", MqTopicEnum.OPC_TOPIC.getTopic(), "发送异常日志"),

        /**
         * 更新附件信息.
         */
        UPDATE_ATTACHMENT("UPDATE_ATTACHMENT", MqTopicEnum.MDC_TOPIC.getTopic(), "更新附件信息"),
        /**
         * 删除附件信息
         */
        DELETE_ATTACHMENT("DELETE_ATTACHMENT", MqTopicEnum.MDC_TOPIC.getTopic(), "删除附件信息"),;

        /**
         * tag
         */
        String tag;
        /**
         * topic
         */
        String topic;
        /**
         * tag name
         */
        String tagName;

        MqTagEnum(String tag,String topic,String tagName) {
            this.tag = tag;
            this.topic = topic;
            this.tagName = tagName;
        }

        /**
         * Gets tag.
         */
        public String getTag() {
            return tag;
        }

        /**
         * Gets topic.
         */
        public String getTopic() {
            return topic;
        }
    }

    /**
     * consumer topic MQ
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ConsumerTopics {

        private static String buildOpcConsumerTopics() {
            List<TopicObj> topicObjList = new ArrayList<>();

            Set<String> sendSmsTagList = new HashSet<>();
            sendSmsTagList.add(MqTagEnum.REGISTER_USER_AUTH_CODE.getTag());
            Set<String> sendEmailTagList = new HashSet<>();
            sendEmailTagList.add(MqTagEnum.FORGOT_PASSWORD_AUTH_CODE.getTag());
            sendEmailTagList.add(MqTagEnum.ACTIVE_USER.getTag());
            sendEmailTagList.add(MqTagEnum.ACTIVE_USER_SUCCESS.getTag());
            sendEmailTagList.add(MqTagEnum.RESET_LOGIN_PWD.getTag());
            sendEmailTagList.add(MqTagEnum.RESET_USER_EMAIL.getTag());

            topicObjList.add(new TopicObj(MqTopicEnum.SEND_SMS_TOPIC.getTopic(),sendSmsTagList));
            topicObjList.add(new TopicObj(MqTopicEnum.SEND_EMAIL_TOPIC.getTopic(),sendEmailTagList));

            Set<String> deleteMassageTag = new HashSet<>();
            deleteMassageTag.add(MqTagEnum.DELETE_CONSUMER_MESSAGE.getTag());
            deleteMassageTag.add(MqTagEnum.DELETE_PRODUCER_MESSAGE.getTag());

            topicObjList.add(new TopicObj(MqTopicEnum.MDC_TOPIC.getTopic(),deleteMassageTag));

            Set<String> mdcMqTag = new HashSet<>();
            mdcMqTag.add(MqTagEnum.UPDATE_ATTACHMENT.getTag());
            mdcMqTag.add(MqTagEnum.DELETE_ATTACHMENT.getTag());

            topicObjList.add(new TopicObj(MqTopicEnum.MDC_TOPIC.getTopic(),mdcMqTag));

            return buildOpcConsumerTopic(topicObjList);
        }

        private static String buildUacConsumerTopics() {
            List<TopicObj> topicObjList = Lists.newArrayList();

            Set<String> deleteMessageTag = new HashSet<>();
            deleteMessageTag.add(MqTagEnum.DELETE_CONSUMER_MESSAGE.getTag());
            deleteMessageTag.add(MqTagEnum.DELETE_PRODUCER_MESSAGE.getTag());

            topicObjList.add(new TopicObj(MqTopicEnum.TPC_TOPIC.getTopic(),deleteMessageTag));
            return buildOpcConsumerTopic(topicObjList);
        }

        private static String buildOpcConsumerTopic(List<TopicObj> topicList) {
            StringBuilder result = new StringBuilder();

            if (!CollectionUtils.isEmpty(topicList)) {
                for (TopicObj topicObj : topicList) {
                    String topic = topicObj.getTopic();
                    Set<String> tagList = topicObj.getTagList();

                    if (StringUtils.isEmpty(topic) || CollectionUtils.isEmpty(tagList)) {
                        continue;
                    }

                    StringBuilder tagInfo = new StringBuilder();
                    for (String tag : tagList) {
                        tagInfo.append(tag).append(GlobalConstant.Symbol.PIPE);
                    }
                    trimEnd(tagInfo,GlobalConstant.Symbol.PIPE);
                    result.append(topic).append(GlobalConstant.Symbol.AT).append(tagInfo);
                }
            }
            trimEnd(result,GlobalConstant.Symbol.COMMA);
            return result.toString();
        }
    }

    /**
     * topic obj
     */
    static class TopicObj {
        private String topic;
        private Set<String> tagList;

        /**
         * instantiate a new Topic obj
         */
        TopicObj(String topic,Set<String> tagList) {
            this.topic = topic;
            this.tagList = tagList;
        }

        /**
         * get topic
         * @return
         */
        public String getTopic() {
            return topic;
        }

        /**
         * get tag list
         */
        public Set<String> getTagList() {
            return tagList;
        }
    }

    private static void trimEnd(StringBuilder stringBuilder,String suffix) {
        if (stringBuilder == null) {
            return;
        }

        String str = stringBuilder.toString();
        if (!StringUtils.isEmpty(suffix) && !str.endsWith(suffix)) {
            return;
        }
        stringBuilder.delete(str.length() - suffix.length(),str.length());
    }
}
