package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ZimgService {
    String uploadImage(MultipartFile multipartFile);
    List<String> uploadImage(List<MultipartFile> multipartFiles);
    String uploadImage(File file);
    boolean deleteImage(String md5);
}
