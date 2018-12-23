package com.springbootdev.amazon.s3.example.aws.service.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.springbootdev.amazon.s3.example.aws.model.ImageAttributes;
import com.springbootdev.amazon.s3.example.aws.service.AmazonS3ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService
{
    private String awsBucketName;
    private String s3endPointUrl;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

    @Autowired
    public AmazonS3ClientServiceImpl(String awsBucketName, String s3endPointUrl)
    {
        this.amazonS3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        this.amazonS3.setRegion(usWest2);
        this.awsBucketName = awsBucketName;
        this.s3endPointUrl = s3endPointUrl;
    }

    @Async
    public void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess)
    {
        String fileName = multipartFile.getOriginalFilename();

        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsBucketName, fileName, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);

            //removing the file created in the server
            file.delete();
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
    }

    @Async
    public void deleteFileFromS3Bucket(String fileName)
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(this.awsBucketName, fileName));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }

    @Override
    public List<ImageAttributes> getFiles() {
        logger.info("Listing images");
        List<ImageAttributes> res = new ArrayList<>();
        ObjectListing objectListing = this.amazonS3.listObjects(this.awsBucketName);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()){
            ImageAttributes imageAttributes = new ImageAttributes();
            if (objectSummary.getKey().contains(".jpg")){
                imageAttributes.setImageName(objectSummary.getKey());
                imageAttributes.setSize(objectSummary.getSize());
                imageAttributes.setBucketName(this.awsBucketName);
                imageAttributes.setImageUrl(this.s3endPointUrl + "/" + this.awsBucketName + "/" + objectSummary.getKey());
            }
            res.add(imageAttributes);
        }
        return res;
    }
}