package com.example.kvmmanger.controller;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.entity.Host;
import com.example.kvmmanger.service.HostService;
import com.example.kvmmanger.service.KvmService;
import io.swagger.annotations.ApiOperation;
import org.libvirt.LibvirtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/storageVolume")
public class StorageVolumeCtrl {
    private final KvmService kvmService;
    private final HostService hostService;

    @Autowired
    public StorageVolumeCtrl(KvmService kvmService, HostService hostService) {
        this.kvmService = kvmService;
        this.hostService = hostService;
    }

    @ApiOperation(value = "获取存储卷列表", notes = "", response = ResponseEntity.class, tags = {"storageVolume"})
    @GetMapping
    public ResponseEntity list(
            @RequestParam Integer hostId,
            @RequestParam String poolName
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.listStorageVolume(host, poolName);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "获取存储卷详情", notes = "", response = ResponseEntity.class, tags = {"storageVolume"})
    @GetMapping("/host/{hostId}/poolName/{poolName}/volumeName/{volumeName}")
    public ResponseEntity get(
            @PathVariable Integer hostId,
            @PathVariable String poolName,
            @PathVariable String volumeName
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.getStorageVolumebyName(host, poolName, volumeName);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "创建存储卷", notes = "", response = ResponseEntity.class, tags = {"storageVolume"})
    @PostMapping
    public ResponseEntity add(
            @RequestParam Integer hostId,
            @RequestParam String xmlDesc,
            @RequestParam String poolName
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.createStorageVolume(host, poolName, xmlDesc);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "克隆存储卷", notes = "", response = ResponseEntity.class, tags = {"storageVolume"})
    @PostMapping("/clone")
    public ResponseEntity clone(
            @RequestParam Integer hostId,
            @RequestParam String poolName,
            @RequestParam String volumeName,
            @RequestParam String xmlDesc
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.cloneStorageVolume(host, poolName, volumeName, xmlDesc);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "删除存储卷", notes = "", response = ResponseEntity.class, tags = {"storageVolume"})
    @DeleteMapping
    public ResponseEntity del(
            @RequestParam Integer hostId,
            @RequestParam String poolName,
            @RequestParam String volumeName
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.deleteStorageVolume(host, poolName, volumeName);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
