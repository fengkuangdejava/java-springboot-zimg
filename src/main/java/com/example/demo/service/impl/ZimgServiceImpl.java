package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.ZimgConfig;
import com.example.demo.exception.DefaultException;
import com.example.demo.model.ResultEnum;
import com.example.demo.model.ZimgResult;
import com.example.demo.service.ZimgService;
import com.example.demo.util.HttpClientUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Service
@Slf4j
public class ZimgServiceImpl implements ZimgService {
    @Autowired
    ZimgConfig zimgConfig;
    private static final  String  uploadPath="/upload";
    private static final  String  deletePath ="/admin";
    @Override
    public String uploadImage(MultipartFile multipartFile) {
        File file = new File(multipartFile.getName());
        String url = uploadImage(file);
        file.deleteOnExit();
        return url;
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFiles) {
        List<String> list = multipartFiles.parallelStream().map(multipartFile->uploadImage(multipartFile)).collect(Collectors.toList());
        return list;
    }

    @Override
    public String uploadImage(File file) {
        String url = zimgConfig.getZimgServer()+uploadPath;
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        Map<String,String> headers = new HashMap<>(4);
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", ext.toLowerCase());
        headers.put("COOKIE", "qixun");
        ZimgResult zimgResult = HttpClientUtil.doPostFile(url,file,headers,ZimgResult.class);
        if(zimgResult.isRet()){
            String imgUrl = zimgConfig.getRouter().trim()+"/"+zimgResult.getInfo().getMd5();
            imgUrl= imgUrl.substring(1);
            log.info("imgUrl={}",imgUrl);
            return imgUrl;
        }else {
            throw new DefaultException(ResultEnum.UPLOAD_ZIMG_ERROR.getCode(),zimgResult.getError().getMessage());
        }
    }

    /**
     * 需要服务器开启远程修改权限 按自己需要修改 admin_rule='allow 127.0.0.1' 这一项
     * */
    @Override
    public boolean deleteImage(String md5) {
        String url = zimgConfig.getZimgServer()+deletePath;
        Map<String,String> params = new HashMap<>(2);
        params.put("md5",md5);
        params.put("t","1");
        ZimgResult zimgResult = HttpClientUtil.doGet(url,params,ZimgResult.class);
        //这里因忘记ssh密码，暂时无法登陆服务器修改配置，所以不知道delete返回结果具体是什么。以下两行只是示意处理流程
        return zimgResult.isRet();
    }

    public static void main(String[] args) {
        String str1 = "abc";
        String str2 = str1.substring(1);
        System.out.println(str2);
    }
}
