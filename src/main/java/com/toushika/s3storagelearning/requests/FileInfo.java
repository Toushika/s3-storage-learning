package com.toushika.s3storagelearning.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileInfo {
    private String bucketName;
    private String objectKey;//file Name
}
