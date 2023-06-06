/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */
package io.element.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 张晓华
 * @date 2023-06-06 18:50
 * @usage 实现了版本调用, 如当前0.1版本的服务调用目标的0.1版本的服务,
 * 也可以自定义谓词实现更多的服务实例的过滤
 */
public class VersionCallRuleImpl extends AbstractLoadBalancerRule {


    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private NacosServiceManager nacosServiceManager;

    private NamingService namingService;


    private static Logger logger = LoggerFactory.getLogger(VersionCallRuleImpl.class);


    private List<Predicate<InstanceFilterItem>> filters;

    public VersionCallRuleImpl(NacosDiscoveryProperties nacosDiscoveryProperties, NacosServiceManager serviceManager, List<Predicate<InstanceFilterItem>> filters) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.nacosServiceManager = serviceManager;
        this.filters = filters;
        getNamingService();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // do nothing
    }

    @Override
    public Server choose(Object key) {
        String version = nacosDiscoveryProperties.getMetadata().get("version");
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String serviceName = loadBalancer.getName();
        try {
            return chooseAvailableInstance(serviceName, true, version);
        } catch (NacosException e) {
            logger.warn("an unexpected error happens while find a service to call by version {}", version);
            throw new RuntimeException("no service meet the condition you specified to call");
        }
    }


    /**
     * @param serviceName the name of Remote Service
     * @param healthy     whether the service is or not healthy
     * @param version     the version expected to call
     * @return an available service if healthy is true
     * and can call the service in the same group by version which specified in Nacos MetaData
     * @throws NacosException no services or network error happen
     */
    public Server chooseAvailableInstance(String serviceName, boolean healthy, String version) throws NacosException {
        List<Instance> instances = chooseInstances(serviceName, healthy);
        List<InstanceFilterItem> filterItems = instances.stream().map(e -> new InstanceFilterItem(version, e)).collect(Collectors.toList());
        // whether can or can't call service of different groups depend on the Predicate you provided
        List<Instance> goalInstances = filter(filterItems);
        if (CollectionUtils.isEmpty(goalInstances)) {
            logger.error("no same version({}) service instance to call", version);
            throw new NacosException(500, "no same version instance");
        }
        Instance instance = BalancerChooser.getHostByRandomWeight(goalInstances);
        Assert.notNull(instance, "no same version instance");
        return new Server(instance.getIp(), instance.getPort());
    }


    /**
     * @param serviceName name of service to call
     * @param healthy     is healthy ?
     * @return service instances
     * @throws NacosException executed failed
     */
    private List<Instance> chooseInstances(String serviceName, boolean healthy) throws NacosException {
        return this.namingService.selectInstances(serviceName, healthy);
    }

    /**
     * @param items the instance need to filter
     * @return the instances which service instance meet the specified conditions
     */
    private List<Instance> filter(final List<InstanceFilterItem> items) {
        List<InstanceFilterItem> copy = items;
        for (Predicate<InstanceFilterItem> filter : filters) {
            copy = items.stream().filter(filter::test).collect(Collectors.toList());
        }
        return copy.stream().map(InstanceFilterItem::getInstance).collect(Collectors.toList());
    }


    private void getNamingService() {
        // Deprecated
        //NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        NamingService namingService = nacosServiceManager.getNamingService(this.nacosDiscoveryProperties.getNacosProperties());
        Assert.notNull(namingService, "namingService is null");
        this.namingService = namingService;
    }


}
