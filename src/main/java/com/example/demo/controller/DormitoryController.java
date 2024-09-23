package com.example.demo.controller;

import com.example.demo.entity.Dormitory;
import com.example.demo.service.DormitoryService;
import com.example.demo.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dormitories")
@CrossOrigin(origins = "http://localhost:4200")
public class DormitoryController {

    private final DormitoryService dormitoryService;
    private final FileStorageService fileStorageService;

    @Autowired
    public DormitoryController(DormitoryService dormitoryService, FileStorageService fileStorageService) {
        this.dormitoryService = dormitoryService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public Dormitory createDormitory(@RequestBody Dormitory dormitory) {
        return dormitoryService.saveDormitory(dormitory);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, List<String>>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        Map<String, List<String>> response = new HashMap<>();

        try {
            for (MultipartFile file : files) {
                String fileUrl = fileStorageService.storeFile(file);
                fileUrls.add(fileUrl);
            }
            response.put("fileUrls", fileUrls);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", List.of("Failed to upload files: " + e.getMessage())));
        }
    }

    @PostMapping("/upload-pdfs")
    public ResponseEntity<List<String>> uploadPdfFiles(@RequestParam("pdfs") MultipartFile[] pdfs) throws IOException {
        List<String> pdfUrls = dormitoryService.uploadPdfFiles(pdfs);
        return ResponseEntity.ok(pdfUrls);
    }

    @GetMapping
    public ResponseEntity<List<Dormitory>> getAllDormitories() {
        List<Dormitory> dormitories = dormitoryService.getAllDormitories();
        return ResponseEntity.ok(dormitories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dormitory> getDormitoryById(@PathVariable("id") Long id) {
        Dormitory dormitory = dormitoryService.getDormitoryById(id);
        if (dormitory != null) {
            return ResponseEntity.ok(dormitory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/provinces")
    public List<String> getProvinces() {
        return dormitoryService.getAllProvinces();
    }

    @GetMapping("/person/{personId}")
    public List<Dormitory> getDormitoriesByPersonId(@PathVariable("personId") Long personId) {
        return dormitoryService.getDormitoriesByPersonId(personId);
    }

    // API สำหรับอัปเดตสถานะของหอพัก
    @PutMapping("/{dormitoryId}/status")
    public Dormitory updateDormitoryStatus(@PathVariable("dormitoryId") Long dormitoryId,
            @RequestParam("status") String status) {
        return dormitoryService.updateDormitoryStatus(dormitoryId, status);
    }

    @GetMapping("/available")
    public List<Dormitory> getDormitoriesExcludingNotPassed() {
        return dormitoryService.getDormitoriesExcludingStatus("ไม่ผ่าน");
    }

    @GetMapping("/available2")
    public List<Dormitory> getAvailableDormitories() {
        return dormitoryService.getAvailableDormitories();
    }

    @DeleteMapping("/{dormitoryId}")
    public ResponseEntity<Void> deleteDormitory(@PathVariable("dormitoryId") Long dormitoryId) {
        Dormitory dormitory = dormitoryService.getDormitoryById(dormitoryId);
        if (dormitory != null) {
            dormitoryService.deleteDormitoryById(dormitoryId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PutMapping("/{id}/update-urls")
    public Dormitory updateDormitoryUrls(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> urlUpdateRequest) {
        
        // ดึงค่า contractUrl, receiptUrl, folioUrl และ status จาก Map
        String contractUrl = urlUpdateRequest.get("contractUrl");
        String receiptUrl = urlUpdateRequest.get("receiptUrl");
        String folioUrl = urlUpdateRequest.get("folioUrl");
        String status = urlUpdateRequest.get("status"); // เพิ่มการอัปเดตสถานะ

        // เรียก service เพื่ออัปเดตข้อมูล
        return dormitoryService.updateDormitoryDetails(id, contractUrl, receiptUrl, folioUrl, status);
    }
}
