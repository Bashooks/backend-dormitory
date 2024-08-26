package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

   
    private final String uploadDir = "/uploads/images/";

    public String storeFile(MultipartFile file) throws IOException {
        // สร้างไดเรกทอรีถ้ายังไม่มี
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // สร้างชื่อไฟล์ใหม่เพื่อป้องกันการซ้ำกัน
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        // ตรวจสอบว่าไฟล์มี extension หรือไม่
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // สร้างชื่อไฟล์ใหม่ด้วย UUID และ extension เดิม
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(newFileName);

        // บันทึกไฟล์
        Files.copy(file.getInputStream(), filePath);

        // ส่งคืน URL ที่สามารถเข้าถึงไฟล์นี้ได้
        return "http://localhost:8080/images/" + newFileName;
    }
}
