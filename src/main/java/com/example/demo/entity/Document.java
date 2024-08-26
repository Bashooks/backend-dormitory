package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String typefile;

    @Column(nullable = false)
    private String documentTitle;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String status;

    // วันที่และเวลาที่อัปโหลด
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    // ใช้ PrePersist เพื่อกำหนดค่า uploadDate อัตโนมัติ
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }

    // เชื่อมโยงกับ Person entity
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;  // เพิ่มฟิลด์นี้เพื่อเก็บข้อมูลผู้ส่งเอกสาร
}
