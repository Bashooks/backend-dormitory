package com.example.demo.service;

import com.example.demo.entity.Dormitory;
import com.example.demo.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.Optional;


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

    public List<String> uploadPdfFiles(MultipartFile[] pdfFiles) throws IOException {
        List<String> pdfUrls = new ArrayList<>();

        // อัปโหลดแต่ละไฟล์ PDF และเก็บ URL
        for (MultipartFile pdf : pdfFiles) {
            String pdfUrl = fileStorageService.storePDFFile(pdf);
            pdfUrls.add(pdfUrl);
        }

        // ส่งคืนรายการ URLs ของไฟล์ PDF
        return pdfUrls;
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

    public List<Dormitory> getDormitoriesByPersonId(Long personId) {
        return dormitoryRepository.findByAddedById(personId);
    }

    public Dormitory updateDormitoryStatus(Long dormitoryId, String status) {
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElse(null);
        if (dormitory != null) {
            dormitory.setStatus(status);
            return dormitoryRepository.save(dormitory);
        }
        return null;
    }

    public List<Dormitory> getDormitoriesExcludingStatus(String status) {
        return dormitoryRepository.findByStatusNot(status);
    }

    public List<Dormitory> getAvailableDormitories() {
        List<String> excludedStatuses = Arrays.asList("ไม่ผ่าน", "รอการอนุมัติ");
        return dormitoryRepository.findByStatusNotIn(excludedStatuses);
    }

    public void deleteDormitoryById(Long dormitoryId) {
        dormitoryRepository.deleteById(dormitoryId);
    }

    public Dormitory updateDormitoryDetails(Long id, String contractUrl, String receiptUrl, String folioUrl, String status) {
        // ค้นหา Dormitory โดยใช้ ID
        Optional<Dormitory> dormitoryOptional = dormitoryRepository.findById(id);
        
        if (dormitoryOptional.isPresent()) {
            Dormitory dormitory = dormitoryOptional.get();

            // อัปเดต URLs หากมีการส่งค่าเข้ามา
            if (contractUrl != null) dormitory.setContractUrl(contractUrl);
            if (receiptUrl != null) dormitory.setReceiptUrl(receiptUrl);
            if (folioUrl != null) dormitory.setFolioUrl(folioUrl);
            
            // อัปเดตสถานะ
            if (status != null) dormitory.setStatus(status);

            // บันทึกการเปลี่ยนแปลงในฐานข้อมูล
            return dormitoryRepository.save(dormitory);
        } else {
            throw new RuntimeException("Dormitory not found with id: " + id);
        }
    }

   
}
