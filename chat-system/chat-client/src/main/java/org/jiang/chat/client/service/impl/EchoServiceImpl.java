package org.jiang.chat.client.service.impl;

import org.jiang.chat.client.config.AppConfiguration;
import org.jiang.chat.client.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EchoServiceImpl implements EchoService {

    private static final String PREFIX = "$";

    @Autowired
    private AppConfiguration appConfiguration;

    @Override
    public void echo(String message, Object... replace) {
        String datetime = LocalDate.now().toString() + " " + LocalTime.now().withNano(0).toString();

        message = "[" + datetime + "]\033[31;4m" + appConfiguration.getUserName() + PREFIX + "\033[0m" + " " + message;

        String log = print(message, replace);

        System.out.println(log);
    }

    /**
     * print message
     */
    private String print(String message,Object... place) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (int i = 0; i < place.length; i++) {
            int index = message.indexOf("{}", k);

            if (index == -1) {
                return message;
            }

            if (index != 0) {
                sb.append(message,k,index);

            }
            sb.append(place[i]);
            if (place.length == 1) {
                sb.append(message,index + 2,message.length());
            }

            k = index + 2;
        }

        if (sb.toString().equals("")) {
            return message;
        } else {
            return sb.toString();
        }
    }
}
