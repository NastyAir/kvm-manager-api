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
@RequestMapping("/domain")
public class DomainCtrl {
    private final KvmService kvmService;
    private final HostService hostService;

    @Autowired
    public DomainCtrl(KvmService kvmService, HostService hostService) {
        this.kvmService = kvmService;
        this.hostService = hostService;
    }
    @ApiOperation(value = "获取客户机列表", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @GetMapping
    public ResponseEntity list(
            @RequestParam Integer hostId
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.listDomain(host);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "获取客户机详情", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @GetMapping("/{domainId}/host/{hostId}")
    public ResponseEntity get(
            @PathVariable Integer hostId,
            @PathVariable Integer domainId
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.getDomainbyId(host, domainId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @ApiOperation(value = "创建客户机（临时）", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PostMapping("/temp")
    public ResponseEntity add(
            @RequestParam Integer hostId,
            @RequestParam String xmlDesc
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.createDomain(host, xmlDesc);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "定义客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PostMapping
    public ResponseEntity define(
            @RequestParam Integer hostId,
            @RequestParam String xmlDesc
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.defineDomain(host, xmlDesc);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "删除客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @DeleteMapping()
    public ResponseEntity del(
            @RequestParam Integer hostId,
            @RequestParam String name
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.undefineDomain(host, name);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
