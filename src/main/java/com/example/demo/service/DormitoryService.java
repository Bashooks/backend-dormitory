package com.example.demo.service;

import com.example.demo.entity.Dormitory;
import com.example.demo.repository.DormitoryRepository;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Paths;

@Service
public class DormitoryService {

    private final DormitoryRepository dormitoryRepository;
    private final FileStorageService fileStorageService;
    private final Path rootLocation = Paths.get("uploads/pdf");

    @Autowired
    public DormitoryService(DormitoryRepository dormitoryRepository, FileStorageService fileStorageService) {
        this.dormitoryRepository = dormitoryRepository;
        this.fileStorageService = fileStorageService;

    }

    public Dormitory saveDormitory(Dormitory dormitory) {
        return dormitoryRepository.save(dormitory);
    }

    public Dormitory saveDormitoryWithImages(Dormitory dormitory, MultipartFile[] files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // อัปโหลดแต่ละไฟล์และเก็บ URL
        for (MultipartFile file : files) {
            String imageUrl = fileStorageService.storeFile(file);
            imageUrls.add(imageUrl);
        }

        // ตั้งค่า URLs ของภาพใน Dormitory entity
        dormitory.setImageUrls(imageUrls);

        // บันทึก Dormitory entity พร้อม URLs ของภาพลงในฐานข้อมูล
        return dormitoryRepository.save(dormitory);
    }

    public List<Dormitory> getAllDormitories() {
        return dormitoryRepository.findAll();
    }

    public Dormitory getDormitoryById(Long id) {
        return dormitoryRepository.findById(id).orElse(null);
    }

    public List<String> getAllProvinces() {
        return dormitoryRepository.findAllDistinctProvinces();
    }

    public List<String> uploadMultiplePdfs(MultipartFile[] files) throws IOException {

        if (!Files.exists(this.rootLocation)) {
            Files.createDirectories(this.rootLocation); // สร้างโฟลเดอร์ถ้ายังไม่มี
        }
        // สร้าง List สำหรับเก็บตำแหน่งไฟล์ที่อัปโหลด
        List<String> filePaths = new ArrayList<>();

        // วนลูปเพื่ออัปโหลดแต่ละไฟล์
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            // ตรวจสอบว่ามีไฟล์ชื่อเดียวกันอยู่หรือไม่ ถ้ามีให้เปลี่ยนชื่อ
            if (Files.exists(destinationFile)) {
                String newFileName = UUID.randomUUID().toString() + "_" + fileName;
                destinationFile = this.rootLocation.resolve(Paths.get(newFileName))
                        .normalize().toAbsolutePath();
                fileName = newFileName;
            }

            // คัดลอกไฟล์ไปยังปลายทาง
            Files.copy(file.getInputStream(), destinationFile);

            // เพิ่มตำแหน่งไฟล์ที่อัปโหลดลงในรายการ
            filePaths.add(destinationFile.toString());
        }

        // คืนค่าเป็น List ของตำแหน่งไฟล์ที่อัปโหลด
        return filePaths;
    }

}
