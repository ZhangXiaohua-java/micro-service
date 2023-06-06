package io.element.conifg;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import io.element.ribbon.InstanceFilterItem;
import io.element.ribbon.PingImpl;
import io.element.ribbon.VersionCallRuleImpl;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;

@Configuration
public class NacosConfiguration {


    /* 为当前服务实例添加元信息 */
    @Bean
    public NacosDiscoveryProperties nacosDiscoveryProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        HashMap<String, String> metaData = new HashMap<>();
        // 当前服务的版本信息
        metaData.put("version", "0.1");
        // 上线时间
        metaData.put("publish", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        nacosDiscoveryProperties.setMetadata(metaData);
        return nacosDiscoveryProperties;
    }


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /* 使用自定义的负载均衡策略 */
    @Bean
    public IRule rule(NacosDiscoveryProperties nacosDiscoveryProperties, NacosServiceManager nacosServiceManager) {
        ArrayList<Predicate<InstanceFilterItem>> filters = new ArrayList<>();
        Predicate<InstanceFilterItem> filterItemPredicate = e -> e.getVersion().equalsIgnoreCase(e.getInstance().getMetadata().get("version"));
        filters.add(filterItemPredicate);
        Predicate<InstanceFilterItem> reachableFilter = e -> e.getInstance().isHealthy();
        return new VersionCallRuleImpl(nacosDiscoveryProperties, nacosServiceManager, filters);
    }


    @Bean
    public IPing ping() {
        return new PingImpl(PingImpl.INTERFACE_PING, restTemplate());
    }


}
