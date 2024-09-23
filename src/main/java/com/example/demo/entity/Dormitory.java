package com.example.demo.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dormitory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // ชื่อหอพัก

    @Column(nullable = false)
    private String district; // ตำบล

    @Column(nullable = false)
    private String subDistrict; // อำเภอ

    @Column(nullable = false)
    private String province; // จังหวัด

    @Column(nullable = false)
    private Double price; // ราคา

    @Column(nullable = false)
    private String furniture; // เฟอร์นิเจอร์

    @Column(nullable = false)
    private String contact; // ช่องทางติดต่อ

    @Column(columnDefinition = "TEXT") 
    private String description; // รายละเอียดหอพัก

    @ElementCollection
    @CollectionTable(name = "dormitory_images", joinColumns = @JoinColumn(name = "dormitory_id"))
    @Column(name = "image_url")
    private List<String> imageUrls; 
    
    @Column(nullable = false)
    private String status; // สถานะหอพัก เช่น ว่าง, ไม่ว่าง

    @Column(nullable = true)
    private String contractUrl; // URL สัญญาหอพัก

    @Column(nullable = true)
    private String receiptUrl; // URL บิลใบเสร็จ

    @Column(nullable = true)
    private String folioUrl; // URL Folio// รายการ URL ของภาพ
}
