package com.example.kvmmanger.common.kvm;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public class KvmConnectionFactory extends BasePooledObjectFactory<Connect> {
    private String serverIP;

    public KvmConnectionFactory(KvmConnConfig connConfig) {
        this.serverIP = connConfig.getServerIP();
    }

    @Override
    public Connect create() throws LibvirtException {
        Connect kvmConn = new Connect("qemu+tcp://" + serverIP + "/system");
        return kvmConn;
    }

    @Override
    public void destroyObject(PooledObject<Connect> pooledObject) throws Exception {
        Connect connect = pooledObject.getObject();
        if (connect.isConnected()) {
            connect.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Connect> p) {
        // 这里确保返回的是已打开的连接
        Connect connect = p.getObject();
        boolean isConnected = false;
        if (connect != null) {
            try {
                isConnected = connect.isConnected();
            } catch (LibvirtException e) {
                e.printStackTrace();
                throw new RuntimeException("validateObject出现异常", e);
            }
        }
        return isConnected;
    }

    @Override
    public PooledObject<Connect> wrap(Connect conn) {
        //包装实际对象
        return new DefaultPooledObject<>(conn);
    }

}
