package com.example.kvmmanger.service;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.common.kvm.KvmConnectionProvider;
import com.example.kvmmanger.common.kvm.KvmMultipleConnFactory;
import com.example.kvmmanger.common.util.RetResponse;
import com.example.kvmmanger.entity.Host;
import lombok.extern.slf4j.Slf4j;
import org.libvirt.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KvmService {

//======================存储池======================

    /**
     * StoragePool 列表
     *
     * @param host
     * @return
     * @throws LibvirtException
     */
    public Result<String[]> listStoragePool(Host host) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        String[] pools = connect.listStoragePools();
        connectionProvider.returnConnection(connect);
        return RetResponse.success(pools);
    }

    /**
     * 获取存储池详情
     *
     * @param host
     * @param name
     * @return
     * @throws LibvirtException
     */
    public Result<Map<String, Object>> getStoragePoolByName(Host host, String name) {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = null;
        StoragePoolInfo storagePoolInfo = null;
        try {
            storagePool = connect.storagePoolLookupByName(name);
            storagePoolInfo = storagePool.getInfo();
        } catch (LibvirtException e) {
            connectionProvider.returnConnection(connect);
            if (e.getMessage().contains("Storage pool not found")) {
                throw new BusinessException(RetCode.RECORD_NOT_FOUND);
            } else {
                throw new BusinessException(RetCode.FAIL, "获取存储池信息错误");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("state", storagePoolInfo.state);
        map.put("capacity", storagePoolInfo.capacity);
        map.put("available", storagePoolInfo.available);
        map.put("allocation", storagePoolInfo.allocation);
        try {
            map.put("xml", storagePool.getXMLDesc(0));
        } catch (LibvirtException e) {
//            e.printStackTrace();
            connectionProvider.returnConnection(connect);
            log.error(e.getMessage());
        }
        connectionProvider.returnConnection(connect);
        return RetResponse.success(map);
    }

    /**
     * 创建存储池（临时）
     *
     * @param host
     * @param xmlDesc
     * @return
     * @throws LibvirtException
     */
    public Result createStoragePool(Host host, String xmlDesc) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolCreateXML(xmlDesc, 0);
        StoragePoolInfo storagePoolInfo = storagePool.getInfo();
        storagePool.create(0);
        connectionProvider.returnConnection(connect);
        return RetResponse.success(storagePoolInfo);
    }

    /**
     * 定义存储池
     *
     * @param host
     * @param xmlDesc
     * @return
     * @throws LibvirtException
     */
    public Result defineStoragePool(Host host, String xmlDesc) {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = null;
        StoragePoolInfo storagePoolInfo = null;
        try {
            storagePool = connect.storagePoolDefineXML(xmlDesc, 0);
            storagePoolInfo = storagePool.getInfo();
            storagePool.create(0);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL);
        }
        connectionProvider.returnConnection(connect);
        return RetResponse.success(storagePoolInfo);
    }

    /**
     * 删除存储池
     *
     * @param host
     * @param name
     * @return
     * @throws LibvirtException
     */
    public Result delStoragePool(Host host, String name) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolLookupByName(name);
        storagePool.destroy();
        storagePool.undefine();
        connectionProvider.returnConnection(connect);
        return RetResponse.success();
    }

//======================存储卷======================

    /**
     * @param host
     * @param storagePoolName
     * @return Result<String [ ]>
     * @throws LibvirtException 遍历存储卷
     */
    public Result<String[]> listStorageVolume(Host host, String storagePoolName) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolLookupByName(storagePoolName);
        String[] volumes = storagePool.listVolumes();
        connectionProvider.returnConnection(connect);
        return RetResponse.success(volumes);
    }

    /**
     * @param host
     * @param storagePoolName
     * @param storageVolName
     * @return
     * @throws LibvirtException 获取存储卷详情
     */
    public Result<StorageVolInfo> getStorageVolumebyName(Host host, String storagePoolName, String storageVolName) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolLookupByName(storagePoolName);
//        String[] volumes = storagePool.listVolumes();
        StorageVol storageVol = storagePool.storageVolLookupByName(storageVolName);
        StorageVolInfo storageVolInfo = storageVol.getInfo();
        connectionProvider.returnConnection(connect);
        return RetResponse.success(storageVolInfo);
    }

    /**
     * 创建存储卷
     *
     * @param host
     * @param storagePoolName
     * @param xmlDesc
     * @return
     * @throws LibvirtException
     */
    public Result createStorageVolume(Host host, String storagePoolName, String xmlDesc) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolLookupByName(storagePoolName);
        StorageVol storageVol = storagePool.storageVolCreateXML(xmlDesc, 0);
        StorageVolInfo storageVolInfo = storageVol.getInfo();
        connectionProvider.returnConnection(connect);
        return RetResponse.success(storageVolInfo);
    }

    /**
     * 克隆存储卷
     *
     * @param host
     * @param storagePoolName
     * @param xmlDesc
     * @return
     * @throws LibvirtException
     */
    public Result cloneStorageVolume(Host host, String storagePoolName, String sourceStorageVolName, String xmlDesc) {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = null;
        StorageVolInfo storageVolInfo = null;
        try {
            storagePool = connect.storagePoolLookupByName(storagePoolName);
            StorageVol sourceVol = storagePool.storageVolLookupByName(sourceStorageVolName);
            StorageVol storageVol = storagePool.storageVolCreateXMLFrom(xmlDesc, sourceVol, 0);
            storageVolInfo = storageVol.getInfo();
        } catch (LibvirtException e) {
            connectionProvider.returnConnection(connect);
            e.printStackTrace();
        }
        connectionProvider.returnConnection(connect);
        return RetResponse.success(storageVolInfo);
    }

    /**
     * 删除存储卷
     *
     * @param host
     * @param storagePoolName
     * @param storageVolName
     * @return
     * @throws LibvirtException
     */
    public Result<StorageVolInfo> deleteStorageVolume(Host host, String storagePoolName, String storageVolName) throws LibvirtException {
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(host.getIp());
        Connect connect = connectionProvider.getConnection();
        StoragePool storagePool = connect.storagePoolLookupByName(storagePoolName);
//        String[] volumes = storagePool.listVolumes();
        StorageVol storageVol = storagePool.storageVolLookupByName(storageVolName);
//        StorageVolInfo storageVolInfo = storageVol.getInfo();
        storageVol.wipe();
        storageVol.delete(0);
        connectionProvider.returnConnection(connect);
        return RetResponse.success();
    }

}
