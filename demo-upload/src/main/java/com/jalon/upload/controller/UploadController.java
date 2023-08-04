package com.jalon.upload.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RestController
public class UploadController {

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "文件为空";
        }
        String localPath = "D:/upload/";
        File dir = new File(localPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        String filename = file.getOriginalFilename();
        file.transferTo(new File(localPath+filename));
        return "upload success";
    }

}
