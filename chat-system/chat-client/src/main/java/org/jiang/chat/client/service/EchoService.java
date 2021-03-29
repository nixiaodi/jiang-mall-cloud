package org.jiang.chat.client.service;

public interface EchoService {

    /**
     * echo message to terminal
     * @param message
     * @param replace
     */
    void echo(String message,Object... replace);
}
