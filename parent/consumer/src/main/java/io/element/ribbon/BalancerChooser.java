/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */

package io.element.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张晓华
 * @date 2023-06-06 18:50
 * @usage 实例选择器
 */
public class BalancerChooser {

    /**
     * 根据权重选择出一个服务实例使用
     * Return one host from the host list by random-weight.
     *
     * @param hosts The list of the host.
     * @return The random-weight result of the host
     */
    protected static Instance getHostByRandomWeight(List<Instance> hosts) {
        if (hosts == null || hosts.size() == 0) {
            return null;
        }
        List<Pair<Instance>> hostsWithWeight = new ArrayList<Pair<Instance>>();
        for (Instance host : hosts) {
            if (host.isHealthy()) {
                hostsWithWeight.add(new Pair<Instance>(host, host.getWeight()));
            }
        }
        Chooser<String, Instance> vipChooser = new Chooser<String, Instance>("www.taobao.com");
        vipChooser.refresh(hostsWithWeight);
        return vipChooser.randomWithWeight();
    }

}
