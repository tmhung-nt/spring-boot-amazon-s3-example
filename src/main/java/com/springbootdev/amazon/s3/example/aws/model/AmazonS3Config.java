package com.springbootdev.amazon.s3.example.aws.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AmazonS3Config
{
    @Value("${aws.bucket.name}")
    private String awsBucketName;

    @Bean(name = "awsBucketName")
    public String getAwsBucketName() {
        return awsBucketName;
    }

    @Value("${endpointUrl}")
    private String s3endPointUrl;

    @Bean(name = "s3endPointUrl")
    public String getS3endPointUrl() {
        return s3endPointUrl;
    }
}