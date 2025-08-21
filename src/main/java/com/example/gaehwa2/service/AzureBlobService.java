package com.example.gaehwa2.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AzureBlobService {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    /**
     * MultipartFile → Azure Blob 업로드 후 URL 반환
     */
    public String uploadFile(String filePath, String blobFileName) throws IOException {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }

        BlobClient blobClient = containerClient.getBlobClient(blobFileName);

        File file = new File(filePath);
        try (InputStream inputStream = new FileInputStream(file)) {
            blobClient.upload(inputStream, file.length(), true);
        }

        return blobClient.getBlobUrl();
    }


//    /**
//     * byte[] → Azure Blob 업로드 후 URL 반환
//     */
//    public String uploadBytes(byte[] data, String originalFileName) {
//        String fileName = UUID.randomUUID() + "_" + originalFileName;
//
//        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
//        if (!containerClient.exists()) {
//            containerClient.create();
//        }
//
//        BlobClient blobClient = containerClient.getBlobClient(fileName);
//
//        blobClient.upload(new java.io.ByteArrayInputStream(data), data.length, true);
//
//        return blobClient.getBlobUrl();
//    }
}
