package com.example.demo.repository;

import com.example.demo.entity.Dormitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
    @Query("SELECT DISTINCT d.province FROM Dormitory d")
    List<String> findAllDistinctProvinces();

    List<Dormitory> findByAddedById(Long personId);

    List<Dormitory> findByStatusNot(String status);

    List<Dormitory> findByStatusNotIn(List<String> statuses);
}
