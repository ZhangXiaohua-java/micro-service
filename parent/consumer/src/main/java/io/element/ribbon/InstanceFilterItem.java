/*
 * Copyright(c) 张晓华 $date
 * ******张晓华拥有本软件的版权2023并保留所有权力*******
 * 温馨提示：
 * 适度编码益脑，沉迷编码伤身，合理安排时间，享受快乐生活。
 */

package io.element.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-06-06 20:01
 * @usage 当前类的用途描述
 */
public class InstanceFilterItem {

    /* expected version */
    private String version;


    /* service instance */
    private Instance instance;


    public InstanceFilterItem(String version, Instance instance) {
        this.version = version;
        this.instance = instance;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceFilterItem that = (InstanceFilterItem) o;
        return Objects.equals(version, that.version) && Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, instance);
    }


    @Override
    public String toString() {
        return "CompareItem{" +
                "version='" + version + '\'' +
                ", instance=" + instance +
                '}';
    }


}
