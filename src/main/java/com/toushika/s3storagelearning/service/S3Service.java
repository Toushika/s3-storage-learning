package com.toushika.s3storagelearning.service;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.toushika.s3storagelearning.requests.BucketInfo;
import com.toushika.s3storagelearning.requests.Employee;
import com.toushika.s3storagelearning.requests.FileInfo;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface S3Service {
    String createBucket(BucketInfo bucketInfo);

    List<Bucket> getBucketList();

    String deleteBucket(FileInfo fileInfo);

    String uploadFileToBucket(FileInfo fileInfo);

    List<Employee> getUploadedFileContent(FileInfo fileInfo);

    S3Object getS3Object(FileInfo fileInfo);

    String deleteFileFromBucket(FileInfo fileInfo);

    ByteArrayOutputStream downloadFileFromBucket(FileInfo fileInfo);
}
