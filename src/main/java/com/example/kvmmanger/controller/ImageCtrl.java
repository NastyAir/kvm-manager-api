package com.example.kvmmanger.controller;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.common.util.FileUploadUtils;
import com.example.kvmmanger.common.util.RetResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageCtrl {

    @Value("${upload.image}")
    String filePath;

    @PostMapping("/file")
    @ApiOperation(value = "文件上传", notes = "", response = ResponseEntity.class, tags = {"image"})
    public ResponseEntity<Result<Object>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(RetResponse.error("上传失败，请选择文件"), HttpStatus.OK);
        }
        try {
            String fileName = FileUploadUtils.uploadFile(file, filePath);
            return new ResponseEntity<>(RetResponse.success(fileName), HttpStatus.OK);
        } catch (IOException e) {
            log.error(e.toString(), e);
//            e.printStackTrace();
            throw new BusinessException(RetCode.FAIL, "上传失败");
//            return new ResponseEntity<>(RetResponse.error("上传失败"), HttpStatus.OK);
        }
    }

    @GetMapping("/file")
    @ApiOperation(value = "文件获取", notes = "", response = ResponseEntity.class, tags = {"image"})
    public void getFile(@RequestParam String filename,
                        HttpServletResponse response) {
        FileUploadUtils.downImage(filename, filePath, response);
    }

    @DeleteMapping("/file")
    @ApiOperation(value = "文件删除", notes = "", response = ResponseEntity.class, tags = {"image"})
    public ResponseEntity<Result<Object>> deleteFile(@RequestParam String filename,
                                                     HttpServletResponse response) {
        File file = new File(filePath + File.separator + filename);
        boolean isSuccess = file.delete();
        return new ResponseEntity<>(RetResponse.make(isSuccess), HttpStatus.OK);
    }

    @GetMapping("/treeList")
    @ApiOperation(value = "镜像树型列表", notes = "", response = ResponseEntity.class, tags = {"image"})
    public ResponseEntity<Result<List<String>>> treeList() {
        File file = new File(filePath);
        File[] array = file.listFiles();
        List<String> fileNames = Arrays.stream(array).map(File::getName).collect(Collectors.toList());
        return new ResponseEntity<>((RetResponse.success(fileNames)), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "镜像树型列表", notes = "", response = ResponseEntity.class, tags = {"image"})
    public ResponseEntity<Result<List<Map>>> list() {
        File file = new File(filePath);
        File[] array = file.listFiles();
        List<Map> fileNames = Arrays.stream(array != null ? array : new File[0]).map(this::getFileInfoMap).collect(Collectors.toList());
        return new ResponseEntity<>((RetResponse.success(fileNames)), HttpStatus.OK);
    }

    @GetMapping("/{filename}")
    @ApiOperation(value = "文件信息获取", notes = "", response = ResponseEntity.class, tags = {"image"})
    public ResponseEntity<Result<Map<String, Object>>> getFileInfo(@PathVariable String filename,
                                                                   HttpServletResponse response) {
        File file = new File(filePath + File.separator + filename);
        Map<String, Object> map = getFileInfoMap(file);
        return new ResponseEntity<>(RetResponse.success(map), HttpStatus.OK);
    }

    private Map<String, Object> getFileInfoMap(File file) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", file.getName());
        map.put("path", file.getPath());
        map.put("parent", file.getParent());
        map.put("canRead", file.canRead());
        map.put("size", file.length());
        map.put("sizeUnti", "B");
        map.put("lastModified", new Date(file.lastModified()));
        map.put("isFile", file.isFile());
        return map;
    }
}
