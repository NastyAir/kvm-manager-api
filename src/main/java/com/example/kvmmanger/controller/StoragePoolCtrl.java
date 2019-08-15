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
@RequestMapping("/storagePool")
public class StoragePoolCtrl {

    private final KvmService kvmService;
    private final HostService hostService;

    @Autowired
    public StoragePoolCtrl(KvmService kvmService, HostService hostService) {
        this.kvmService = kvmService;
        this.hostService = hostService;
    }

    @ApiOperation(value = "获取存储池列表", notes = "", response = ResponseEntity.class, tags = {"storagePool"})
    @GetMapping
    public ResponseEntity list(
            @RequestParam Integer hostId
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.listStoragePool(host);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "获取存储池详情", notes = "", response = ResponseEntity.class, tags = {"storagePool"})
    @GetMapping("/host/{hostId}/name/{name}")
    public ResponseEntity get(
            @PathVariable Integer hostId,
            @PathVariable String name
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.getStoragePoolByName(host, name);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "创建存储池（临时）", notes = "", response = ResponseEntity.class, tags = {"storagePool"})
    @PostMapping("/temp")
    public ResponseEntity add(
            @RequestParam Integer hostId,
            @RequestParam String xmlDesc
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.createStoragePool(host, xmlDesc);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "定义存储池", notes = "", response = ResponseEntity.class, tags = {"storagePool"})
    @PostMapping
    public ResponseEntity define(
            @RequestParam Integer hostId,
            @RequestParam String xmlDesc
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.defineStoragePool(host, xmlDesc);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "删除存储池", notes = "", response = ResponseEntity.class, tags = {"storagePool"})
    @DeleteMapping()
    public ResponseEntity del(
            @RequestParam Integer hostId,
            @RequestParam String name
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        try {
            message = kvmService.delStoragePool(host, name);
        } catch (LibvirtException e) {
            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "连接KVM异常，获取信息失败");
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
