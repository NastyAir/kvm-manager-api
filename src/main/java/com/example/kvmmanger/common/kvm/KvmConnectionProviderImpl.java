package com.example.kvmmanger.common.kvm;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.libvirt.Connect;
import org.springframework.beans.factory.InitializingBean;

public class KvmConnectionProviderImpl implements KvmConnectionProvider, InitializingBean {
    // thrift服务器的配置
    private KvmConnConfig kvmConnConfig;

    // 连接池
    private GenericObjectPool<Connect> objectPool;

    @Override
    public Connect getConnection() {
        try {
            return objectPool.borrowObject();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("getConnection出现异常", e);
        }

    }

    @Override
    public void returnConnection(Connect connect) {
        try {
            // 将对象放回对象池
            objectPool.returnObject(connect);
        } catch (Exception e) {
            throw new RuntimeException("returnConnection出现异常", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化连接工厂
        KvmConnectionFactory kvmConnectionFactory = new KvmConnectionFactory(kvmConnConfig);
        // 初始化连接池
        objectPool = new GenericObjectPool<>(kvmConnectionFactory);
        // TODO:设置连接池的参数，否则使用默认的配置
    }

    @Override
    public void setKvmConfigItem(KvmConnConfig kvmConnConfig) {
        this.kvmConnConfig = kvmConnConfig;
    }
}
