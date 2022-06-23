package com.lw.rmi.server;

import java.util.Objects;

/**
 * @author shkstart
 * @create 2022-06-22 22:38
 */
public class NodeAddress {
    private int port;
    private String ip;

    public NodeAddress(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public NodeAddress() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeAddress that = (NodeAddress) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, ip);
    }
}
