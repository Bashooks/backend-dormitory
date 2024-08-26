package com.example.demo.entity;


import java.time.LocalDate;

import org.hibernate.usertype.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ข้อมูลส่วนตัวเพิ่มเติม
    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String userType = "user";
}
