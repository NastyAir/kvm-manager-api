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

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/host")
public class HostCtrl {

    private final KvmService kvmService;
    private final HostService hostService;

    @Autowired
    public HostCtrl(KvmService kvmService, HostService hostService) {
        this.kvmService = kvmService;
        this.hostService = hostService;
    }

    @ApiOperation(value = "获取主机列表", notes = "", response = ResponseEntity.class, tags = {"host"})
    @GetMapping
    public ResponseEntity list(
            @RequestParam(required = false) String hostName,
            @RequestParam(required = false, defaultValue = "ASC") String order,
            @RequestParam(required = false, defaultValue = "id") String orderColumn,
            @RequestParam(required = false, defaultValue = "0") String currentPage,
            @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Result message = hostService.list(hostName, order, orderColumn, currentPage, pageSize);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "新增主机", notes = "", response = ResponseEntity.class, tags = {"host"})
    @PostMapping
    public ResponseEntity add(@RequestBody @Valid Host host) {
        Result message = hostService.add(host);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

}
