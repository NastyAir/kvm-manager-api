package com.example.kvmmanger.common.kvm;

import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import org.libvirt.Connect;

import java.util.HashMap;
import java.util.Map;

/**
 * 多ip kvm连接 工厂类
 */
public class KvmMultipleConnFactory {
    private static Map<String, KvmConnectionProvider> KvmConnectionProviderMap = new HashMap<>();

    /**
     * 获取ip的kvm连接，没有就获取后放入Map再后返回
     *
     * @param ip kvm IP地址
     * @return Connect kvm连接对象
     */
    public static KvmConnectionProvider getKvmConnect(String ip) {
        if (KvmConnectionProviderMap.containsKey(ip)) {
            return KvmConnectionProviderMap.get(ip);
        } else {
            KvmConnectionProvider connectionProvider = putKvmConnectionProvider(ip);
            return connectionProvider;
        }
    }

    /**
     * 设置ip所在对象
     *
     * @param ip
     */
    public static void setKvmConnect(String ip) {
        KvmConnectionProvider connectionProvider = putKvmConnectionProvider(ip);
    }

    private static KvmConnectionProvider putKvmConnectionProvider(String ip) {
        KvmConnConfig connConfig = new KvmConnConfig(ip);
        KvmConnectionProviderImpl connectionProvider = new KvmConnectionProviderImpl();
        connectionProvider.setKvmConfigItem(connConfig);
        connectionProvider.afterPropertiesSet();
        try {
            if (connectionProvider.getConnection().isConnected()) {
                KvmConnectionProviderMap.put(ip, connectionProvider);
                return connectionProvider;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BusinessException(RetCode.FAIL.getCode(), "无法连接到kvm，请确认配置信息");
    }
}
