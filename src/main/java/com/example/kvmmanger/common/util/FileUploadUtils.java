package com.example.kvmmanger.common.util;

import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

public class FileUploadUtils {
    public static String uploadFile(MultipartFile file, String storePath) throws IOException {
        String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = "file" + UUID.randomUUID() + suffixName;
        return uploadFile(file, storePath, fileName);
    }
    public static String uploadFileOriginal(MultipartFile file, String storePath) throws IOException {
        return uploadFile(file, storePath, file.getOriginalFilename());
    }

    public static String uploadFile(MultipartFile file, String storePath, String fileName) throws IOException {
//        String fileName = "file" + UUID.randomUUID() + file.getOriginalFilename();
        File filePath = new File(storePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File dest = new File(filePath + "/" + fileName);
        file.transferTo(dest);
        return fileName;
    }

    public static void downImage(String filename, String storePath, HttpServletResponse response) {
        if (StringUtils.containsAny(filename, "./", "../", "%", ".\\")) {
            throw new BusinessException(RetCode.FAIL, "非法路径");
        }
        File filePath = new File(storePath);
        File file = new File(filePath + File.separator + filename);
        if (file.exists()) { //判断文件父目录是否存在
            String fileType = filename.substring(filename.lastIndexOf(".") + 1);
            if (StringUtils.equalsIgnoreCase(fileType, "jpg")) {
                fileType = "jpeg";
            }
            response.setContentType("image/" + fileType);
//            response.setContentType("application/force-download");
//            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void downFile(String filename, String storePath, HttpServletResponse response) {
        if (StringUtils.containsAny(filename, "./", "../", "%", ".\\")) {
            throw new BusinessException(RetCode.FAIL, "非法路径");

        }
        File filePath = new File(storePath);
        File file = new File(filePath + "/" + filename);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            logger.info("----------file download" + filename);
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
