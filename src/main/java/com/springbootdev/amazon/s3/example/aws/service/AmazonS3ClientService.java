package com.springbootdev.amazon.s3.example.aws.service;

import com.springbootdev.amazon.s3.example.aws.model.ImageAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmazonS3ClientService
{
    void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);

    void deleteFileFromS3Bucket(String fileName);

    List<ImageAttributes> getFiles();
}
