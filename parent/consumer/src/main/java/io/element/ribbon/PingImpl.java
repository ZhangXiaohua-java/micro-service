/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */

package io.element.ribbon;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Inet4Address;

/**
 * @author 张晓华
 * @date 2023-06-06 20:36
 * @usage 当前类的用途描述
 */
public class PingImpl implements IPing {

    private String strategy;

    private RestTemplate restTemplate;


    public static final String TCP_PING = "TCP";

    public static final String INTERFACE_PING = "I_PING";

    private static Logger logger = LoggerFactory.getLogger(PingImpl.class);

    public PingImpl() {
        this(TCP_PING, new RestTemplate());
    }

    public PingImpl(String strategy, RestTemplate restTemplate) {
        this.strategy = strategy;
        this.restTemplate = restTemplate;
    }


    @Override
    public boolean isAlive(Server server) {
        switch (this.strategy) {
            case TCP_PING:
                return tcpPing(server);
            case INTERFACE_PING:
                return interfacePing(server);
            default:
                return false;
        }
    }


    public boolean tcpPing(Server server) {
        try {
            return Inet4Address.getByName(server.getHost()).isReachable(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean interfacePing(Server server) {
        String hostPort = server.getHostPort();
        String scheme = server.getScheme();
        String url = scheme + hostPort + "/ping";
        String str = restTemplate.getForObject(url, String.class);
        return "OK".equalsIgnoreCase(str);
    }


}
