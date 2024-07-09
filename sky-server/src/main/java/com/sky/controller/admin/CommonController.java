package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 说明
 */
    @RestController
    @RequestMapping("/admin/common")
    @Api(tags = "通用接口")
    @Slf4j
    public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    //接受上传文件
//    类型必须为MultipartFile
//    形参名必须与参数名一致
    public Result<String> upload(MultipartFile file) {
//        获取文件
        String uploadedFilename = file.getOriginalFilename();
        log.info("上传文件：{}", uploadedFilename);
//        接收文件
//        给文件生成唯一的文件名
        String uuid = UUID.randomUUID().toString();
//        截取文件后缀名
        int lastIndexOfDot = uploadedFilename.lastIndexOf(".");
        String fileExt = uploadedFilename.substring(lastIndexOfDot);
        String objectName=uuid+fileExt;
//        上传给oss存储
        try {
            String url=aliOssUtil.upload(file.getBytes(),objectName);
            //        返回图片完整路径给前端 回显
            return Result.success(url);
        } catch (IOException e) {
            log.error("上传文件失败.",e);
        }
        return Result.error("上传文件失败!");
    }


    public static void main(String[] args) {
        String uploadFilename = "1.32.png";
        int lastIndexOfDot = uploadFilename.lastIndexOf(".");
        String fileExt = uploadFilename.substring(lastIndexOfDot);
        System.out.println("fileExt=" + fileExt);

    }
}


