# micro-service

**call service interface according to the version of current service under the help of Nacos and Ribbon.**
**使用Nacos和Ribbon根据版本号调用服务接口**

## 使用方法
```java

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
    
    /* 使用自定义的负载均衡策略 */
    @Bean
    public IRule rule(NacosDiscoveryProperties nacosDiscoveryProperties, NacosServiceManager nacosServiceManager) {
        ArrayList<Predicate<InstanceFilterItem>> filters = new ArrayList<>();
        //自定义谓词过滤掉和当前版本不一致的服务信息
        Predicate<InstanceFilterItem> filterItemPredicate = e -> e.getVersion().equalsIgnoreCase(e.getInstance().getMetadata().get("version"));
        filters.add(filterItemPredicate);
        Predicate<InstanceFilterItem> reachableFilter = e -> e.getInstance().isHealthy();
        return new VersionCallRuleImpl(nacosDiscoveryProperties, nacosServiceManager, filters);
    }
    
    最重要的一点必须要替换掉Ribbon原有的本地负载均衡客户端,使用LoadBalanceClient类,将该类注入到IOC容器中即可.
    class LoadBalanceClient extends RibbonLoadBalancerClient 

```
