package com.example.gaehwa2.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
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

    public String generateSasUrl(String containerName, String blobName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // SAS 토큰 생성 (읽기 전용, 1시간 유효)
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusHours(1),
                new BlobSasPermission().setReadPermission(true)
        );

        String sasToken = blobClient.generateSas(values);
        return blobClient.getBlobUrl() + "?" + sasToken;
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
