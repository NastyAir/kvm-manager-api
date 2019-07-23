package com.example.kvmmanger.common.kvm;

import org.libvirt.Connect;

public interface KvmConnectionProvider {
    /**
     * 从连接池里获取一个TProtocol对象
     * @return
     */
    Connect getConnection();

    /**
     * 将一个Connect对象放回连接池
     * @param connect
     */
    void returnConnection(Connect connect);

    void setKvmConfigItem(KvmConnConfig kvmConnConfig);
}
