package io.element.controller;

import io.element.feign.ProviderRemoteClient;
import io.element.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

@RestController
public class ExposedController {


    @Resource
    private ProviderRemoteClient providerRemoteClient;

    @Resource
    private LoadBalancerClient loadBalancerClient;



    private static Logger logger = LoggerFactory.getLogger(ExposedController.class);


    @GetMapping("/date")
    public Result date() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        Map<String, String> metadata = serviceInstance.getMetadata();
        String serviceId = serviceInstance.getServiceId();
        URI uri = serviceInstance.getUri();
        logger.info("负载均衡选择出的主机:{},端口:{},服务序号:{},uri:{},元信息:{}", host, port, serviceId, uri, metadata);
        return providerRemoteClient.currentDate();
    }


}
