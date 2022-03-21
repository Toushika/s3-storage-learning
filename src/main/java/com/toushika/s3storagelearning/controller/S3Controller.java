package com.toushika.s3storagelearning.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.toushika.s3storagelearning.requests.BucketInfo;
import com.toushika.s3storagelearning.requests.Employee;
import com.toushika.s3storagelearning.requests.FileInfo;
import com.toushika.s3storagelearning.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/create-bucket")
    public String createBucket(@RequestBody BucketInfo bucketInfo) {
        log.info("S3Controller :: createBucket :: bucketInfo :: {}", bucketInfo);
        return s3Service.createBucket(bucketInfo);
    }

    @PostMapping("/get-bucket-list")
    public List<Bucket> getBucketList() {
        return s3Service.getBucketList();
    }

    @PostMapping("/delete-bucket")
    public String deleteBucket(@RequestBody FileInfo fileInfo) {
        log.info("S3Controller :: deleteBucket :: bucketInfo :: {}", fileInfo);
        return s3Service.deleteBucket(fileInfo);
    }

    @PostMapping("/upload-file-to-bucket")
    public String uploadFileToBucket(@RequestBody FileInfo fileInfo) {
        log.info("S3Controller :: uploadFileToBucket :: fileInfo :: {}", fileInfo);
        return s3Service.uploadFileToBucket(fileInfo);
    }

    @PostMapping("/upload-file-content")
    public List<Employee> uploadFileContent(@RequestBody FileInfo fileInfo) {
        log.info("S3Controller :: uploadFileToBucket :: fileInfo :: {}", fileInfo);
        return s3Service.getUploadedFileContent(fileInfo);
    }

    @PostMapping("/delete-file-from-bucket")
    public void deleteFileFromBucket(@RequestBody FileInfo fileInfo) {
        log.info("S3Controller :: deleteFileFromBucket :: fileInfo :: {}", fileInfo);
        s3Service.deleteFileFromBucket(fileInfo);
    }

    @GetMapping("/download-file-from-bucket")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String bucketName, @RequestParam String objectKey) {
        FileInfo fileInfo = FileInfo.builder()
                .bucketName(bucketName)
                .objectKey(objectKey)
                .build();
        ByteArrayOutputStream downloadInputStream = s3Service.downloadFileFromBucket(fileInfo);
        return ResponseEntity.ok()
                .contentType(contentType(fileInfo.getObjectKey()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getObjectKey() + "\"")
                .body(downloadInputStream.toByteArray());
    }

    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        System.out.println("fileExtension = " + fileExtension);
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "json":
                return MediaType.APPLICATION_JSON;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
