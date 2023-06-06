/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */

package io.element.ribbon;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author 张晓华
 * @date 2023-06-06 19:40
 * @usage 替代RibbonLoadBalancerClient类完成本地负载均衡
 */
@Component
public class LoadBalanceClient extends RibbonLoadBalancerClient {


    @Resource
    private ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory;

    public LoadBalanceClient(SpringClientFactory clientFactory) {
        super(clientFactory);
    }


    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        ReactiveLoadBalancer<ServiceInstance> loadBalancer = loadBalancerClientFactory.getInstance(serviceId);
        if (loadBalancer == null) {
            return null;
        }
        Response<ServiceInstance> loadBalancerResponse = Mono.from(loadBalancer.choose(request)).block();
        if (loadBalancerResponse == null) {
            return null;
        }
        return loadBalancerResponse.getServer();
    }


}
