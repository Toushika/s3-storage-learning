package com.toushika.s3storagelearning.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toushika.s3storagelearning.requests.BucketInfo;
import com.toushika.s3storagelearning.requests.Employee;
import com.toushika.s3storagelearning.requests.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    @Value("${file.path}")
    private String filePath;
    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper;

    @Override
    public String createBucket(BucketInfo bucketInfo) {
        if (amazonS3.doesBucketExistV2(bucketInfo.getBucketName()))
            return bucketInfo.getBucketName() + " - Name already exist. Please enter a new one";
        else
            amazonS3.createBucket(bucketInfo.getBucketName());
        return bucketInfo.getBucketName() + " - bucket has been created";
    }

    @Override
    public List<Bucket> getBucketList() {
        return amazonS3.listBuckets();
    }

    @Override
    public String deleteBucket(FileInfo fileInfo) {
        if (amazonS3.doesBucketExistV2(fileInfo.getBucketName())) {
            deleteFileFromBucket(fileInfo);
            amazonS3.deleteBucket(fileInfo.getBucketName());
            return fileInfo.getBucketName() + " -  bucket has been deleted";
        } else {
            return fileInfo.getBucketName() + " -  bucket does not exist . So can not perform delete operation";
        }
    }

    @Override
    public String uploadFileToBucket(FileInfo fileInfo) {
        if (amazonS3.doesBucketExistV2(fileInfo.getBucketName())) {
            try {
                amazonS3.putObject(fileInfo.getBucketName(), fileInfo.getObjectKey(), new File(filePath));
            } catch (AmazonServiceException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return fileInfo.getObjectKey() + " - has been uploaded successfully.";
        } else {
            return fileInfo.getBucketName() + " is not exist. So file can not be uploaded here.";
        }
    }

    @Override
    public List<Employee> getUploadedFileContent(FileInfo fileInfo) {
        S3ObjectInputStream objectContent = amazonS3.getObject(fileInfo.getBucketName(), fileInfo.getObjectKey()).getObjectContent();
        var reader = new BufferedReader(new InputStreamReader(objectContent));
        var sb = new StringBuilder();
        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return objectMapper.readValue(sb.toString(), List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public S3Object getS3Object(FileInfo fileInfo) {
        return amazonS3.getObject(new GetObjectRequest(fileInfo.getBucketName(), fileInfo.getObjectKey()));
    }

    @Override
    public String deleteFileFromBucket(FileInfo fileInfo) {
        if (amazonS3.doesBucketExistV2(fileInfo.getBucketName()) && amazonS3.doesObjectExist(fileInfo.getBucketName(), fileInfo.getObjectKey())) {
            amazonS3.deleteObject(fileInfo.getBucketName(), fileInfo.getObjectKey());
            return fileInfo.getObjectKey() + " - has been deleted successfully.";
        }
        return fileInfo.getObjectKey() + " - can not deleted as it does not exist.";
    }

    @Override
    public ByteArrayOutputStream downloadFileFromBucket(FileInfo fileInfo) {
        try {
            S3Object s3object = amazonS3.getObject(new GetObjectRequest(fileInfo.getBucketName(), fileInfo.getObjectKey()));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
