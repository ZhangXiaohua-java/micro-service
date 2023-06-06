package io.element.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Configuration
public class NacosConfiguration {


    @Bean
    public NacosDiscoveryProperties nacosDiscoveryProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        HashMap<String, String> metaData = new HashMap<>();
        // 当前服务的版本信息
        metaData.put("version", "0.2");
        // 上线时间
        metaData.put("publish", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        nacosDiscoveryProperties.setMetadata(metaData);
        return nacosDiscoveryProperties;
    }


}
