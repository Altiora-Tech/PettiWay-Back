package com.altioratech.pettiway.image.domain;

import org.springframework.web.multipart.MultipartFile;

public interface StorageServicePort {
    String uploadFile(MultipartFile file, String folder);
    void deleteFile(String fileUrl);
}