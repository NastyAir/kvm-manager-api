package com.example.kvmmanger.controller;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.entity.Host;
import com.example.kvmmanger.service.HostService;
import com.example.kvmmanger.service.KvmService;
import io.swagger.annotations.ApiOperation;
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
    @GetMapping("/{uuid}/host/{hostId}")
    public ResponseEntity get(
            @PathVariable Integer hostId,
            @PathVariable String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.getDomainByUuid(host, uuid);
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
    @DeleteMapping("/{uuid}/host/{hostId}")
    public ResponseEntity del(
            @PathVariable Integer hostId,
            @PathVariable String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.undefineDomain(host, uuid);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @ApiOperation(value = "操作客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/action")
    public ResponseEntity action(
            @RequestParam Integer hostId,
            @RequestParam String uuid,
            @RequestParam String action
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, action);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "开启客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/start")
    public ResponseEntity start(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "start");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "关闭客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/shutdown")
    public ResponseEntity shutdown(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "shutdown");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "重启客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/reboot")
    public ResponseEntity reboot(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "reboot");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "关闭客户机电源", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/destroy")
    public ResponseEntity destroy(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "destroy");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "挂起客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/suspend")
    public ResponseEntity suspend(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "suspend");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "恢复客户机", notes = "", response = ResponseEntity.class, tags = {"domain"})
    @PutMapping("/resume")
    public ResponseEntity resume(
            @RequestParam Integer hostId,
            @RequestParam String uuid
    ) {
        Host host = hostService.getOne(hostId);
        Result message = null;
        message = kvmService.domainAction(host, uuid, "resume");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
