package org.jiang.chat.route.api.vo.response;

import org.jiang.chat.common.model.RouteInfo;

import java.io.Serializable;

/**
 * @author 蒋小胖
 */
public class CIMServerResponseVo implements Serializable {
    private String ip ;
    private Integer cimServerPort;
    private Integer httpPort;

    public CIMServerResponseVo(RouteInfo routeInfo) {
        this.ip = routeInfo.getIp();
        this.cimServerPort = routeInfo.getCimServerPort();
        this.httpPort = routeInfo.getHttpPort();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCimServerPort() {
        return cimServerPort;
    }

    public void setCimServerPort(Integer cimServerPort) {
        this.cimServerPort = cimServerPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
