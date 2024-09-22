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
            return ResponseEntity.status(500).body(Map.of("error", List.of("Failed to upload files: " + e.getMessage())));
        }
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
   

}
