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

    // ไดเรกทอรีสำหรับเก็บไฟล์รูปภาพ
    private final String imageUploadDir = "/uploads/images/";

    // ไดเรกทอรีสำหรับเก็บไฟล์ PDF
    private final String pdfUploadDir = "/uploads/pdf/";

    private final String pdfStorageLocation = "/uploads/pdf/";

    // Method สำหรับการเก็บไฟล์รูปภาพ
    public String storeFile(MultipartFile file) throws IOException {
        return storeFileToDirectory(file, imageUploadDir, "images");
    }

    // Method สำหรับการเก็บไฟล์ PDF
    public String storePDFFile(MultipartFile file) throws IOException {
        return storeFileToDirectory(file, pdfUploadDir, "pdf");
    }

    // Method สำหรับการจัดการไฟล์ใน directory ต่างๆ
    private String storeFileToDirectory(MultipartFile file, String uploadDir, String fileType) throws IOException {
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
        return "http://localhost:8080/" + fileType + "/" + newFileName;
    }

    public String storePdf(MultipartFile file) throws IOException {
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("File must be a PDF");
        }

        // ตั้งชื่อไฟล์ด้วย UUID เพื่อให้ไม่ซ้ำกัน
        String fileName = UUID.randomUUID().toString() + ".pdf";
        Path targetLocation = Paths.get(pdfStorageLocation).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);

        // คืน URL หรือ Path ของไฟล์กลับไป
        return targetLocation.toString();
    }
}
