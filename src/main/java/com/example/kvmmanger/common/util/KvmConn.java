package com.example.kvmmanger.common.util;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public enum KvmConn {
    CONNECT;
    private Connect kvmConn = null;

    KvmConn() {
        try {
            kvmConn = new Connect("qemu+tcp://192.168.71.147/system");
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }

    public Connect getConnection() {
        return kvmConn;
    }
}
