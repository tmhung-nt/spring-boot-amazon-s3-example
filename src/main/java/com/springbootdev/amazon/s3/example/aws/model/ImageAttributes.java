package com.springbootdev.amazon.s3.example.aws.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageAttributes {
    private String imageName;
    private String imageUrl;
    private String bucketName;
    private Long size = 0L;
}

