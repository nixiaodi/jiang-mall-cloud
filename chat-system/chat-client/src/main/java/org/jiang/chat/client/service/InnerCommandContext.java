package org.jiang.chat.client.service;

import lombok.extern.slf4j.Slf4j;
import org.jiang.chat.client.service.impl.command.PrintAllCommand;
import org.jiang.chat.client.util.SpringBeanFactory;
import org.jiang.chat.common.enums.SystemCommandEnum;
import org.jiang.chat.common.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InnerCommandContext {

    /**
     * 获取执行器实例
     */
    public InnerCommand getInstance(String command) {
        Map<String, String> allClazz = SystemCommandEnum.getAllClazz();

        // 兼容需要命令后接参数的数据 :q cross
        String[] trim = command.trim().split(" ");
        String clazz = allClazz.get(trim[0]);
        InnerCommand innerCommand = null;
        try {
            if (StringUtil.isEmpty(clazz)) {
                clazz = PrintAllCommand.class.getName();
            }
            innerCommand = ((InnerCommand) SpringBeanFactory.getBean(Class.forName(clazz)));
        } catch (Exception e) {
            log.error("Exception", e);
        }

        return innerCommand;
    }
}
