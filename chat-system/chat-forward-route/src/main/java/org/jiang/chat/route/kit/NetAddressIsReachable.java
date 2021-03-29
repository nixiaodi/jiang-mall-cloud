package org.jiang.chat.route.kit;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetAddressIsReachable {

    public static boolean checkAddressReachable(String address,int port,int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address,port),timeout);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
        }
    }
}
