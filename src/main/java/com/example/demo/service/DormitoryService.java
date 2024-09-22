package com.example.demo.service;

import com.example.demo.entity.Dormitory;
import com.example.demo.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DormitoryService {

    private final DormitoryRepository dormitoryRepository;
    private final FileStorageService fileStorageService;
   

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
}

