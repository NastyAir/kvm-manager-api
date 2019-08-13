package com.example.kvmmanger.controller;

import com.example.kvmmanger.common.Result;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageCtrl {

    @Value("${upload.image}")
    String filePath;

    @PostMapping("/file")
    @ApiOperation(value = "文件上传")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(RetResponse.error("上传失败，请选择文件"), HttpStatus.OK);

        }
        try {
            String fileName = FileUploadUtils.uploadFile(file, filePath);
            return new ResponseEntity<>(RetResponse.success(fileName), HttpStatus.OK);

        } catch (IOException e) {
            log.error(e.toString(), e);
            e.printStackTrace();
            return new ResponseEntity<>(RetResponse.error("上传失败"), HttpStatus.OK);
        }
    }

    @GetMapping("/file")
    @ApiOperation(value = "文件获取")
    public void getFile(@RequestParam String filename,
                        HttpServletResponse response) {
        FileUploadUtils.downImage(filename, filePath, response);
    }

    @GetMapping
    @ApiOperation(value = "镜像列表")
    public ResponseEntity getFile() {
        File file = new File(filePath);
        File[] array = file.listFiles();
        List<String> fileNames = Arrays.stream(array).map(File::getName).collect(Collectors.toList());
        return new ResponseEntity<>((RetResponse.success(fileNames)), HttpStatus.OK);
    }


}
