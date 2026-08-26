package com.sky.controller.admin;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    //本地图片存放物理路径
    private static final String LOCAL_SAVE_PATH = "D:/Learn/sky/nginx-1.20.2/html/uploads/";
    //http访问前缀
    private static final String HTTP_PREFIX = "http://localhost/uploads/";

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        if(file.isEmpty()){
            return Result.error("文件不能为空");
        }
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //生成唯一文件名，防止覆盖
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffix;

        File dest = new File(LOCAL_SAVE_PATH + newFileName);
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件到本地磁盘
            file.transferTo(dest);
            //返回前端完整http访问地址，存入数据库image字段
            String url = HTTP_PREFIX + newFileName;
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败",e);
            return Result.error("上传失败");
        }
    }
}
