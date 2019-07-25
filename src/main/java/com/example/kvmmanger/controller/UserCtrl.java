package com.example.kvmmanger.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.kvmmanger.common.util.RetResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserCtrl {
    @ApiOperation(value = "用户详情", notes = "", response = ResponseEntity.class, tags = {"user"})
    @GetMapping
    public ResponseEntity get(){
        String info="{\"roles\":[\"admin\"],\"introduction\":\"I am a super administrator\",\"avatar\":\"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\",\"name\":\"Super Admin\"}";
        return new ResponseEntity<>(RetResponse.success("成功", JSONObject.parse(info)), HttpStatus.OK);
    }

}
